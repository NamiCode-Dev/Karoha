package social.karotter.client.data

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

data class SavedCredentialAccount(
    val identifier: String,
    val displayName: String,
    val username: String,
    val avatarUrl: String?
)

/** Credentials are encrypted with a non-exportable key held by Android Keystore. */
class CredentialVault(context: Context) {
    private val prefs = context.getSharedPreferences("karotter_credential_vault_v1", Context.MODE_PRIVATE)
    private val alias = "karotter_login_aes_v1"
    private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }

    private fun key(): SecretKey {
        (keyStore.getKey(alias, null) as? SecretKey)?.let { return it }
        return KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore").apply {
            init(
                KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .setRandomizedEncryptionRequired(true)
                    .build()
            )
        }.generateKey()
    }

    private fun accountKey(identifier: String): String = Base64.encodeToString(
        MessageDigest.getInstance("SHA-256").digest(identifier.trim().lowercase().toByteArray(Charsets.UTF_8)),
        Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING
    )

    private fun accountKeys(): List<String> = prefs.getString("accounts", "").orEmpty()
        .split(',')
        .filter { it.isNotBlank() }

    private fun decrypt(storedKey: String): Pair<String, String>? = try {
        val iv = Base64.decode(prefs.getString("iv_$storedKey", null) ?: return null, Base64.NO_WRAP)
        val encrypted = Base64.decode(prefs.getString("data_$storedKey", null) ?: return null, Base64.NO_WRAP)
        val cipher = Cipher.getInstance("AES/GCM/NoPadding").apply {
            init(Cipher.DECRYPT_MODE, key(), GCMParameterSpec(128, iv))
        }
        val plainBytes = cipher.doFinal(encrypted)
        val parts = plainBytes.toString(Charsets.UTF_8).split('\u0000', limit = 2)
        plainBytes.fill(0)
        if (parts.size == 2) parts[0] to parts[1] else null
    } catch (_: Exception) {
        null
    }

    fun save(identifier: String, password: String) {
        val storedKey = accountKey(identifier)
        val plain = "$identifier\u0000$password".toByteArray(Charsets.UTF_8)
        val cipher = Cipher.getInstance("AES/GCM/NoPadding").apply { init(Cipher.ENCRYPT_MODE, key()) }
        val encrypted = cipher.doFinal(plain)
        val accounts = (listOf(storedKey) + accountKeys().filterNot { it == storedKey }).distinct()
        prefs.edit()
            .putString("iv_$storedKey", Base64.encodeToString(cipher.iv, Base64.NO_WRAP))
            .putString("data_$storedKey", Base64.encodeToString(encrypted, Base64.NO_WRAP))
            .putString("accounts", accounts.joinToString(","))
            .putString("active", storedKey)
            .remove("iv")
            .remove("data")
            .apply()
        plain.fill(0)
    }

    private fun loadLegacy(): Pair<String, String>? = try {
        val iv = Base64.decode(prefs.getString("iv", null) ?: return null, Base64.NO_WRAP)
        val encrypted = Base64.decode(prefs.getString("data", null) ?: return null, Base64.NO_WRAP)
        val cipher = Cipher.getInstance("AES/GCM/NoPadding").apply {
            init(Cipher.DECRYPT_MODE, key(), GCMParameterSpec(128, iv))
        }
        val plainBytes = cipher.doFinal(encrypted)
        val parts = plainBytes.toString(Charsets.UTF_8).split('\u0000', limit = 2)
        plainBytes.fill(0)
        if (parts.size == 2) parts[0] to parts[1] else null
    } catch (_: Exception) {
        null
    }

    fun load(): Pair<String, String>? {
        val active = prefs.getString("active", null)
        if (active != null) decrypt(active)?.let { return it }
        accountKeys().firstOrNull()?.let { storedKey ->
            prefs.edit().putString("active", storedKey).apply()
            decrypt(storedKey)?.let { return it }
        }
        return loadLegacy()?.also { (identifier, password) -> save(identifier, password) }
    }

    fun identifiers(): List<String> {
        if (accountKeys().isEmpty()) load()
        return accountKeys().mapNotNull { decrypt(it)?.first }
    }

    fun saveProfileForActive(displayName: String, username: String, avatarUrl: String?) {
        val active = prefs.getString("active", null) ?: return
        prefs.edit()
            .putString("display_$active", displayName)
            .putString("username_$active", username)
            .apply {
                if (avatarUrl.isNullOrBlank()) remove("avatar_$active") else putString("avatar_$active", avatarUrl)
            }
            .apply()
    }

    fun saveSessionIdForActive(sessionId: String?) {
        val active = prefs.getString("active", null) ?: return
        prefs.edit().apply {
            if (sessionId.isNullOrBlank()) remove("session_$active")
            else putString("session_$active", sessionId)
        }.apply()
    }

    fun sessionIdForActive(): String? {
        val active = prefs.getString("active", null) ?: return null
        return prefs.getString("session_$active", null)?.takeIf { it.isNotBlank() }
    }

    fun sessionIdFor(identifier: String): String? =
        prefs.getString("session_${accountKey(identifier)}", null)?.takeIf { it.isNotBlank() }

    fun savedSessionIds(): List<String> =
        accountKeys().mapNotNull { storedKey ->
            prefs.getString("session_$storedKey", null)?.takeIf { it.isNotBlank() }
        }.distinct()

    fun accounts(): List<SavedCredentialAccount> {
        if (accountKeys().isEmpty()) load()
        return accountKeys().mapNotNull { storedKey ->
            val identifier = decrypt(storedKey)?.first ?: return@mapNotNull null
            SavedCredentialAccount(
                identifier,
                prefs.getString("display_$storedKey", null).orEmpty().ifBlank { identifier },
                prefs.getString("username_$storedKey", null).orEmpty(),
                prefs.getString("avatar_$storedKey", null)
            )
        }
    }

    fun select(identifier: String): Pair<String, String>? {
        val storedKey = accountKey(identifier)
        if (storedKey !in accountKeys()) return null
        prefs.edit().putString("active", storedKey).apply()
        return decrypt(storedKey)
    }

    fun clear() {
        val active = prefs.getString("active", null)
        if (active == null) {
            prefs.edit().remove("iv").remove("data").apply()
            return
        }
        val remaining = accountKeys().filterNot { it == active }
        val editor = prefs.edit()
            .remove("iv_$active")
            .remove("data_$active")
            .remove("display_$active")
            .remove("username_$active")
            .remove("avatar_$active")
            .remove("session_$active")
            .putString("accounts", remaining.joinToString(","))
        if (remaining.isEmpty()) editor.remove("active") else editor.putString("active", remaining.first())
        editor.apply()
    }
}
