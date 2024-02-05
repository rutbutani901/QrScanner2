package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.R
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.databinding.ActivityAppIntroBinding
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.setGradientInStatusBar
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.setLocale
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.sharedPref
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.viewPager.AppIntroViewPager

class AppIntroActivity : AppCompatActivity() {

    lateinit var binding:ActivityAppIntroBinding
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
        binding=ActivityAppIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setGradientInStatusBar(this)
        sharedPref.isAppIntroShown=true

        createFragments()
        setClickListners()
    }

    fun setClickListners(){
        binding.skip.setOnClickListener{

            if(sharedPref.languageCode.isEmpty()){
                startActivity(Intent(this, LanguageActivity::class.java))
                finish()
            }else{
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        }
        binding.next.setOnClickListener{

            var position= binding.viewPager2.currentItem
            if(position==3) {

                if(sharedPref.languageCode.isEmpty()){
                    startActivity(Intent(this, LanguageActivity::class.java))
                    finish()
                }else{
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                }
            }
            else  binding.viewPager2.currentItem=++position
        }
    }
    lateinit var appIntroViewPager: AppIntroViewPager
    fun createFragments() {

        appIntroViewPager= AppIntroViewPager(this)
        binding.viewPager2.adapter= appIntroViewPager
        binding.viewPager2.offscreenPageLimit= appIntroViewPager.itemCount

        binding.viewPager2.currentItem=0

        binding.viewPager2.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback(){

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                }
            })

    }

}