package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.R
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.databinding.ActivityInAppPurchaseBinding
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.setBlueGradientInStatusBar
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.setLocale
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.sharedPref

class InAppPurchaseActivity : AppCompatActivity() {

    lateinit var binding:ActivityInAppPurchaseBinding

    var selected=1
    var previous=-1
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
        binding= ActivityInAppPurchaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setBlueGradientInStatusBar(this)

//        perfromSelected()
        setClickListners()
    }

    fun setClickListners(){

        binding.cross.setOnClickListener{
            finish()
        }
        binding.continueButton.setOnClickListener{

        }



    }


}
//        binding.restore.setOnClickListener{
//
//            sharedPref.isPremiumPurchased=true
//
//            val intent= Intent(this,HomeActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            intent.putExtra("proPurchased",true)
//            startActivity(intent)
//
////            finish()
//        }
//        binding.continueButton.setOnClickListener{
//
//        }

//        binding.month.setOnClickListener{
//            selected=0
//            perfromSelected()
//        }
//        binding.yearly.setOnClickListener{
//            selected=1
////            perfromSelected()
//        }
//        binding.lifetime.setOnClickListener{
//            selected=2
////            perfromSelected()
//        }
//fun perfromSelected(){
//    if(selected!=previous){
//        when(selected){
//            0->{
//                binding.month.isSelected=true
//                binding.oneMonth.setTextColor(ContextCompat.getColor(this,R.color.white))
//                binding.oneMonthDes.setTextColor(ContextCompat.getColor(this,R.color.white))
//                binding.oneMonthDes2.setTextColor(ContextCompat.getColor(this,R.color.white))
//                binding.oneMonthPrice.setTextColor(ContextCompat.getColor(this,R.color.white))
//            }
//            1->{
//                binding.yearly.isSelected=true
//                binding.oneYearly.setTextColor(ContextCompat.getColor(this,R.color.white))
//                binding.oneYearlyDes.setTextColor(ContextCompat.getColor(this,R.color.white))
//                binding.oneYearlyDes2.setTextColor(ContextCompat.getColor(this,R.color.white))
//                binding.oneYearlyDes3.setTextColor(ContextCompat.getColor(this,R.color.white))
//                binding.oneYearlyPrice.setTextColor(ContextCompat.getColor(this,R.color.white))
//
//            }
//            2->{
//                binding.lifetime.isSelected=true
//                binding.onelifetime.setTextColor(ContextCompat.getColor(this,R.color.white))
//                binding.onelifetimeDes.setTextColor(ContextCompat.getColor(this,R.color.white))
//                binding.onelifetimeDes3.setTextColor(ContextCompat.getColor(this,R.color.white))
//                binding.onelifetimePrice.setTextColor(ContextCompat.getColor(this,R.color.white))
//
//            }
//
//        }
//        if(previous!=-1){
//            when(previous){
//                0->{
//                    binding.month.isSelected=false
//                    binding.oneMonth.setTextColor(ContextCompat.getColor(this,R.color.black))
//                    binding.oneMonthDes.setTextColor(ContextCompat.getColor(this,R.color.black))
//                    binding.oneMonthDes2.setTextColor(ContextCompat.getColor(this,R.color.black))
//                    binding.oneMonthPrice.setTextColor(ContextCompat.getColor(this,R.color.black))
//                }
//                1->{
//                    binding.yearly.isSelected=false
//                    binding.oneYearly.setTextColor(ContextCompat.getColor(this,R.color.black))
//                    binding.oneYearlyDes.setTextColor(ContextCompat.getColor(this,R.color.black))
//                    binding.oneYearlyDes2.setTextColor(ContextCompat.getColor(this,R.color.black))
//                    binding.oneYearlyDes3.setTextColor(ContextCompat.getColor(this,R.color.black))
//                    binding.oneYearlyPrice.setTextColor(ContextCompat.getColor(this,R.color.black))
//
//                }
//                2->{
//                    binding.lifetime.isSelected=false
//                    binding.onelifetime.setTextColor(ContextCompat.getColor(this,R.color.black))
//                    binding.onelifetimeDes.setTextColor(ContextCompat.getColor(this,R.color.black))
//                    binding.onelifetimeDes3.setTextColor(ContextCompat.getColor(this,R.color.black))
//                    binding.onelifetimePrice.setTextColor(ContextCompat.getColor(this,R.color.black))
//
//                }
//            }
//        }
//        previous=selected
//    }
//
//    }