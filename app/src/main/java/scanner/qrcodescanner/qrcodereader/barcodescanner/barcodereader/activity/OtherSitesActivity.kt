package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.activity

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.GeolocationPermissions
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.R
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.databinding.ActivityOtherSitesBinding
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.setLocale
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.sharedPref


class OtherSitesActivity : AppCompatActivity() {

    lateinit var binding:ActivityOtherSitesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        setLocale(sharedPref.languageCode)
        super.onCreate(savedInstanceState)
        when(sharedPref.appTheme){
            0->{
//                application.setTheme(R.style.GreenTheme)
                setTheme(R.style.GreenTheme)
            }
            1->{
//                application.setTheme(R.style.BlueTheme)
                setTheme(R.style.BlueTheme)
            }
            2->{
//                application.setTheme(R.style.PurpleTheme)
                setTheme(R.style.PurpleTheme)
            }
            3->{
//                application.setTheme(R.style.YellowTheme)
                setTheme(R.style.YellowTheme)
            }
            4->{
//                application.setTheme(R.style.PinkTheme)
                setTheme(R.style.PinkTheme)
            }
        }
        binding= ActivityOtherSitesBinding.inflate(layoutInflater)
        setContentView(binding.root)

       val link= intent.getStringExtra("link").toString()

        loadWebPage(link)
//        binding.webview.settings.javaScriptEnabled=true
//        binding.webview.setWebViewClient(MyWebViewClient())
//        binding.webview.loadUrl(link)

        binding.backButton.setOnClickListener{
            onBackPressed()
        }
    }

    fun loadWebPage(url:String){
        binding.webview.webViewClient=MyWebViewClient()
        binding.webview.settings.allowContentAccess=true
        binding.webview.settings.useWideViewPort=true
        binding.webview.settings.domStorageEnabled=true
        binding.webview.settings.loadWithOverviewMode=true
        binding.webview.settings.allowFileAccess=true
        binding.webview.settings.javaScriptEnabled=true
        binding.webview.setLayerType(View.LAYER_TYPE_SOFTWARE,null)
        binding.webview.loadUrl(url)

        binding.webview.webChromeClient=object : WebChromeClient(){
            override fun onGeolocationPermissionsShowPrompt(
                origin: String,
                callback: GeolocationPermissions.Callback
            ) {
                callback.invoke(origin,true,false)
            }
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                binding.progress.progress=newProgress
            }
        }
    }

    inner class MyWebViewClient:WebViewClient(){
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            binding.progress.visibility= View.VISIBLE
        }

        override fun onPageCommitVisible(view: WebView, url: String?) {
            super.onPageCommitVisible(view, url)
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            binding.progress.visibility= View.GONE
        }
    }


//    private class MyWebViewClient : WebViewClient() {
//        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
//            // Load clicked links within the WebView
//            view.loadUrl(url)
//            return true
//        }
//    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}