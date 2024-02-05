package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.R
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.adapter.ScanningTipsAdapter
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.databinding.ActivityScanningTipsBinding
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.setGradientInStatusBar
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.setLocale
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.sharedPref
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.modelClass.SupportedCodesModelClass

class ScanningTipsActivity : AppCompatActivity() {

    lateinit var binding:ActivityScanningTipsBinding

    var codeList = ArrayList<SupportedCodesModelClass>()
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
        binding= ActivityScanningTipsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setGradientInStatusBar(this)
        getList()


        binding.backButton.setOnClickListener {
            onBackPressed()
        }


        setAdapter(codeList)
    }

    fun getList() {
        codeList.add(
            SupportedCodesModelClass(
                getString(R.string.orientation90),
                R.drawable.orientation_ninety,
                true
            )
        )
        codeList.add(
            SupportedCodesModelClass(
                getString(R.string.orientation0),
                R.drawable.orientation_zero,
                true
            )
        )
        codeList.add(
            SupportedCodesModelClass(
                getString(R.string.otherOrientation),
                R.drawable.orientaion_other,
                true
            )
        )
        codeList.add(
            SupportedCodesModelClass(
                getString(R.string.lightOrShadow),
                R.drawable.light_or_shadow_img,
                false
            )
        )
        codeList.add(
            SupportedCodesModelClass(
                getString(R.string.tooClose),
                R.drawable.blurry,
                false
            )
        )
        codeList.add(
            SupportedCodesModelClass(
                getString(R.string.ledWhenDark),
                R.drawable.led_when_dark_img,
                true
            )
        )
        codeList.add(
            SupportedCodesModelClass(
                getString(R.string.lowContrast),
                R.drawable.low_constrast_img,
                false
            )
        )

    }

    lateinit var tipsAdapter: ScanningTipsAdapter
    fun setAdapter(suportedCodeList: ArrayList<SupportedCodesModelClass>) {

        tipsAdapter = ScanningTipsAdapter(this, suportedCodeList)
        binding.recycler.setHasFixedSize(true)
        binding.recycler.setItemViewCacheSize(suportedCodeList.size)
        binding.recycler.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        binding.recycler.adapter = tipsAdapter

    }
}