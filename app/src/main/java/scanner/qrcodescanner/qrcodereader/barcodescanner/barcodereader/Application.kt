package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.ads.AppOpenHandler


class   Application: Application() ,LifecycleObserver{

    private var appOpenHandler: AppOpenHandler?=null

    override fun onCreate() {
        super.onCreate()
        appOpenHandler = AppOpenHandler(this)

//        val yandexConfig= YandexMetricaConfig.newConfigBuilder(
//            resources.getString(R.string.app_metrica_id)).build()
//        YandexMetrica.activate(applicationContext,yandexConfig)
//        YandexMetrica.enableActivityAutoTracking(this)

    }

}