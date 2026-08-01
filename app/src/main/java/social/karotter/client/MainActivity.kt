package social.karotter.client

import android.os.Bundle
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.app.NotificationManagerCompat
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import social.karotter.client.ui.KarotterApp

data class NotificationNavigationTarget(
    val postId: Long? = null,
    val username: String? = null,
    val dmGroupId: Long? = null,
    val nonce: Long = System.nanoTime()
)

class MainActivity : ComponentActivity() {
    private var notificationTarget by mutableStateOf<NotificationNavigationTarget?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        readNotificationTarget(intent)
        enableEdgeToEdge()
        setContent {
            KarotterApp(
                notificationTarget = notificationTarget,
                onNotificationTargetConsumed = { notificationTarget = null }
            )
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        readNotificationTarget(intent)
    }

    override fun onResume() {
        super.onResume()
        AppVisibility.isForeground = true
        // Do not stop a service that may have just been launched with
        // startForegroundService(). Its onCreate() must get a chance to call
        // startForeground(), otherwise Android terminates the app. The service
        // observes AppVisibility and stops itself without polling.
    }

    override fun onPause() {
        AppVisibility.isForeground = false
        BackgroundNotificationManager.startIfNeeded(this)
        super.onPause()
    }

    private fun readNotificationTarget(intent: Intent?) {
        val postId = intent?.getLongExtra(EXTRA_NOTIFICATION_POST_ID, -1L)?.takeIf { it > 0L }
        val username = intent?.getStringExtra(EXTRA_NOTIFICATION_USERNAME)?.takeIf { it.isNotBlank() }
        val dmGroupId = intent?.getLongExtra(EXTRA_NOTIFICATION_DM_GROUP_ID, -1L)?.takeIf { it > 0L }
        val systemNotificationId = intent?.getIntExtra(EXTRA_SYSTEM_NOTIFICATION_ID, -1)?.takeIf { it >= 0 }
        systemNotificationId?.let { NotificationManagerCompat.from(this).cancel(it) }
        if (postId != null || username != null || dmGroupId != null) {
            notificationTarget = NotificationNavigationTarget(postId, username, dmGroupId)
            intent?.removeExtra(EXTRA_NOTIFICATION_POST_ID)
            intent?.removeExtra(EXTRA_NOTIFICATION_USERNAME)
            intent?.removeExtra(EXTRA_NOTIFICATION_DM_GROUP_ID)
        }
        intent?.removeExtra(EXTRA_SYSTEM_NOTIFICATION_ID)
    }

    companion object {
        const val EXTRA_NOTIFICATION_POST_ID = "karoha.notification.POST_ID"
        const val EXTRA_NOTIFICATION_USERNAME = "karoha.notification.USERNAME"
        const val EXTRA_NOTIFICATION_DM_GROUP_ID = "karoha.notification.DM_GROUP_ID"
        const val EXTRA_SYSTEM_NOTIFICATION_ID = "karoha.notification.SYSTEM_NOTIFICATION_ID"
    }
}
