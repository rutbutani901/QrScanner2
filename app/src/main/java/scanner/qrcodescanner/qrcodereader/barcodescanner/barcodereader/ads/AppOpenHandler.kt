package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.ads

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.activity.SplashActivity
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.util.AppSharedPrefrence
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.util.Constants
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import java.util.*


class AppOpenHandler(private val applicationClass: Application) :LifecycleEventObserver, ActivityLifecycleCallbacks {

    private lateinit var loadCallbacks: AppOpenAd.AppOpenAdLoadCallback
    private var adShowerActivity:Activity?=null
    var isLoading=false
    var appOpenAd:AppOpenAd?=null


    companion object{
        var isShowingAd=false
    }

    init {
        applicationClass.registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    private fun loadAppOpenAd(appOpenId: String?){

        if(!(AppSharedPrefrence.newInstance(applicationClass).isPremiumPurchased)){
            if(scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.ads.AdHandler.getInstance(
                    applicationClass
                ).isNetworkAvailable(applicationClass)){
                if(!isLoading){
                    if(isAdAvailable){
                        showAdIfAvailable()
                        return
                    }
                }else{
                    return
                }
                isLoading=true
                loadCallbacks=object :AppOpenAd.AppOpenAdLoadCallback(){
                    override fun onAdFailedToLoad(loadError: LoadAdError) {
                        super.onAdFailedToLoad(loadError)
                        isLoading=false

                    }

                    override fun onAdLoaded(appOpenAd: AppOpenAd) {

                        super.onAdLoaded(appOpenAd)
                        this@AppOpenHandler.appOpenAd=appOpenAd
                        isLoading=false

                        if(adShowerActivity !is SplashActivity) showAdIfAvailable()
                    }


                }

               try{
                   val request= AdRequest.Builder().build()
                   AppOpenAd.load(
                       applicationClass,appOpenId!!,request,loadCallbacks
                   )
               }catch (e:Exception){
                   e.printStackTrace()
               }

            }
        }

    }

    private val isAdAvailable:Boolean
    get() = appOpenAd!=null

    fun showAdIfAvailable(){

        if(!isShowingAd){
            if(isAdAvailable){
                val fullScreenContentCallback:FullScreenContentCallback=
                    object : FullScreenContentCallback(){
                        override fun onAdDismissedFullScreenContent() {
                            super.onAdDismissedFullScreenContent()
                            appOpenAd=null
                            isShowingAd = false
                        }

                        override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                            super.onAdFailedToShowFullScreenContent(p0)
                            isShowingAd = false
                        }

                        override fun onAdShowedFullScreenContent() {
                            super.onAdShowedFullScreenContent()
                            isShowingAd =true
                        }
                    }
                appOpenAd!!.fullScreenContentCallback= fullScreenContentCallback
                appOpenAd!!.show(adShowerActivity!!)
                isShowingAd =true

            }else{
                if(Build.VERSION.SDK_INT<= Build.VERSION_CODES.R){
                    applicationClass.startActivity(Intent(applicationClass, SplashActivity::class.java))
//                    loadAppOpenAd(R.string.appOpen.toString())

                }
            }
        }
    }





    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        adShowerActivity=activity
    }

    override fun onActivityResumed(activity: Activity) {
        Log.d("CHECK1","onActivityResumed")
        adShowerActivity=activity
    }
    override fun onActivityStarted(activity: Activity) {
        adShowerActivity=activity
    }

    override fun onActivityPostResumed(activity: Activity) {
        super.onActivityPostResumed(activity)
        adShowerActivity=activity
    }

    override fun onActivityPaused(activity: Activity) {
        adShowerActivity=activity
    }
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }
    override fun onActivityStopped(activity: Activity) {

    }
    override fun onActivityDestroyed(activity: Activity) {

        adShowerActivity?.let {
            if(it.localClassName== activity.localClassName){
                adShowerActivity=null
            }
        }
    }

    override fun onActivityPreSaveInstanceState(activity: Activity, outState: Bundle) {
        super.onActivityPreSaveInstanceState(activity, outState)
    }
    override fun onActivityPreCreated(activity: Activity, savedInstanceState: Bundle?) {
        super.onActivityPreCreated(activity, savedInstanceState)
    }
    override fun onActivityPreDestroyed(activity: Activity) {
        super.onActivityPreDestroyed(activity)
    }


    override fun onActivityPostDestroyed(activity: Activity) {
        super.onActivityPostDestroyed(activity)
    }
    override fun onActivityPostSaveInstanceState(activity: Activity, outState: Bundle) {
        super.onActivityPostSaveInstanceState(activity, outState)
    }


    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if(event==Lifecycle.Event.ON_PAUSE){
            Log.d("CHECK1","PAUSEFIRST")
        }
        if(event==Lifecycle.Event.ON_START){
            Log.d("CHECK1","STARTFIRST")
        }
        when(event){
            Lifecycle.Event.ON_PAUSE->{
                Log.d("CHECK1","PAUSE")
            }
            Lifecycle.Event.ON_DESTROY->{
                Log.d("CHECK1","DESTRIY")
            }
            Lifecycle.Event.ON_START->{
                Log.d("CHECK1","START")
                if (!AppSharedPrefrence.newInstance(applicationClass).isPremiumPurchased) {
                    if(!Constants.isSplashScreen && !Constants.openOtherScreenFromApp){
//                        loadAppOpenAd(R.string.appOpen.toString())
                        val intent=Intent(applicationClass, SplashActivity::class.java)
                        intent.putExtra("adLoader",true)
                        intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK
                        applicationClass.startActivity(intent)
                    }
                }

            }
            else-> {}
        }
    }

}