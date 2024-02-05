package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.fragments

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.NetworkChangeReceiver
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.R
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.activity.InAppPurchaseActivity
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.databinding.FragmentCreateBinding
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.sharedPref
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.util.Constants
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.util.InternetChecker
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.viewPager.HomeViewPager


class CreateFragment : Fragment() {


    lateinit var binding:FragmentCreateBinding
    lateinit var fragmentActivity: FragmentActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentActivity=context as FragmentActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(activity!=null) fragmentActivity=activity as FragmentActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding= FragmentCreateBinding.inflate(layoutInflater)

        if(fragmentActivity.sharedPref.isPremiumPurchased){
            binding.pro.visibility=View.GONE
        }else{
            binding.pro.visibility=View.VISIBLE
        }
//        isNetworkAvailable()

        val connectionLiveData = NetworkChangeReceiver(fragmentActivity)
        connectionLiveData.observe(fragmentActivity, Observer { isConnected ->
            isConnected?.let {
                if(isConnected){

                    if( binding.viewPager2.visibility!=View.VISIBLE){

                        binding.checkInternetLayout.visibility=View.GONE
                        binding.viewPager2.visibility=View.VISIBLE

                        createFragments()
                    }

                }else{
                    binding.viewPager2.visibility=View.GONE
                    binding.checkInternetLayout.visibility=View.VISIBLE
                }
            }
        })

        setClickListners()

        return binding.root
    }



    fun setClickListners(){
        binding.pro.setOnClickListener{
            startActivity(Intent(fragmentActivity, InAppPurchaseActivity::class.java))
        }

        binding.tapToRetry.setOnClickListener{
            binding.tapToRetry.text=getString(R.string.loading)
            Handler(Looper.getMainLooper()).postDelayed({
                binding.tapToRetry.text=getString(R.string.tapToRetry)
            },1000)
        }
        binding.networkSetting.setOnClickListener{

            Constants.openOtherScreenFromApp=true
            val intent = Intent("android.settings.SETTINGS")
            networkCheckerLauncher.launch(intent)
        }
    }
    var networkCheckerLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result->
        Constants.openOtherScreenFromApp=false
       isNetworkAvailable()
    }



    override fun onDestroy() {
        super.onDestroy()
        Constants.openOtherScreenFromApp=false
    }



    fun isNetworkAvailable(){
        if(this::binding.isInitialized){
            if (InternetChecker.isConnectedToInternet(fragmentActivity) || InternetChecker.isConnectedToWifi(fragmentActivity)) {

                binding.checkInternetLayout.visibility=View.GONE

                createFragments()

            } else {
                binding.checkInternetLayout.visibility=View.VISIBLE

            }
        }
    }

    var qrCodeFrag= QRCodeInsideCreateFragment()
    var barCodeFrag= BarcodeInsideCreateFragment()

    lateinit var homeViewPager: HomeViewPager
    fun createFragments(){
        val fragList= arrayListOf(
            qrCodeFrag,
            barCodeFrag,
        )

        homeViewPager= HomeViewPager(fragmentActivity,fragList)
        binding.viewPager2.adapter= homeViewPager
        binding.viewPager2.offscreenPageLimit= homeViewPager.itemCount



        val typedValue = TypedValue()
        val theme: Resources.Theme = fragmentActivity.getTheme()
        theme.resolveAttribute(R.attr.greenAppColor, typedValue, true)
        @ColorInt val greenAppColor: Int = typedValue.data

        binding.viewPager2.registerOnPageChangeCallback(
            object :ViewPager2.OnPageChangeCallback(){

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    when(position) {
                        0->{
                            binding.qrCodeTabSelector.visibility=View.VISIBLE
                            binding.barcodeTabSelector.visibility=View.GONE
                            when(fragmentActivity.sharedPref.appTheme){
                                0->{

                                    binding.qrCodeTabText.setTextColor(ContextCompat.getColorStateList(fragmentActivity,R.color.greenAppColor))
                                    binding.qrCodeTabSelector.backgroundTintList=ContextCompat.getColorStateList(fragmentActivity,R.color.greenAppColor)
                                }
                                1->{

                                    binding.qrCodeTabText.setTextColor(ContextCompat.getColorStateList(fragmentActivity,R.color.blueThemeColor))
                                    binding.qrCodeTabSelector.backgroundTintList=ContextCompat.getColorStateList(fragmentActivity,R.color.blueThemeColor)
                                }
                                2->{

                                    binding.qrCodeTabText.setTextColor(ContextCompat.getColorStateList(fragmentActivity,R.color.purpleThemeColor))
                                    binding.qrCodeTabSelector.backgroundTintList=ContextCompat.getColorStateList(fragmentActivity,R.color.purpleThemeColor)
                                }
                                3->{

                                    binding.qrCodeTabText.setTextColor(ContextCompat.getColorStateList(fragmentActivity,R.color.yellowThemeColor))
                                    binding.qrCodeTabSelector.backgroundTintList=ContextCompat.getColorStateList(fragmentActivity,R.color.yellowThemeColor)
                                }
                                4->{

                                    binding.qrCodeTabText.setTextColor(ContextCompat.getColorStateList(fragmentActivity,R.color.pinkThemeColor))
                                    binding.qrCodeTabSelector.backgroundTintList=ContextCompat.getColorStateList(fragmentActivity,R.color.pinkThemeColor)
                                }
                            }

//                            binding.qrCodeTabText.setTextColor(ContextCompat.getColorStateList(fragmentActivity,greenAppColor))
//                            binding.qrCodeTabSelector.backgroundTintList=ContextCompat.getColorStateList(fragmentActivity,greenAppColor)
                            binding.barCodeTabText.setTextColor(ContextCompat.getColorStateList(fragmentActivity,R.color.greyTextColorSplash))

//                            binding.barcodeTabSelector.backgroundTintList=ContextCompat.getColorStateList(fragmentActivity,R.color.greyTextColorSplash)
                        }
                        1->{
                            binding.qrCodeTabSelector.visibility=View.GONE
                            binding.barcodeTabSelector.visibility=View.VISIBLE
                            when(fragmentActivity.sharedPref.appTheme){
                                0->{

                                    binding.barCodeTabText.setTextColor(ContextCompat.getColorStateList(fragmentActivity,R.color.greenAppColor))
                                    binding.barcodeTabSelector.backgroundTintList=ContextCompat.getColorStateList(fragmentActivity,R.color.greenAppColor)
                                }
                                1->{

                                    binding.barCodeTabText.setTextColor(ContextCompat.getColorStateList(fragmentActivity,R.color.blueThemeColor))
                                    binding.barcodeTabSelector.backgroundTintList=ContextCompat.getColorStateList(fragmentActivity,R.color.blueThemeColor)
                                }
                                2->{

                                    binding.barCodeTabText.setTextColor(ContextCompat.getColorStateList(fragmentActivity,R.color.purpleThemeColor))
                                    binding.barcodeTabSelector.backgroundTintList=ContextCompat.getColorStateList(fragmentActivity,R.color.purpleThemeColor)
                                }
                                3->{

                                    binding.barCodeTabText.setTextColor(ContextCompat.getColorStateList(fragmentActivity,R.color.yellowThemeColor))
                                    binding.barcodeTabSelector.backgroundTintList=ContextCompat.getColorStateList(fragmentActivity,R.color.yellowThemeColor)
                                }
                                4->{

                                    binding.barCodeTabText.setTextColor(ContextCompat.getColorStateList(fragmentActivity,R.color.pinkThemeColor))
                                    binding.barcodeTabSelector.backgroundTintList=ContextCompat.getColorStateList(fragmentActivity,R.color.pinkThemeColor)
                                }
                            }

                            binding.qrCodeTabText.setTextColor(ContextCompat.getColorStateList(fragmentActivity,R.color.greyTextColorSplash))
//                            binding.qrCodeTabSelector.backgroundTintList=ContextCompat.getColorStateList(fragmentActivity,R.color.greyTextColorSplash)
//                            binding.barCodeTabText.setTextColor(ContextCompat.getColorStateList(fragmentActivity,greenAppColor))
//                            binding.barcodeTabSelector.backgroundTintList=ContextCompat.getColorStateList(fragmentActivity,greenAppColor)
                        }

                    }
                }
            })

        binding.qrCodeTab.setOnClickListener{
            binding.viewPager2.currentItem=0
        }
        binding.barcodeTab.setOnClickListener{
            binding.viewPager2.currentItem=1
        }
    }



}