package social.karotter.client

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.content.FileProvider
import org.json.JSONObject
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.security.MessageDigest

data class AppUpdateInfo(
    val tagName: String,
    val title: String,
    val notes: String,
    val apkUrl: String,
    val apkName: String,
    val apkSize: Long,
    val sha256: String?,
    val releaseUrl: String
)

sealed interface InstallApkResult {
    data object Launched : InstallApkResult
    data object PermissionRequired : InstallApkResult
    data class Failure(val message: String) : InstallApkResult
}

object AppUpdateManager {
    private const val LATEST_RELEASE_URL =
        "https://api.github.com/repos/NamiCode-Dev/Karoha/releases/latest"
    private const val USER_AGENT = "Karoha-Android-Updater"

    fun checkForUpdate(context: Context): Result<AppUpdateInfo?> = runCatching {
        val connection = openConnection(LATEST_RELEASE_URL).apply {
            setRequestProperty("Accept", "application/vnd.github+json")
            setRequestProperty("X-GitHub-Api-Version", "2022-11-28")
        }
        val status = connection.responseCode
        if (status == HttpURLConnection.HTTP_NOT_FOUND) {
            connection.disconnect()
            return@runCatching null
        }
        if (status !in 200..299) {
            val message = connection.errorStream?.bufferedReader()?.use { it.readText() }.orEmpty()
            connection.disconnect()
            error("GitHub Releasesを確認できませんでした（HTTP $status）${message.take(160)}")
        }
        val root = connection.inputStream.bufferedReader().use { JSONObject(it.readText()) }
        connection.disconnect()
        val tagName = root.optString("tag_name").trim()
        val currentVersion = packageInfo(context, context.packageName)?.versionName.orEmpty()
        if (tagName.isBlank() || compareVersions(tagName, currentVersion) <= 0) {
            return@runCatching null
        }
        val apkAssets = root.optJSONArray("assets")
            ?.let { assets ->
                buildList {
                    for (index in 0 until assets.length()) {
                        val asset = assets.optJSONObject(index) ?: continue
                        val name = asset.optString("name")
                        val url = asset.optString("browser_download_url")
                        if (name.endsWith(".apk", ignoreCase = true) && url.startsWith("https://")) {
                            add(asset)
                        }
                    }
                }
            }
            .orEmpty()
        val asset = apkAssets.maxByOrNull { apkAssetPriority(it.optString("name")) }
            ?: return@runCatching null
        AppUpdateInfo(
            tagName = tagName,
            title = root.optString("name").takeIf { it.isNotBlank() } ?: tagName,
            notes = root.optString("body"),
            apkUrl = asset.getString("browser_download_url"),
            apkName = asset.optString("name", "Karoha-$tagName.apk"),
            apkSize = asset.optLong("size", -1L),
            sha256 = asset.optString("digest").removePrefix("sha256:")
                .takeIf { it.matches(Regex("[0-9a-fA-F]{64}")) },
            releaseUrl = root.optString("html_url")
        )
    }

    fun downloadApk(
        context: Context,
        update: AppUpdateInfo,
        onProgress: (Float) -> Unit
    ): Result<File> = runCatching {
        val updateDirectory = File(context.cacheDir, "updates").apply { mkdirs() }
        val safeTag = update.tagName.replace(Regex("[^A-Za-z0-9._-]"), "_")
        val destination = File(updateDirectory, "Karoha-$safeTag.apk")
        val temporary = File(updateDirectory, "Karoha-$safeTag.download")
        if (temporary.exists()) temporary.delete()

        val connection = openConnection(update.apkUrl).apply {
            setRequestProperty("Accept", "application/octet-stream")
        }
        val status = connection.responseCode
        if (status !in 200..299) {
            connection.disconnect()
            error("APKをダウンロードできませんでした（HTTP $status）")
        }
        val expectedSize = connection.contentLengthLong.takeIf { it > 0L }
            ?: update.apkSize.takeIf { it > 0L }
        var downloaded = 0L
        connection.inputStream.use { input ->
            temporary.outputStream().buffered().use { output ->
                val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                while (true) {
                    val count = input.read(buffer)
                    if (count < 0) break
                    output.write(buffer, 0, count)
                    downloaded += count
                    expectedSize?.let {
                        onProgress((downloaded.toFloat() / it.toFloat()).coerceIn(0f, 1f))
                    }
                }
            }
        }
        connection.disconnect()
        if (expectedSize != null && downloaded != expectedSize) {
            temporary.delete()
            error("APKのダウンロードが完了していません")
        }
        update.sha256?.let { expectedDigest ->
            val actualDigest = sha256(temporary)
            if (!actualDigest.equals(expectedDigest, ignoreCase = true)) {
                temporary.delete()
                error("APKの整合性を確認できませんでした")
            }
        }
        verifyUpdatePackage(context, temporary)
        if (destination.exists()) destination.delete()
        if (!temporary.renameTo(destination)) {
            temporary.copyTo(destination, overwrite = true)
            temporary.delete()
        }
        onProgress(1f)
        destination
    }

    fun launchInstaller(context: Context, apk: File): InstallApkResult {
        if (!apk.isFile) return InstallApkResult.Failure("更新APKが見つかりません")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
            !context.packageManager.canRequestPackageInstalls()
        ) {
            return InstallApkResult.PermissionRequired
        }
        return runCatching {
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                apk
            )
            context.startActivity(
                Intent(Intent.ACTION_INSTALL_PACKAGE).apply {
                    data = uri
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true)
                    putExtra(Intent.EXTRA_RETURN_RESULT, false)
                }
            )
        }.fold(
            onSuccess = { InstallApkResult.Launched },
            onFailure = { InstallApkResult.Failure(it.message ?: "インストーラーを開けませんでした") }
        )
    }

    fun installPermissionIntent(context: Context): Intent =
        Intent(
            Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
            Uri.parse("package:${context.packageName}")
        )

    private fun verifyUpdatePackage(context: Context, apk: File) {
        val packageManager = context.packageManager
        val archive = packageInfo(packageManager, apk.absolutePath)
            ?: error("APKのパッケージ情報を読み取れません")
        if (archive.packageName != context.packageName) {
            error("Karoha用ではないAPKが検出されました")
        }
        val current = packageInfo(context, context.packageName)
            ?: error("現在のアプリ情報を確認できません")
        if (versionCode(archive) <= versionCode(current)) {
            error("現在より新しいAPKではありません")
        }
        val currentSigners = signerDigests(current)
        val archiveSigners = signerDigests(archive)
        if (currentSigners.isEmpty() || archiveSigners.isEmpty() || currentSigners != archiveSigners) {
            error("APKの署名が現在のKarohaと一致しません")
        }
    }

    @Suppress("DEPRECATION")
    private fun packageInfo(context: Context, packageName: String): PackageInfo? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.packageManager.getPackageInfo(
                packageName,
                PackageManager.PackageInfoFlags.of(PackageManager.GET_SIGNING_CERTIFICATES.toLong())
            )
        } else {
            context.packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
        }

    @Suppress("DEPRECATION")
    private fun packageInfo(packageManager: PackageManager, archivePath: String): PackageInfo? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.getPackageArchiveInfo(
                archivePath,
                PackageManager.PackageInfoFlags.of(PackageManager.GET_SIGNING_CERTIFICATES.toLong())
            )
        } else {
            packageManager.getPackageArchiveInfo(archivePath, PackageManager.GET_SIGNATURES)
        }

    @Suppress("DEPRECATION")
    private fun signerDigests(info: PackageInfo): Set<String> {
        val signatures = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val signingInfo = info.signingInfo ?: return emptySet()
            if (signingInfo.hasMultipleSigners()) {
                signingInfo.apkContentsSigners
            } else {
                signingInfo.signingCertificateHistory
            }
        } else {
            info.signatures
        }
        return signatures.orEmpty().map { signature ->
            MessageDigest.getInstance("SHA-256")
                .digest(signature.toByteArray())
                .joinToString("") { "%02x".format(it) }
        }.toSet()
    }

    @Suppress("DEPRECATION")
    private fun versionCode(info: PackageInfo): Long =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) info.longVersionCode
        else info.versionCode.toLong()

    private fun compareVersions(remote: String, local: String): Int {
        val remoteParts = versionParts(remote)
        val localParts = versionParts(local)
        if (remoteParts.isEmpty() || localParts.isEmpty()) return 0
        val count = maxOf(remoteParts.size, localParts.size)
        repeat(count) { index ->
            val left = remoteParts.getOrElse(index) { 0 }
            val right = localParts.getOrElse(index) { 0 }
            if (left != right) return left.compareTo(right)
        }
        return 0
    }

    private fun versionParts(value: String): List<Int> =
        value.trim().removePrefix("v").removePrefix("V")
            .substringBefore('-')
            .split('.')
            .mapNotNull { it.toIntOrNull() }

    private fun apkAssetPriority(name: String): Int {
        val lower = name.lowercase()
        return when {
            "universal" in lower -> 5
            "release" in lower -> 4
            "karoha" in lower -> 3
            "debug" in lower -> 1
            else -> 2
        }
    }

    private fun openConnection(url: String): HttpURLConnection =
        (URL(url).openConnection() as HttpURLConnection).apply {
            instanceFollowRedirects = true
            connectTimeout = 15_000
            readTimeout = 45_000
            setRequestProperty("User-Agent", USER_AGENT)
        }

    private fun sha256(file: File): String {
        val digest = MessageDigest.getInstance("SHA-256")
        file.inputStream().use { input ->
            val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
            while (true) {
                val count = input.read(buffer)
                if (count < 0) break
                digest.update(buffer, 0, count)
            }
        }
        return digest.digest().joinToString("") { "%02x".format(it) }
    }
}
