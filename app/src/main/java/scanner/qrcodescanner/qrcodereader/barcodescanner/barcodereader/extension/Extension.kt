package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.net.wifi.WifiManager
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.R
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.util.AppSharedPrefrence
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

fun Activity.setLocale(lanCode:String) {

    val locale= Locale(lanCode)
    Locale.setDefault(locale)
    val config=this.resources.configuration
    config.setLocale(locale)
    resources.updateConfiguration(config,this.resources.displayMetrics)

}

val Context.sharedPref: AppSharedPrefrence get() = AppSharedPrefrence.newInstance(applicationContext)

fun BottomNavigationView.uncheckAllItems() {
    menu.setGroupCheckable(0, true, false)
    for (i in 0 until menu.size()) {
        menu.getItem(i).isChecked = false
    }
    menu.setGroupCheckable(0, true, true)
}

val Context.wifiManager: WifiManager?
    get() = applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager

fun Context.setGradientInStatusBar(activity: Activity){
    val window: Window = activity.window
    val background = ContextCompat.getDrawable(this, R.drawable.only_gradient_green)
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

    window.statusBarColor = ContextCompat.getColor(this,android.R.color.transparent)
//    window.navigationBarColor = ContextCompat.getColor(this,android.R.color.transparent)
    window.setBackgroundDrawable(background)
}

fun Context.setBlueGradientInStatusBar(activity: Activity){
    val window: Window = activity.window
    val background = ContextCompat.getDrawable(this, R.drawable.gradient_light_blue_pro)
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

    window.statusBarColor = ContextCompat.getColor(this,android.R.color.transparent)
//    window.navigationBarColor = ContextCompat.getColor(this,android.R.color.transparent)
    window.setBackgroundDrawable(background)
}

fun Context.rate(){
    val url="https://play.google.com/store/apps/details?id=${packageName}"
    val intent= Intent(Intent.ACTION_VIEW)
    intent.flags= Intent.FLAG_ACTIVITY_NEW_TASK
    intent.data= Uri.parse(url)
    startActivity(intent)
}
fun Context.feedBack(){

    val intent= Intent(Intent.ACTION_SEND)
    var receiptant=arrayOf(resources.getString(R.string.feedBackEmail))
    intent.putExtra(Intent.EXTRA_EMAIL,receiptant)
    intent.putExtra(Intent.EXTRA_SUBJECT,"FeedBack")
    intent.putExtra(Intent.EXTRA_CC,resources.getString(R.string.feedBackEmail))
    intent.type="text/html"
    intent.setPackage("com.google.android.gm")
    startActivity(Intent.createChooser(intent,"Send Mail"))
}
