package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.fragments

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.NetworkChangeReceiver
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.R
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.activity.AboutUsActivity
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.activity.InAppPurchaseActivity
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.activity.LanguageActivity
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.activity.SearchOptionActivity
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.databinding.DialogCameraOrientationBinding
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.databinding.FragmentSettingBinding
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.feedBack
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.rate
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.sharedPref
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.util.Constants
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.util.InternetChecker
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class SettingFragment : Fragment() {


    interface SettingInterface {

        fun changeTheme(theme:Int)
    }

    lateinit var settingInterface: SettingInterface

    lateinit var binding:FragmentSettingBinding
    lateinit var fragmentActivity: FragmentActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        settingInterface=activity as SettingInterface

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

        binding= FragmentSettingBinding.inflate(layoutInflater)

        if(fragmentActivity.sharedPref.isPremiumPurchased){

            binding.proLayout.visibility=View.GONE
            binding.continousScanningPremiumIcon.visibility=View.GONE
            binding.duplicateBarcodesPremiumIcon.visibility=View.GONE
            binding.confirmScansManuallyPremiumIcon.visibility=View.GONE
        }else{

            binding.proLayout.visibility=View.VISIBLE
            binding.continousScanningPremiumIcon.visibility=View.VISIBLE
            binding.duplicateBarcodesPremiumIcon.visibility=View.VISIBLE
            binding.confirmScansManuallyPremiumIcon.visibility=View.VISIBLE
        }

        setClickListners()

        val connectionLiveData = NetworkChangeReceiver(fragmentActivity)
        connectionLiveData.observe(fragmentActivity, Observer { isConnected ->
            isConnected?.let {
                if(isConnected){
                    binding.checkInternetLayout.visibility=View.GONE
                    binding.contentInScroll.visibility=View.VISIBLE


                }else{
                    binding.contentInScroll.visibility=View.GONE
                    binding.checkInternetLayout.visibility=View.VISIBLE
                }
            }
        })

        return binding.root
    }

    fun checkContinousScan(){
        if(this::binding.isInitialized){

            binding.continousScanningSwitch.isChecked= fragmentActivity.sharedPref.isContinousScanningEnabled
        }

    }

    val newStart = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {

            fragmentActivity.finish()
        }
    }

    var networkCheckerLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result->
        Constants.openOtherScreenFromApp=false
        isNetworkAvailable()
    }
    fun isNetworkAvailable(){
        if(this::binding.isInitialized){
            if (InternetChecker.isConnectedToInternet(fragmentActivity) || InternetChecker.isConnectedToWifi(fragmentActivity)) {

                binding.checkInternetLayout.visibility=View.GONE
                binding.contentInScroll.visibility=View.VISIBLE

            } else {
                binding.checkInternetLayout.visibility=View.VISIBLE

            }
        }
    }


    override fun onDestroy() {
        Constants.openOtherScreenFromApp=false
        super.onDestroy()
    }

    fun setClickListners(){
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
        
        binding.proLayout.setOnClickListener{
            startActivity(Intent(fragmentActivity, InAppPurchaseActivity::class.java))
        }
        var selectedLang="English"
        when(fragmentActivity.sharedPref.languageCode){
            "en"->{
                selectedLang="English"
            }
            "es"->{
                selectedLang="Español"
            }
            "fr"->{
                selectedLang="Français"
            }
            "de"->{
                selectedLang="Deutsch"
            }
            "it"->{
                selectedLang="Italiano"
            }
            "pt"->{
                selectedLang="Português"
            }
            "ko"->{
                selectedLang="한국인"
            }
        }
        binding.selectedLang.text=selectedLang
        binding.language.setOnClickListener{
            val intent=Intent(fragmentActivity, LanguageActivity::class.java)
            intent.putExtra("cameFromSetting",true)
            newStart.launch(intent)
        }
        binding.theme.setOnClickListener{

            openThemeSelector()
        }
        val openAutomatically=fragmentActivity.sharedPref.openWebsiteAutomatically
        binding.openWebsiteAutomaticallySwitch.isChecked= openAutomatically
        binding.openWebsiteAutomatically.setOnClickListener{

            val openAuto=fragmentActivity.sharedPref.openWebsiteAutomatically
            binding.openWebsiteAutomaticallySwitch.isChecked=!openAuto
            fragmentActivity.sharedPref.openWebsiteAutomatically=!openAuto
        }

        val continousScan=fragmentActivity.sharedPref.isContinousScanningEnabled
        binding.continousScanningSwitch.isChecked= continousScan
        binding.continuousScanning.setOnClickListener{

            if(fragmentActivity.sharedPref.isPremiumPurchased){

                val continousScan=fragmentActivity.sharedPref.isContinousScanningEnabled

                binding.continousScanningSwitch.isChecked=!continousScan
                fragmentActivity.sharedPref.isContinousScanningEnabled=!continousScan

            }else{

                binding.continousScanningSwitch.isChecked=false
                fragmentActivity.sharedPref.isContinousScanningEnabled=false
                startActivity(Intent(fragmentActivity, InAppPurchaseActivity::class.java))

            }
        }

        val duplicateBarcodes=fragmentActivity.sharedPref.isDuplicateBarcodeEnable
        binding.duplicateBarcodesSwitch.isChecked= duplicateBarcodes
        binding.duplicateBarcodes.setOnClickListener{
            val isPremiumPurchased=fragmentActivity.sharedPref.isPremiumPurchased

            if(isPremiumPurchased){

                val isduplicateBarocdeEnable=fragmentActivity.sharedPref.isDuplicateBarcodeEnable

                binding.duplicateBarcodesSwitch.isChecked=!isduplicateBarocdeEnable
                fragmentActivity.sharedPref.isDuplicateBarcodeEnable=!isduplicateBarocdeEnable

            }else{
                binding.duplicateBarcodesSwitch.isChecked=true
                fragmentActivity.sharedPref.isDuplicateBarcodeEnable=true
                startActivity(Intent(fragmentActivity, InAppPurchaseActivity::class.java))
                //start premiums screen
            }
        }

        val scanManually= fragmentActivity.sharedPref.confirmScanManually
        binding.confirmScansManuallySwitch.isChecked=scanManually
        binding.confirmScansManually.setOnClickListener{
            val isPremiumPurchased=fragmentActivity.sharedPref.isPremiumPurchased

            if(isPremiumPurchased){
                val isConfirmScanMauallyEnable= fragmentActivity.sharedPref.confirmScanManually

                binding.confirmScansManuallySwitch.isChecked=!isConfirmScanMauallyEnable
                fragmentActivity.sharedPref.confirmScanManually=!isConfirmScanMauallyEnable
            }else{
                binding.confirmScansManuallySwitch.isChecked=false
                fragmentActivity.sharedPref.confirmScanManually=false
                startActivity(Intent(fragmentActivity, InAppPurchaseActivity::class.java))

            }
        }

        val playSound= fragmentActivity.sharedPref.playSound
        binding.playSoundSwitch.isChecked=playSound
        binding.playSound.setOnClickListener{
            val sound=fragmentActivity.sharedPref.playSound

            binding.playSoundSwitch.isChecked=!sound
            fragmentActivity.sharedPref.playSound=!sound
        }

        val vibrateOnScan= fragmentActivity.sharedPref.vibrateOnScan
        binding.vibrateSwitch.isChecked=vibrateOnScan
        binding.vibrate.setOnClickListener{
            val vibrate=fragmentActivity.sharedPref.vibrateOnScan

            binding.vibrateSwitch.isChecked=!vibrate
            fragmentActivity.sharedPref.vibrateOnScan=!vibrate
        }

        val copyToClipboard= fragmentActivity.sharedPref.copyToClipboard
        binding.copyToClipboardSwitch.isChecked=copyToClipboard
        binding.copyToClipboard.setOnClickListener{

            val copyClipBoard=fragmentActivity.sharedPref.copyToClipboard

            binding.copyToClipboardSwitch.isChecked=!copyClipBoard
            fragmentActivity.sharedPref.copyToClipboard=!copyClipBoard

        }

        val orientation=fragmentActivity.sharedPref.cameraOrientationBack
        if(orientation) binding.cameraOrientationMessage.text=getString(R.string.backCameraRecomd)
        else             binding.cameraOrientationMessage.text=getString(R.string.frontCamera)
        binding.cameraOrientation.setOnClickListener{
            showCameraOrientationDialog()
        }
        binding.searchOptions.setOnClickListener{

            startActivity(Intent(fragmentActivity, SearchOptionActivity::class.java))
        }
        binding.rate.setOnClickListener{
            Constants.openOtherScreenFromApp=true
            fragmentActivity.sharedPref.isAppRated=true
            fragmentActivity.rate()
        }
        binding.feedBack.setOnClickListener{
            Constants.openOtherScreenFromApp=true
fragmentActivity.feedBack()
        }
        binding.about.setOnClickListener{

            startActivity(Intent(fragmentActivity, AboutUsActivity::class.java))
        }
    }

    override fun onResume() {
        Constants.openOtherScreenFromApp=false
        super.onResume()
    }


    fun openThemeSelector(){
        val bottomSheet = ThemeSelectorBottomSheet()
        val bundle = Bundle()
        bundle.putInt("selectedTheme", fragmentActivity.sharedPref.appTheme)
        bottomSheet.arguments = bundle

        bottomSheet.show(childFragmentManager, "ModalBottomSheet")

        childFragmentManager.setFragmentResultListener("requestKey",viewLifecycleOwner) { key, bundle ->
            val selectedTheme = bundle.getString("bundleKey")
            selectedTheme?.let {

                fragmentActivity.sharedPref.appTheme=selectedTheme.toInt()

                settingInterface.changeTheme(selectedTheme.toInt())
            }
        }
    }



    lateinit var cameraOrientationBinding: DialogCameraOrientationBinding
    fun showCameraOrientationDialog(){

        cameraOrientationBinding= DialogCameraOrientationBinding.inflate(layoutInflater)

        val typedValue = TypedValue()
        val theme: Resources.Theme = fragmentActivity.getTheme()
        theme.resolveAttribute(R.attr.greenAppColor, typedValue, true)
        @ColorInt val greenAppColor: Int = typedValue.data

        val orientation=fragmentActivity.sharedPref.cameraOrientationBack

        if(orientation) {
            cameraOrientationBinding.frontCameraRadioButton.setImageResource(R.drawable.ic_off_radio)
            cameraOrientationBinding.frontCameraRadioButton.imageTintList=ContextCompat.getColorStateList(fragmentActivity,R.color.unselectedLangOffRadioIconColor)
            cameraOrientationBinding.backCameraRadioButton.setImageResource(R.drawable.ic_on_radio)
            when(fragmentActivity.sharedPref.appTheme){
                0->{
                     cameraOrientationBinding.backCameraRadioButton.imageTintList= ContextCompat.getColorStateList(fragmentActivity,R.color.greenAppColor)


                }
                1->{

                     cameraOrientationBinding.backCameraRadioButton.imageTintList= ContextCompat.getColorStateList(fragmentActivity,R.color.blueThemeColor)

                }
                2->{

                     cameraOrientationBinding.backCameraRadioButton.imageTintList= ContextCompat.getColorStateList(fragmentActivity,R.color.purpleThemeColor)

                }
                3->{

                     cameraOrientationBinding.backCameraRadioButton.imageTintList= ContextCompat.getColorStateList(fragmentActivity,R.color.yellowThemeColor)

                }
                4->{

                     cameraOrientationBinding.backCameraRadioButton.imageTintList= ContextCompat.getColorStateList(fragmentActivity,R.color.pinkThemeColor)

                }
            }


        }else{
            cameraOrientationBinding.frontCameraRadioButton.setImageResource(R.drawable.ic_on_radio)
            when(fragmentActivity.sharedPref.appTheme){
                0->{
                    cameraOrientationBinding.frontCameraRadioButton.imageTintList= ContextCompat.getColorStateList(fragmentActivity,R.color.greenAppColor)



                }
                1->{

                    cameraOrientationBinding.frontCameraRadioButton.imageTintList= ContextCompat.getColorStateList(fragmentActivity,R.color.blueThemeColor)


                }
                2->{

                    cameraOrientationBinding.frontCameraRadioButton.imageTintList= ContextCompat.getColorStateList(fragmentActivity,R.color.purpleThemeColor)


                }
                3->{

                    cameraOrientationBinding.frontCameraRadioButton.imageTintList= ContextCompat.getColorStateList(fragmentActivity,R.color.yellowThemeColor)


                }
                4->{

                    cameraOrientationBinding.frontCameraRadioButton.imageTintList= ContextCompat.getColorStateList(fragmentActivity,R.color.pinkThemeColor)


                }
            }

            cameraOrientationBinding.backCameraRadioButton.setImageResource(R.drawable.ic_off_radio)
            cameraOrientationBinding.backCameraRadioButton.imageTintList=ContextCompat.getColorStateList(fragmentActivity,R.color.unselectedLangOffRadioIconColor)
        }



        var dialog= MaterialAlertDialogBuilder(fragmentActivity,R.style.ThemeOverlay_App_MaterialAlertDialog)
            .setView(cameraOrientationBinding.root)
            .setOnCancelListener(object : DialogInterface.OnCancelListener{
                override fun onCancel(dialog: DialogInterface?) {

                    //DIALOG CANCEL
                }

            })
            .show()


        dialog.window?.setBackgroundDrawable(resources.getDrawable(R.drawable.shape_rounded_corners))
        dialog.setCanceledOnTouchOutside(false)
        cameraOrientationBinding.cancel.setOnClickListener{

            dialog.cancel()
        }
        cameraOrientationBinding.backCamera.setOnClickListener{

            binding.cameraOrientationMessage.text=getString(R.string.backCameraRecomd)
           fragmentActivity.sharedPref.cameraOrientationBack=true
            cameraOrientationBinding.backCameraRadioButton.setImageResource(R.drawable.ic_on_radio)
            cameraOrientationBinding.frontCameraRadioButton.setImageResource(R.drawable.ic_off_radio)
            dialog.cancel()
        }
        cameraOrientationBinding.frontCamera.setOnClickListener{

            binding.cameraOrientationMessage.text=getString(R.string.frontCamera)
           fragmentActivity.sharedPref.cameraOrientationBack=false
            cameraOrientationBinding.backCameraRadioButton.setImageResource(R.drawable.ic_off_radio)
            cameraOrientationBinding.frontCameraRadioButton.setImageResource(R.drawable.ic_on_radio)
            dialog.cancel()
        }




    }

}