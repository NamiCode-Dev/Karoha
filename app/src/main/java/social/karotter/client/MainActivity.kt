package social.karotter.client

import android.os.Bundle
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import social.karotter.client.ui.KarotterApp

data class NotificationNavigationTarget(
    val postId: Long? = null,
    val username: String? = null,
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
        stopService(Intent(this, BackgroundNotificationService::class.java))
    }

    override fun onPause() {
        AppVisibility.isForeground = false
        BackgroundNotificationManager.startIfNeeded(this)
        super.onPause()
    }

    private fun readNotificationTarget(intent: Intent?) {
        val postId = intent?.getLongExtra(EXTRA_NOTIFICATION_POST_ID, -1L)?.takeIf { it > 0L }
        val username = intent?.getStringExtra(EXTRA_NOTIFICATION_USERNAME)?.takeIf { it.isNotBlank() }
        if (postId != null || username != null) {
            notificationTarget = NotificationNavigationTarget(postId, username)
            intent?.removeExtra(EXTRA_NOTIFICATION_POST_ID)
            intent?.removeExtra(EXTRA_NOTIFICATION_USERNAME)
        }
    }

    companion object {
        const val EXTRA_NOTIFICATION_POST_ID = "karoha.notification.POST_ID"
        const val EXTRA_NOTIFICATION_USERNAME = "karoha.notification.USERNAME"
    }
}
