package social.karotter.client

import android.app.Application
import org.conscrypt.Conscrypt
import java.security.Security

class KarotterApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Android 8 などの古い端末でも、証明書検証を維持したまま最新のTLSを利用する。
        if (Security.getProvider("Conscrypt") == null) {
            Security.insertProviderAt(Conscrypt.newProvider(), 1)
        }
    }
}
