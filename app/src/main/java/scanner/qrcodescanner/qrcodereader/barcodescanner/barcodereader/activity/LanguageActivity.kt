package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.R
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.adapter.LanguageAdapter
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.ads.AdEventListener
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.ads.AdHandler
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.databinding.ActivityLanguageBinding
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.setLocale
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.sharedPref
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.modelClass.LanguageModelClass
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.viewModel.LanguageViewModel
import com.google.android.gms.ads.nativead.NativeAd

class LanguageActivity : AppCompatActivity() {

    lateinit var binding:ActivityLanguageBinding
    var tempLangCode=""
    lateinit var viewModel: LanguageViewModel
    var cameFromSettings = false

    override fun onCreate(savedInstanceState: Bundle?) {
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
        binding= ActivityLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(LanguageViewModel::class.java)

        val languageList = viewModel.getLanList()

        cameFromSettings = intent.getBooleanExtra("cameFromSetting", false)
        if (cameFromSettings) {
            loadNewNativeAd()

            binding.back.visibility = View.VISIBLE

            val lanCode = sharedPref.languageCode
            tempLangCode=lanCode

            languageList.find { it.isSelected }?.isSelected = false
            languageList.find { it.lanCode.equals(lanCode) }?.isSelected = true

        } else {
            loadNativeAd()
            binding.back.visibility = View.GONE
        }
        binding.back.setOnClickListener {
            onBackPressed()
        }


        setAdapter(languageList)
        binding.tick.setOnClickListener {

            sharedPref.languageCode=lanSelec
            setResult(RESULT_OK)
            if (sharedPref.languageCode.isEmpty()) {
                sharedPref.languageCode = "en"
            }
            setLocale(sharedPref.languageCode)
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            if (cameFromSettings) intent.putExtra("fromSplash", false)
            else intent.putExtra("fromSplash", true)
            startActivity(intent)
            finish()
        }

    }
    lateinit var languageAdapter: LanguageAdapter

    var lanSelec=""
    fun setAdapter(languageList: ArrayList<LanguageModelClass>) {

        languageAdapter = LanguageAdapter(this, languageList) { lanCode ->
            lanSelec= lanCode
        }
        binding.recycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recycler.adapter = languageAdapter

    }
    override fun onBackPressed() {

        sharedPref.languageCode = tempLangCode
        val languageList = viewModel.getLanList()
        languageList.find { it.isSelected }?.isSelected = false
        languageList.find { it.lanCode.equals(tempLangCode) }?.isSelected = true
        super.onBackPressed()

    }

    fun loadNativeAd() {

        if (scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.ads.AdHandler.getInstance(this).isNetworkAvailable(this)) {

            binding.shimmerLayout.root.visibility = View.VISIBLE
            binding.adContainer.visibility = View.GONE

            if (scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.ads.AdHandler.getInstance(this).nativeAd != null) {

                binding.adContainer.visibility = View.VISIBLE
                binding.shimmerLayout.root.visibility = View.GONE
                scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.ads.AdHandler.getInstance(this).showNativeAd(
                    this,
                    binding.adContainer,
                    scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.ads.AdHandler.getInstance(this).nativeAd, true
                )

            } else {
                loadNewNativeAd()
            }
        } else {
            binding.adLinearLayout.visibility = View.GONE
        }
    }


    fun loadNewNativeAd() {

        if (scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.ads.AdHandler.getInstance(this).isNetworkAvailable(this)) {

            binding.adContainer.visibility = View.GONE
            binding.shimmerLayout.root.visibility = View.VISIBLE

            scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.ads.AdHandler.getInstance(this).loadNativeAd(
                this,
                getString(R.string.nativeAdvanced),
                object :
                    scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.ads.AdEventListener {
                    override fun onAdLoaded(adObject: Any?) {
                        if (adObject != null) {

                            binding.adContainer.visibility = View.VISIBLE
                            binding.shimmerLayout.root.visibility = View.GONE

                            scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.ads.AdHandler.getInstance(this@LanguageActivity).showNativeAd(
                                this@LanguageActivity,
                                binding.adContainer,
                                adObject as NativeAd, true
                            )
                        } else {
                            binding.adContainer.visibility = View.GONE
                            binding.shimmerLayout.root.visibility = View.GONE
                        }
                    }

                    override fun onAdClosed() {}
                    override fun onLoadError(errorCode: String?) {

                        binding.adContainer.visibility = View.GONE
                        binding.shimmerLayout.root.visibility = View.GONE
                    }
                })

        } else {

            binding.adLinearLayout.visibility = View.GONE

        }

    }

    override fun onDestroy() {

        super.onDestroy()
    }
}