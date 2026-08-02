package social.karotter.client

import android.app.Application
import android.os.Build
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import org.conscrypt.Conscrypt
import java.security.Security

class KarotterApplication : Application(), ImageLoaderFactory {
    override fun onCreate() {
        super.onCreate()
        // Android 8 などの古い端末でも、証明書検証を維持したまま最新のTLSを利用する。
        if (Security.getProvider("Conscrypt") == null) {
            Security.insertProviderAt(Conscrypt.newProvider(), 1)
        }
    }

    override fun newImageLoader(): ImageLoader = ImageLoader.Builder(this)
        .components {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .crossfade(false)
        .build()
}
