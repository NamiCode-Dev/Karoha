package social.karotter.client

import android.Manifest
import android.app.Notification
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.os.SystemClock
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import org.json.JSONObject
import social.karotter.client.data.ApiDmGroup
import social.karotter.client.data.ApiNotification
import social.karotter.client.data.ApiResult
import social.karotter.client.data.KarotterApi
import java.security.MessageDigest
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

private const val NOTIFICATION_STATE_PREFS = "karoha_background_notification_state_v1"
private const val EVENT_CHANNEL_ID = "karoha_social_events"

private fun sharedNotificationKey(item: ApiNotification): String =
    "${item.id}:${item.type}:${item.actorName}:${item.createdAt}"

private fun sharedDmKey(item: ApiDmGroup): String =
    "${item.id}:${item.lastMessageAt}:${item.lastMessage}:${item.unreadCount}:${item.isRequest}"

private fun notificationStateKey(account: String): String {
    val digest = MessageDigest.getInstance("SHA-256")
        .digest(account.trim().lowercase().toByteArray())
        .take(8)
        .joinToString("") { "%02x".format(it) }
    return "last_$digest"
}

private fun dmStateKey(account: String): String =
    notificationStateKey(account).replaceFirst("last_", "last_dm_")

private fun dmUnreadSnapshot(groups: List<ApiDmGroup>): Map<Long, String> =
    groups.asSequence()
        .filter { it.unreadCount > 0 || it.isRequest }
        .associate { it.id to sharedDmKey(it) }

private fun encodeDmSnapshot(snapshot: Map<Long, String>): String = JSONObject().apply {
    snapshot.forEach { (groupId, key) -> put(groupId.toString(), key) }
}.toString()

private fun decodeDmSnapshot(raw: String?): Map<Long, String> {
    if (raw.isNullOrBlank()) return emptyMap()
    return runCatching {
        val json = JSONObject(raw)
        buildMap {
            json.keys().forEach { groupId ->
                groupId.toLongOrNull()?.let { put(it, json.optString(groupId)) }
            }
        }
    }.getOrDefault(emptyMap())
}

object AppVisibility {
    @Volatile var isForeground: Boolean = false
}

object BackgroundNotificationManager {
    private const val PREFS = "karoha_background_notifications_v1"
    private const val ENABLED = "enabled"
    private const val AUTH_PAUSED = "auth_paused"
    private const val RESTART_REQUEST_CODE = 64115

    fun isEnabled(context: Context): Boolean =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getBoolean(ENABLED, false)

    fun isAuthPaused(context: Context): Boolean =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getBoolean(AUTH_PAUSED, false)

    fun setEnabled(context: Context, enabled: Boolean) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
            .putBoolean(ENABLED, enabled)
            .apply()
        if (!enabled) {
            cancelScheduledRestart(context)
            context.stopService(Intent(context, BackgroundNotificationService::class.java))
        }
    }

    fun onLoginSucceeded(context: Context) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
            .putBoolean(AUTH_PAUSED, false)
            .apply()
    }

    fun onLoggedOut(context: Context) {
        context.stopService(Intent(context, BackgroundNotificationService::class.java))
    }

    fun markForegroundNotificationShown(
        context: Context,
        account: String?,
        notification: ApiNotification
    ) {
        if (account.isNullOrBlank()) return
        context.getSharedPreferences(NOTIFICATION_STATE_PREFS, Context.MODE_PRIVATE)
            .edit()
            .putString(notificationStateKey(account), sharedNotificationKey(notification))
            .apply()
    }

    fun markForegroundDmSnapshot(context: Context, account: String?, groups: List<ApiDmGroup>) {
        if (account.isNullOrBlank()) return
        context.getSharedPreferences(NOTIFICATION_STATE_PREFS, Context.MODE_PRIVATE)
            .edit()
            .putString(dmStateKey(account), encodeDmSnapshot(dmUnreadSnapshot(groups)))
            .apply()
    }

    fun clearEventNotifications(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return
        val manager = context.getSystemService(NotificationManager::class.java)
        manager.activeNotifications
            .filter { it.notification.channelId == EVENT_CHANNEL_ID }
            .forEach { manager.cancel(it.id) }
    }

    internal fun pauseForAuthentication(context: Context) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
            .putBoolean(AUTH_PAUSED, true)
            .apply()
        cancelScheduledRestart(context)
    }

    internal fun scheduleRestart(context: Context, delayMillis: Long = 30_000L) {
        if (!isEnabled(context) || isAuthPaused(context) || AppVisibility.isForeground) return
        val alarm = context.getSystemService(AlarmManager::class.java)
        alarm.setAndAllowWhileIdle(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + delayMillis,
            restartPendingIntent(context)
        )
    }

    internal fun cancelScheduledRestart(context: Context) {
        context.getSystemService(AlarmManager::class.java).cancel(restartPendingIntent(context))
    }

    private fun restartPendingIntent(context: Context): PendingIntent = PendingIntent.getBroadcast(
        context,
        RESTART_REQUEST_CODE,
        Intent(context, BackgroundNotificationRestartReceiver::class.java)
            .setAction("social.karotter.client.RESTART_BACKGROUND_NOTIFICATIONS"),
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    fun startIfNeeded(context: Context, action: String? = null) {
        if (!isEnabled(context) || isAuthPaused(context) || AppVisibility.isForeground) return
        runCatching {
            ContextCompat.startForegroundService(
                context,
                Intent(context, BackgroundNotificationService::class.java).setAction(action)
            )
        }
    }
}

class BackgroundNotificationService : Service() {
    private val pollerLock = Any()
    @Volatile private var executor: ScheduledExecutorService? = null
    @Volatile private var lastPollFinishedAt = SystemClock.elapsedRealtime()
    private lateinit var api: KarotterApi
    private val state by lazy {
        getSharedPreferences(NOTIFICATION_STATE_PREFS, Context.MODE_PRIVATE)
    }

    override fun onCreate() {
        super.onCreate()
        createChannels()
        api = KarotterApi(applicationContext)
        val monitor = NotificationCompat.Builder(this, CHANNEL_MONITOR)
            .setSmallIcon(android.R.drawable.stat_notify_sync_noanim)
            .setContentTitle("Karoha")
            .setContentText("バックグラウンドで通知を確認しています")
            .setContentIntent(openAppIntent())
            .setOngoing(true)
            .setSilent(true)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(MONITOR_NOTIFICATION_ID, monitor, ServiceInfo.FOREGROUND_SERVICE_TYPE_REMOTE_MESSAGING)
        } else {
            startForeground(MONITOR_NOTIFICATION_ID, monitor)
        }
        startPoller()
        BackgroundNotificationManager.scheduleRestart(this, WATCHDOG_INTERVAL_MILLIS)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!BackgroundNotificationManager.isEnabled(this) ||
            BackgroundNotificationManager.isAuthPaused(this) ||
            AppVisibility.isForeground
        ) {
            stopSelf()
            return START_NOT_STICKY
        }
        if (intent?.action == ACTION_WATCHDOG &&
            SystemClock.elapsedRealtime() - lastPollFinishedAt >= STALE_POLLER_MILLIS
        ) {
            startPoller(force = true)
        }
        BackgroundNotificationManager.scheduleRestart(this, WATCHDOG_INTERVAL_MILLIS)
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onTaskRemoved(rootIntent: Intent?) {
        BackgroundNotificationManager.scheduleRestart(this)
        super.onTaskRemoved(rootIntent)
    }

    override fun onDestroy() {
        synchronized(pollerLock) {
            executor?.shutdownNow()
            executor = null
        }
        BackgroundNotificationManager.scheduleRestart(this)
        super.onDestroy()
    }

    private fun startPoller(force: Boolean = false) {
        synchronized(pollerLock) {
            if (force) {
                executor?.shutdownNow()
                executor = null
            }
            if (executor?.isShutdown == false && executor?.isTerminated == false) return
            lastPollFinishedAt = SystemClock.elapsedRealtime()
            executor = Executors.newSingleThreadScheduledExecutor().also { scheduler ->
                scheduler.scheduleWithFixedDelay(::pollSafely, 0L, 15L, TimeUnit.SECONDS)
            }
        }
    }

    private fun pollSafely() {
        try {
            runCatching { poll() }
        } finally {
            lastPollFinishedAt = SystemClock.elapsedRealtime()
        }
    }

    private fun poll() {
        if (AppVisibility.isForeground ||
            !BackgroundNotificationManager.isEnabled(this) ||
            BackgroundNotificationManager.isAuthPaused(this)
        ) {
            stopSelf()
            return
        }
        if (!api.hasNetworkConnection()) return
        val activeIdentifier = api.activeAccountIdentifier()
        if (activeIdentifier.isNullOrBlank()) {
            authenticationFailed("保存済みのログイン情報がありません")
            return
        }

        val result = api.notificationPage(1, 20)
        when (result) {
            is ApiResult.Success -> deliverNew(
                activeIdentifier,
                result.value.filter {
                    !it.suppressed && !it.type.equals("DM", ignoreCase = true)
                }
            )
            is ApiResult.Failure -> {
                // KarotterApi already performs refresh -> recorded-session resume
                // -> one final login for authentication failures. Temporary server
                // and transport failures must not create another login session.
                if (result.status == 401) {
                    authenticationFailed(result.message)
                    return
                }
            }
        }
        when (val dmResult = api.dmGroups()) {
            is ApiResult.Success -> deliverNewDm(activeIdentifier, dmResult.value)
            is ApiResult.Failure -> if (dmResult.status == 401) authenticationFailed(dmResult.message)
        }
    }

    private fun deliverNew(account: String, notifications: List<ApiNotification>) {
        if (notifications.isEmpty()) return
        val newestKey = sharedNotificationKey(notifications.first())
        val stateKey = notificationStateKey(account)
        val previousKey = state.getString(stateKey, null)
        state.edit().putString(stateKey, newestKey).apply()
        if (previousKey == null || AppVisibility.isForeground || !canPostNotifications()) return

        val additions = notifications.takeWhile { sharedNotificationKey(it) != previousKey }.take(5)
        additions.asReversed().forEach { notification ->
            val notificationId = EVENT_NOTIFICATION_BASE + sharedNotificationKey(notification).hashCode().and(0x0FFFFFFF)
            val systemNotification = NotificationCompat.Builder(this, EVENT_CHANNEL_ID)
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setContentTitle(notification.actorName.ifBlank { "Karoha" })
                .setContentText(notification.message)
                .setStyle(NotificationCompat.BigTextStyle().bigText(notification.message))
                .setContentIntent(openAppIntent(notification.post?.id, notification.actorUsername, systemNotificationId = notificationId))
                .setAutoCancel(true)
                .setCategory(NotificationCompat.CATEGORY_SOCIAL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()
            NotificationManagerCompat.from(this).notify(notificationId, systemNotification)
        }
    }

    private fun deliverNewDm(account: String, groups: List<ApiDmGroup>) {
        val snapshot = dmUnreadSnapshot(groups)
        val stateKey = dmStateKey(account)
        val previousRaw = state.getString(stateKey, null)
        val previous = decodeDmSnapshot(previousRaw)
        state.edit().putString(stateKey, encodeDmSnapshot(snapshot)).apply()
        if (previousRaw == null || AppVisibility.isForeground || !canPostNotifications()) return

        groups.asSequence()
            .filter { it.unreadCount > 0 || it.isRequest }
            .filter { previous[it.id] != sharedDmKey(it) }
            .take(5)
            .forEach { group ->
                val notificationId = DM_NOTIFICATION_BASE + group.id.hashCode()
                val title = group.name.ifBlank {
                    group.members.joinToString("、") { it.displayName.ifBlank { it.username } }
                        .ifBlank { "新しいメッセージ" }
                }
                val message = group.lastMessage.ifBlank {
                    if (group.isRequest) "メッセージリクエストが届きました" else "画像が送信されました"
                }
                val systemNotification = NotificationCompat.Builder(this, EVENT_CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.stat_notify_chat)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                    .setContentIntent(openAppIntent(dmGroupId = group.id, systemNotificationId = notificationId))
                    .setAutoCancel(true)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .build()
                NotificationManagerCompat.from(this).notify(notificationId, systemNotification)
            }
    }

    private fun authenticationFailed(message: String) {
        if (!BackgroundNotificationManager.isAuthPaused(this) && canPostNotifications()) {
            val notification = NotificationCompat.Builder(this, CHANNEL_ERRORS)
                .setSmallIcon(android.R.drawable.stat_notify_error)
                .setContentTitle("Karohaへの再ログインが必要です")
                .setContentText("通知を取得できませんでした。アプリを開いてログインしてください。")
                .setStyle(
                    NotificationCompat.BigTextStyle().bigText(
                        "通知を取得できませんでした。アプリを開いてログインしてください。\n$message"
                    )
                )
                .setContentIntent(openAppIntent())
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()
            NotificationManagerCompat.from(this).notify(AUTH_ERROR_NOTIFICATION_ID, notification)
        }
        BackgroundNotificationManager.pauseForAuthentication(this)
        stopSelf()
    }

    private fun canPostNotifications(): Boolean =
        NotificationManagerCompat.from(this).areNotificationsEnabled() &&
            (Build.VERSION.SDK_INT < 33 ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED)

    private fun openAppIntent(
        postId: Long? = null,
        username: String? = null,
        dmGroupId: Long? = null,
        systemNotificationId: Int? = null
    ): PendingIntent {
        val intent = Intent(this, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            .apply {
                postId?.let { putExtra(MainActivity.EXTRA_NOTIFICATION_POST_ID, it) }
                username?.let { putExtra(MainActivity.EXTRA_NOTIFICATION_USERNAME, it) }
                dmGroupId?.let { putExtra(MainActivity.EXTRA_NOTIFICATION_DM_GROUP_ID, it) }
                systemNotificationId?.let { putExtra(MainActivity.EXTRA_SYSTEM_NOTIFICATION_ID, it) }
            }
        return PendingIntent.getActivity(
            this,
            systemNotificationId ?: postId?.hashCode() ?: username?.hashCode() ?: dmGroupId?.hashCode() ?: 0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun createChannels() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(
            NotificationChannel(CHANNEL_MONITOR, "バックグラウンド確認", NotificationManager.IMPORTANCE_LOW).apply {
                description = "Karohaが15秒ごとに新着通知を確認するための動作表示"
                setShowBadge(false)
            }
        )
        manager.createNotificationChannel(
            NotificationChannel(EVENT_CHANNEL_ID, "新着通知", NotificationManager.IMPORTANCE_HIGH).apply {
                description = "いいね、返信、フォローなどの新着通知"
            }
        )
        manager.createNotificationChannel(
            NotificationChannel(CHANNEL_ERRORS, "ログインエラー", NotificationManager.IMPORTANCE_HIGH).apply {
                description = "バックグラウンド通知の再ログインに失敗した場合の通知"
            }
        )
    }

    companion object {
        internal const val ACTION_WATCHDOG = "social.karotter.client.BACKGROUND_NOTIFICATION_WATCHDOG"
        private const val WATCHDOG_INTERVAL_MILLIS = 90_000L
        private const val STALE_POLLER_MILLIS = 180_000L
        private const val CHANNEL_MONITOR = "karoha_background_monitor"
        private const val CHANNEL_ERRORS = "karoha_auth_errors"
        private const val MONITOR_NOTIFICATION_ID = 61001
        private const val AUTH_ERROR_NOTIFICATION_ID = 61002
        private const val EVENT_NOTIFICATION_BASE = 62000
        private const val DM_NOTIFICATION_BASE = 63000
    }
}

class BackgroundNotificationRestartReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (!BackgroundNotificationManager.isEnabled(context) ||
            BackgroundNotificationManager.isAuthPaused(context)
        ) return
        // Keep a later fallback armed until the foreground service actually starts.
        BackgroundNotificationManager.scheduleRestart(context, 5 * 60_000L)
        BackgroundNotificationManager.startIfNeeded(context, BackgroundNotificationService.ACTION_WATCHDOG)
    }
}
