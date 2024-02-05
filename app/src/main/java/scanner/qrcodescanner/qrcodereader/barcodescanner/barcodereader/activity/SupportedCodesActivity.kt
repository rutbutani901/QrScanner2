package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.R
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.adapter.SupportedCodesAdapter
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.databinding.ActivitySupportedCodesBinding
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.setGradientInStatusBar
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.setLocale
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.sharedPref
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.modelClass.SupportedCodesModelClass

class SupportedCodesActivity : AppCompatActivity() {

    lateinit var binding:ActivitySupportedCodesBinding

    var codeList= arrayListOf(
        SupportedCodesModelClass("QR", R.drawable.qr,true),
        SupportedCodesModelClass("Model 1 QR", R.drawable.model_one,false),
        SupportedCodesModelClass("Micro QR", R.drawable.micro_qr,false),
        SupportedCodesModelClass("EAN 13/JAN", R.drawable.ean_thirteen,true),
        SupportedCodesModelClass("EAN-8", R.drawable.ean_eight,true),
        SupportedCodesModelClass("EAN-5", R.drawable.ean_five,false),
        SupportedCodesModelClass("Code 25 interl. (itf)", R.drawable.code_twenty_five_inter,true),
        SupportedCodesModelClass("Code 25 Industrial", R.drawable.code_industrial,false),
        SupportedCodesModelClass("UPC-A", R.drawable.upca_img,true),//upca
        SupportedCodesModelClass("UPC-E", R.drawable.upce_img,true),//upce
        SupportedCodesModelClass("Codabar", R.drawable.code_twenty_five_inter,true),//upce
        SupportedCodesModelClass("Code 39", R.drawable.code_twenty_five_inter,true),//upce
        SupportedCodesModelClass("Code 93", R.drawable.code_twenty_five_inter,true),//upce
        SupportedCodesModelClass("Code 128", R.drawable.code_twenty_five_inter,true),//upce
        SupportedCodesModelClass("RSS-14/GS1 Databar", R.drawable.code_twenty_five_inter,true),//upce
        SupportedCodesModelClass("Aztec", R.drawable.aztec_img,true),//upce
        SupportedCodesModelClass("Data Matrix", R.drawable.qr,true),//upce
        SupportedCodesModelClass("PDF417", R.drawable.pdf_img,true),//upce
        SupportedCodesModelClass("Micro Pdf417", R.drawable.pdf_img,false),//upce
    )

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
        binding= ActivitySupportedCodesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setGradientInStatusBar(this)

        binding.backButton.setOnClickListener{
            onBackPressed()
        }

        setAdapter(codeList)
    }

    lateinit var supprtedAdapter: SupportedCodesAdapter

    fun setAdapter(suportedCodeList:ArrayList<SupportedCodesModelClass>) {//SupportedCodesModelClass

        supprtedAdapter= SupportedCodesAdapter(this, suportedCodeList)//SupportedCodesModelClass
        binding.recycler.setHasFixedSize(true)
        binding.recycler.setItemViewCacheSize(suportedCodeList.size)
        binding.recycler.layoutManager=  GridLayoutManager(this,2, GridLayoutManager.VERTICAL,false)
        binding.recycler.adapter=supprtedAdapter

    }

}