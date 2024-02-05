package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.R
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.databinding.ActivityHelpBinding
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.feedBack
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.setGradientInStatusBar
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.setLocale
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.sharedPref

class HelpActivity : AppCompatActivity() {

    lateinit var binding:ActivityHelpBinding
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

        binding=ActivityHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setGradientInStatusBar(this)

        binding.backButton.setOnClickListener{
            onBackPressed()
        }

        binding.supportedCodes.setOnClickListener{
            startActivity(Intent(this, SupportedCodesActivity::class.java))
        }
        binding.tips.setOnClickListener{
            startActivity(Intent(this, ScanningTipsActivity::class.java))
        }
        binding.feedBack.setOnClickListener{
            feedBack()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}