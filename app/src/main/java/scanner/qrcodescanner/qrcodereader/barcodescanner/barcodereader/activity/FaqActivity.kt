package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.R
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.databinding.ActivityFaqBinding
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.setGradientInStatusBar
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.setLocale
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.sharedPref

class FaqActivity : AppCompatActivity() {

    lateinit var binding:ActivityFaqBinding

    var prevVisible=-1

    var can=false
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
        binding= ActivityFaqBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setGradientInStatusBar(this)
        binding.backButton.setOnClickListener{
            onBackPressed()
        }
        binding.first.setOnClickListener{
            if(binding.firstText.visibility==View.VISIBLE){
                binding.firstText.visibility=View.GONE
                binding.firstDivider.visibility=View.GONE

                binding.secondText.visibility=View.GONE
                binding.secondDivider.visibility=View.GONE

                binding.thirdText.visibility=View.GONE
                binding.thirdDivider.visibility=View.GONE

                binding.fourthText.visibility=View.GONE
                binding.fourthDivider.visibility=View.GONE
            }else{
                binding.firstText.visibility=View.VISIBLE
                binding.firstDivider.visibility=View.VISIBLE

                binding.secondText.visibility=View.GONE
                binding.secondDivider.visibility=View.GONE

                binding.thirdText.visibility=View.GONE
                binding.thirdDivider.visibility=View.GONE

                binding.fourthText.visibility=View.GONE
                binding.fourthDivider.visibility=View.GONE
            }
        }
        binding.second.setOnClickListener{
            if(binding.secondText.visibility==View.VISIBLE){
                binding.firstText.visibility=View.GONE
                binding.firstDivider.visibility=View.GONE

                binding.secondText.visibility=View.GONE
                binding.secondDivider.visibility=View.GONE

                binding.thirdText.visibility=View.GONE
                binding.thirdDivider.visibility=View.GONE

                binding.fourthText.visibility=View.GONE
                binding.fourthDivider.visibility=View.GONE
            }else{
                binding.firstText.visibility=View.GONE
                binding.firstDivider.visibility=View.GONE

                binding.secondText.visibility=View.VISIBLE
                binding.secondDivider.visibility=View.VISIBLE

                binding.thirdText.visibility=View.GONE
                binding.thirdDivider.visibility=View.GONE

                binding.fourthText.visibility=View.GONE
                binding.fourthDivider.visibility=View.GONE
            }
        }
        binding.third.setOnClickListener{
            if(binding.thirdText.visibility==View.VISIBLE){
                binding.firstText.visibility=View.GONE
                binding.firstDivider.visibility=View.GONE

                binding.secondText.visibility=View.GONE
                binding.secondDivider.visibility=View.GONE

                binding.thirdText.visibility=View.GONE
                binding.thirdDivider.visibility=View.GONE

                binding.fourthText.visibility=View.GONE
                binding.fourthDivider.visibility=View.GONE
            }else{
                binding.firstText.visibility=View.GONE
                binding.firstDivider.visibility=View.GONE

                binding.secondText.visibility=View.GONE
                binding.secondDivider.visibility=View.GONE

                binding.thirdText.visibility=View.VISIBLE
                binding.thirdDivider.visibility=View.VISIBLE

                binding.fourthText.visibility=View.GONE
                binding.fourthDivider.visibility=View.GONE
            }
        }
        binding.fourth.setOnClickListener{
            if(binding.fourthText.visibility==View.VISIBLE){
                binding.firstText.visibility=View.GONE
                binding.firstDivider.visibility=View.GONE

                binding.secondText.visibility=View.GONE
                binding.secondDivider.visibility=View.GONE

                binding.thirdText.visibility=View.GONE
                binding.thirdDivider.visibility=View.GONE

                binding.fourthText.visibility=View.GONE
                binding.fourthDivider.visibility=View.GONE
            }else{
                binding.firstText.visibility=View.GONE
                binding.firstDivider.visibility=View.GONE

                binding.secondText.visibility=View.GONE
                binding.secondDivider.visibility=View.GONE

                binding.thirdText.visibility=View.GONE
                binding.thirdDivider.visibility=View.GONE

                binding.fourthText.visibility=View.VISIBLE
                binding.fourthDivider.visibility=View.VISIBLE
            }
        }


//        binding.first.setOnClickListener{
//            if(prevVisible!=-1){
//
//                can=true
//                when(prevVisible){
//                    0->{
//                        binding.firstDivider.visibility=View.GONE
//                        binding.firstText.visibility=View.GONE
//                    }
//                    1->{
//                        binding.secondDivider.visibility=View.GONE
//                        binding.secondText.visibility=View.GONE
//                    }
//                    2->{
//                        binding.thirdDivider.visibility=View.GONE
//                        binding.thirdText.visibility=View.GONE
//                    }
//                    3->{
//                        binding.fourthDivider.visibility=View.GONE
//                        binding.fourthText.visibility=View.GONE
//                    }
//                }
//            }
//            if(binding.firstText.visibility==View.VISIBLE){
//
//                prevVisible=-1
//                binding.firstText.visibility=View.GONE
//                binding.firstDivider.visibility=View.GONE
//            }else{
//
//                prevVisible=0
//                binding.firstText.visibility=View.VISIBLE
//                binding.firstDivider.visibility=View.VISIBLE
//            }
//
//
//
//        }
//        binding.second.setOnClickListener{
//            if(prevVisible!=-1){
//                when(prevVisible){
//                    0->{
//                        binding.firstDivider.visibility=View.GONE
//                        binding.firstText.visibility=View.GONE
//                    }
//                    1->{
//                        binding.secondDivider.visibility=View.GONE
//                        binding.secondText.visibility=View.GONE
//                    }
//                    2->{
//                        binding.thirdDivider.visibility=View.GONE
//                        binding.thirdText.visibility=View.GONE
//                    }
//                    3->{
//                        binding.fourthDivider.visibility=View.GONE
//                        binding.fourthText.visibility=View.GONE
//                    }
//                }
//            }
//            if(binding.secondText.visibility==View.VISIBLE){
//
//                prevVisible=-1
//                binding.secondText.visibility=View.GONE
//                binding.secondDivider.visibility=View.GONE
//            }else{
//prevVisible=1
//                binding.secondText.visibility=View.VISIBLE
//                binding.secondDivider.visibility=View.VISIBLE
//            }
//
//
//
//
//
//
//        }
//        binding.third.setOnClickListener{
//            if(prevVisible!=-1){
//                when(prevVisible){
//                    0->{
//                        binding.firstDivider.visibility=View.GONE
//                        binding.firstText.visibility=View.GONE
//                    }
//                    1->{
//                        binding.secondDivider.visibility=View.GONE
//                        binding.secondText.visibility=View.GONE
//                    }
//                    2->{
//                        binding.thirdDivider.visibility=View.GONE
//                        binding.thirdText.visibility=View.GONE
//                    }
//                    3->{
//                        binding.fourthDivider.visibility=View.GONE
//                        binding.fourthText.visibility=View.GONE
//                    }
//                }
//            }
//            if(binding.thirdText.visibility==View.VISIBLE){
//
//                prevVisible=-1
//                binding.thirdText.visibility=View.GONE
//                binding.thirdDivider.visibility=View.GONE
//            }else{
//                prevVisible=2
//                binding.thirdText.visibility=View.VISIBLE
//                binding.thirdDivider.visibility=View.VISIBLE
//            }
//
//
//
//
//        }
//        binding.fourth.setOnClickListener{
//            if(prevVisible!=-1){
//                when(prevVisible){
//                    0->{
//                        binding.firstDivider.visibility=View.GONE
//                        binding.firstText.visibility=View.GONE
//                    }
//                    1->{
//                        binding.secondDivider.visibility=View.GONE
//                        binding.secondText.visibility=View.GONE
//                    }
//                    2->{
//                        binding.thirdDivider.visibility=View.GONE
//                        binding.thirdText.visibility=View.GONE
//                    }
//                    3->{
//                        binding.fourthDivider.visibility=View.GONE
//                        binding.fourthText.visibility=View.GONE
//                    }
//                }
//            }
//            if(binding.fourthText.visibility==View.VISIBLE){
//
//                prevVisible=-1
//                binding.fourthText.visibility=View.GONE
//                binding.fourthDivider.visibility=View.GONE
//            }else{
//                prevVisible=3
//                binding.fourthText.visibility=View.VISIBLE
//                binding.fourthDivider.visibility=View.VISIBLE
//            }
//
//
//
//
//        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
