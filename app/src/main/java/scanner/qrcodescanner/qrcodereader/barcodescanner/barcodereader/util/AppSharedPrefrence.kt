package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.util

import android.content.Context

class AppSharedPrefrence(context: Context) {

    val pref= context.getSharedPreferences("MyShardPref",Context.MODE_PRIVATE)


    public  companion object{
        fun newInstance(context: Context)= AppSharedPrefrence(context)
    }

    var languageCode: String
        get()= pref.getString("languageCode","").toString()
        set(languageCode)= pref.edit().putString("languageCode",languageCode).apply()


    var isAppIntroShown: Boolean
        get()= pref.getBoolean("isAppIntroShown",false)
        set(isAppIntroShown)= pref.edit().putBoolean("isAppIntroShown",isAppIntroShown).apply()

    var cameraPermisionPermanetDeniedFirstTime: Boolean
        get()= pref.getBoolean("cameraPermisionPermanetDeniedFirstTime",false)
        set(cameraPermisionPermanetDeniedFirstTime)= pref.edit().putBoolean("cameraPermisionPermanetDeniedFirstTime",cameraPermisionPermanetDeniedFirstTime).apply()

    var imagePickerPermisionPermanetDeniedFirstTime: Boolean
        get()= pref.getBoolean("cameraPermisionPermanetDeniedFirstTime",false)
        set(cameraPermisionPermanetDeniedFirstTime)= pref.edit().putBoolean("cameraPermisionPermanetDeniedFirstTime",cameraPermisionPermanetDeniedFirstTime).apply()

    var cameraOrientationBack:Boolean
        get()= pref.getBoolean("cameraOrientationBack",true)
        set(cameraOrientationBack)= pref.edit().putBoolean("cameraOrientationBack",cameraOrientationBack).apply()

    var playSound:Boolean
        get()= pref.getBoolean("playSound",true)
        set(playSound)= pref.edit().putBoolean("playSound",playSound).apply()

    var vibrateOnScan:Boolean
        get()= pref.getBoolean("vibrateOnScan",true)
        set(vibrateOnScan)= pref.edit().putBoolean("vibrateOnScan",vibrateOnScan).apply()

    var confirmScanManually:Boolean
        get()= pref.getBoolean("confirmScanManually",false)
        set(confirmScanManually)= pref.edit().putBoolean("confirmScanManually",confirmScanManually).apply()

    var openWebsiteAutomatically:Boolean
        get()= pref.getBoolean("openWebsiteAutomatically",false)
        set(openWebsiteAutomatically)= pref.edit().putBoolean("openWebsiteAutomatically",openWebsiteAutomatically).apply()

    var isContinousScanningEnabled:Boolean
        get()= pref.getBoolean("isContinousScanningEnabled",false)
        set(isContinousScanningEnabled)= pref.edit().putBoolean("isContinousScanningEnabled",isContinousScanningEnabled).apply()

    var isDuplicateBarcodeEnable:Boolean
        get()= pref.getBoolean("isDuplicateBarcodeEnable",true)
        set(isDuplicateBarcodeEnable)= pref.edit().putBoolean("isDuplicateBarcodeEnable",isDuplicateBarcodeEnable).apply()

    var isPremiumPurchased:Boolean
        get()= pref.getBoolean("isPremiumPurchased",false)
        set(isPremiumPurchased)= pref.edit().putBoolean("isPremiumPurchased",isPremiumPurchased).apply()

    //0 for green
    //1 for blue
    //2 for purple
    //3 for yellow
    //4 for pink
    var appTheme: Int
        get()= pref.getInt("appTheme",0)
        set(appTheme)= pref.edit().putInt("appTheme",appTheme).apply()

    var copyToClipboard:Boolean
        get()= pref.getBoolean("copyToClipboard",true)
        set(copyToClipboard)= pref.edit().putBoolean("copyToClipboard",copyToClipboard).apply()

    var isAppRated: Boolean
        get()= pref.getBoolean("isAppRated",false)
        set(isAppRated)= pref.edit().putBoolean("isAppRated",isAppRated).apply()

    var countryCodeSearchOption: String
        get()= pref.getString("countryCodeSearchOption","").toString()
        set(countryCodeSearchOption)= pref.edit().putString("countryCodeSearchOption",countryCodeSearchOption).apply()

    var displayProductInfoAuto:Boolean
        get()= pref.getBoolean("displayProductInfoAuto",false)
        set(displayProductInfoAuto)= pref.edit().putBoolean("displayProductInfoAuto",displayProductInfoAuto).apply()



}