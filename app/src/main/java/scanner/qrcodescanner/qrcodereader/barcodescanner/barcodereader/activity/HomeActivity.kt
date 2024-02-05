package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.ColorInt
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.model.KeyPath
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.R
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.databinding.ActivityHomeBinding
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.modelClass.ScanNowEvent
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.util.Constants
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.viewModel.HomeViewModel
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.viewPager.HomeViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.DexterError
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.willy.ratingbar.BaseRatingBar
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.*
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.fragments.*
import kotlin.math.roundToInt


class HomeActivity : AppCompatActivity(), SettingFragment.SettingInterface {

    lateinit var viewModel: HomeViewModel

    lateinit var binding:ActivityHomeBinding

    var shoppingFragment= ShoppingFragment()
    var createFragment= CreateFragment()
    var scanFragment= ScanFragment()
    var historyFragment= HistoryFragment()
    var settingFragment= SettingFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        setLocale(sharedPref.languageCode)
        super.onCreate(savedInstanceState)

        binding= ActivityHomeBinding.inflate(layoutInflater)
        when(sharedPref.appTheme){
            0->{

                setTheme(R.style.GreenTheme)
                binding.lotie.setAnimationTint(ContextCompat.getColor(this,R.color.greenAppColor))
                binding.scanIcon.background=ContextCompat.getDrawable(this,R.drawable.gradient_green)
                binding.bottomNavigationView.itemIconTintList=ContextCompat.getColorStateList(this,R.color.home_botom_navigation_item_selector)
                binding.bottomNavigationView.itemTextColor=ContextCompat.getColorStateList(this,R.color.home_botom_navigation_item_selector)

            }
            1->{

                setTheme(R.style.BlueTheme)
                binding.lotie.setAnimationTint(ContextCompat.getColor(this,R.color.blueThemeColor))
                binding.scanIcon.background=ContextCompat.getDrawable(this,R.drawable.gradient_green)
                binding.bottomNavigationView.itemIconTintList=ContextCompat.getColorStateList(this,R.color.home_botom_navigation_item_selector)
                binding.bottomNavigationView.itemTextColor=ContextCompat.getColorStateList(this,R.color.home_botom_navigation_item_selector)
            }
            2->{

                setTheme(R.style.PurpleTheme)
                binding.lotie.setAnimationTint(ContextCompat.getColor(this,R.color.purpleThemeColor))
                binding.scanIcon.background=ContextCompat.getDrawable(this,R.drawable.gradient_green)
                binding.bottomNavigationView.itemIconTintList=ContextCompat.getColorStateList(this,R.color.home_botom_navigation_item_selector)
                binding.bottomNavigationView.itemTextColor=ContextCompat.getColorStateList(this,R.color.home_botom_navigation_item_selector)

            }
            3->{

                setTheme(R.style.YellowTheme)
                binding.lotie.setAnimationTint(ContextCompat.getColor(this,R.color.yellowThemeColor))
                binding.scanIcon.background=ContextCompat.getDrawable(this,R.drawable.gradient_green)
                binding.bottomNavigationView.itemIconTintList=ContextCompat.getColorStateList(this,R.color.home_botom_navigation_item_selector)
                binding.bottomNavigationView.itemTextColor=ContextCompat.getColorStateList(this,R.color.home_botom_navigation_item_selector)

            }
            4->{

                setTheme(R.style.PinkTheme)
                binding.lotie.setAnimationTint(ContextCompat.getColor(this,R.color.pinkThemeColor))
                binding.scanIcon.background=ContextCompat.getDrawable(this,R.drawable.gradient_green)
                binding.bottomNavigationView.itemIconTintList=ContextCompat.getColorStateList(this,R.color.home_botom_navigation_item_selector)
                binding.bottomNavigationView.itemTextColor=ContextCompat.getColorStateList(this,R.color.home_botom_navigation_item_selector)

            }
        }
        setContentView(binding.root)

        Constants.isSplashScreen=false

        setGradientInStatusBar(this)

       if(intent.getBooleanExtra("proPurchased",false)){


       }


       binding.bottomNavigationView.uncheckAllItems()
        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.menu.getItem(2).isEnabled = false

        viewModel=ViewModelProvider(this).get(HomeViewModel::class.java)
        EventBus.getDefault().register(this)




        createFragments()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: ScanNowEvent) {

        if(event.isScan){
            historyFragment.disableDelete()
            binding.bottomNavigationView.uncheckAllItems()
            binding.viewPager2.setCurrentItem(2,false)

        }else{
            binding.bottomNavigationView.menu.getItem(1).isChecked = true;
            binding.viewPager2.setCurrentItem(1,false)
//            createFragment.isNetworkAvailable()

            viewModel.setpreviousFragmentIndex(1)
            historyFragment.disableDelete()

        }
    }

    fun LottieAnimationView.setAnimationTint(@ColorInt color: Int) {

        addValueCallback(
            KeyPath("**" ),
            LottieProperty.COLOR_FILTER
        ) { PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP) }
    }

    private fun getKeyPath(): KeyPath? {
        val keyPaths: List<KeyPath> = binding.lotie.resolveKeyPath(KeyPath("Fill", "Ellipse 1", "Fill 1"))
        for (i in keyPaths.indices) {
            Log.i("KeyPath", keyPaths[i].toString())
        }
        return if (keyPaths.size == 5) {
            keyPaths[4]
        } else {
            null
        }
    }






    lateinit var homeViewPager: HomeViewPager

    private fun createFragments() {

        val fragList= arrayListOf(
            shoppingFragment,
            createFragment,
            scanFragment,
            historyFragment,
            settingFragment
        )

        homeViewPager= HomeViewPager(this,fragList)

        binding.viewPager2.adapter= homeViewPager
        binding.viewPager2.offscreenPageLimit= homeViewPager.itemCount
        binding.viewPager2.isUserInputEnabled=false

        binding.viewPager2.setCurrentItem(2,false)

        binding.bottomNavigationView.setOnNavigationItemSelectedListener(
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.shopping ->{
                        binding.bottomNavigationView.menu.getItem(0).isChecked = true;
                        binding.viewPager2.setCurrentItem(0,false)
                        viewModel.setpreviousFragmentIndex(0)
                        historyFragment.disableDelete()

                    }
                    R.id.create -> {
                        binding.bottomNavigationView.menu.getItem(1).isChecked = true;
                        binding.viewPager2.setCurrentItem(1,false)
//                        createFragment.isNetworkAvailable()

                        viewModel.setpreviousFragmentIndex(1)
                        historyFragment.disableDelete()
                     }
                    R.id.history ->{
                        binding.bottomNavigationView.menu.getItem(3).isChecked = true
                        viewModel.setpreviousFragmentIndex(2)
                        binding.viewPager2.setCurrentItem(3,false)

                    }
                    R.id.setting -> {
                        settingFragment.checkContinousScan()
                        binding.bottomNavigationView.menu.getItem(4).isChecked = true
                        binding.viewPager2.setCurrentItem(4,false)
                        viewModel.setpreviousFragmentIndex(3)
                        historyFragment.disableDelete()

                      }
                }
                false
            })

        binding.scanIcon.setOnClickListener{
            scanFragment.checkContinousScan()
            historyFragment.disableDelete()
            binding.bottomNavigationView.uncheckAllItems()
            binding.viewPager2.setCurrentItem(2,false)



        }

    }
    private fun showCameraPermissionDialog() {

        Log.d("ShowDialod","called")
        val dialogView: View = this.layoutInflater.inflate(R.layout.dialog_home_camera_permission, null)

        val dialogBuilder :AlertDialog.Builder = AlertDialog.Builder(this, R.style.MyDialogStyle)
        dialogBuilder.setView(dialogView)
        dialogBuilder.setCancelable(false)
        dialogBuilder.create()
        val dialog= dialogBuilder.show()

        dialogView.findViewById<TextView>(R.id.allow).setOnClickListener{
            Log.d("BUttonClicked","yesyes")
            requestCameraPermission()
            dialog.dismiss()
        }

        dialogView.findViewById<TextView>(R.id.no).setOnClickListener {
            Log.d("BUttonClicked","noyes")
            dialog.dismiss()
        }
    }
    private fun requestCameraPermission(){
        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.CAMERA
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(multiplePermissionsReport: MultiplePermissionsReport) {

                    if (multiplePermissionsReport.areAllPermissionsGranted()) {

                    }else if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied) {
                        Log.d("PermissionHandler","permamnedenied")

                        if(sharedPref.cameraPermisionPermanetDeniedFirstTime){

                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            val uri: Uri = Uri.fromParts("package",this@HomeActivity.packageName, null)
                            intent.data = uri
                            cameraPermissionResultLauncher.launch(intent)
                        }
                        if(sharedPref.cameraPermisionPermanetDeniedFirstTime==false) sharedPref.cameraPermisionPermanetDeniedFirstTime=true

                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    list: List<PermissionRequest?>?,
                    permissionToken: PermissionToken
                ) {
                    Log.d("PermissionHandler","continue")
                    permissionToken.continuePermissionRequest()
                }
            }).withErrorListener { error: DexterError? ->

            }
            .onSameThread().check()
    }

    var cameraPermissionResultLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result->

        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){
        }
    }


    inline fun View.afterMeasured(crossinline block: () -> Unit) {
        if (measuredWidth > 0 && measuredHeight > 0) {
            block()
        } else {
            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
                override fun onGlobalLayout() {
                    if (measuredWidth > 0 && measuredHeight > 0) {
                        viewTreeObserver.removeOnGlobalLayoutListener(this)
                        block()
                    }
                }

            })
        }
    }

    fun checkReadExternalPermission(){
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU){
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_IMAGES)== PackageManager.PERMISSION_GRANTED) {

                openPhotoChooseIntent()
            }
            else {
                requestImagePickerPermission(Manifest.permission.READ_MEDIA_IMAGES)
            }
        }
        else{
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
            {
                openPhotoChooseIntent()

            }
            else {
                requestImagePickerPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }
    private fun openPhotoChooseIntent() {
        val getInt = Intent(Intent.ACTION_GET_CONTENT)
        getInt.type = "image/*"
        val pickInt = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickInt.type = "image/*"
        val chooserInt = Intent.createChooser(getInt, "Select Image")
        chooserInt.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickInt))
        filePicherResultLauncher.launch(chooserInt)
    }
    private var filePicherResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        if (result.resultCode == Activity.RESULT_OK) {

            Glide.with(this).asBitmap().load(result.data?.data)
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(object : CustomTarget<Bitmap?>() {
                    override fun onResourceReady(
                        bitmap: Bitmap,
                        transition: Transition<in Bitmap?>?,
                    ) {
                        readBarcodeImageFromBitmap(bitmap)

                    }

                    override fun onLoadCleared(placeholder: Drawable?) {}
                })
        }
    }
    private fun readBarcodeImageFromBitmap(myBitmap: Bitmap){

        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_ALL_FORMATS)
            .build()

        val image = InputImage.fromBitmap(myBitmap,0)
        val scanner = BarcodeScanning.getClient(options)
        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                if(barcodes.size==0){
                    Toast.makeText(this,getString(R.string.noBarcodesFound), Toast.LENGTH_SHORT).show()
                }else{
                    val barcodeValue=barcodes[0].rawValue
                    val barcodeFormat=barcodes[0].format
                    val barcodeValueType=barcodes[0].valueType

                    val intent = Intent(this, ScanResult::class.java)
                    intent.putExtra("barcodeValue", barcodeValue)
                    intent.putExtra("barcodeFormat", barcodeFormat)
                    intent.putExtra("barcodeValueType", barcodeValueType)
                    startActivity(intent)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this,"Failed", Toast.LENGTH_SHORT).show()
            }

    }

    private fun requestImagePickerPermission(permission:String){
        Dexter.withContext(this)
            .withPermissions(
                permission
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(multiplePermissionsReport: MultiplePermissionsReport) {

                    if (multiplePermissionsReport.areAllPermissionsGranted()) {

                        openPhotoChooseIntent()
                    }else if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied) {

                        if(sharedPref.imagePickerPermisionPermanetDeniedFirstTime){

                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            val uri: Uri = Uri.fromParts("package",packageName, null)
                            intent.data = uri
                            imagePickerPermissionLauncher.launch(intent)
                        }
                        if(sharedPref.imagePickerPermisionPermanetDeniedFirstTime==false) sharedPref.imagePickerPermisionPermanetDeniedFirstTime=true

                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    list: List<PermissionRequest?>?,
                    permissionToken: PermissionToken
                ) {
                    Log.d("PermissionHandler","continue")
                    permissionToken.continuePermissionRequest()
                }
            }).withErrorListener { error: DexterError? ->

            }
            .onSameThread().check()
    }
    var imagePickerPermissionLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result->

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU)//API33
        {
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_IMAGES)==
                PackageManager.PERMISSION_GRANTED) {

                openPhotoChooseIntent()
            }

        }
        else{
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)==
                PackageManager.PERMISSION_GRANTED) {

                openPhotoChooseIntent()
            }
        }
    }

    fun initializeCamera() {
//        startCamera()
    }

    override fun onResume() {
        super.onResume()


    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun onBackPressed() {
        if(!sharedPref.isAppRated){
            showRateDialog()
        }else{
            super.onBackPressed()
        }
    }
    private fun showRateDialog() {


        val dialogView: View = layoutInflater.inflate(R.layout.dialog_rate, null)

        val dialogBuilder : AlertDialog.Builder = AlertDialog.Builder(this, R.style.MyDialogStyle)
        dialogBuilder.setView(dialogView)
        dialogBuilder.setCancelable(false)
        dialogBuilder.create()
        val dialog= dialogBuilder.show()

        dialogView.findViewById<ImageView>(R.id.cross).setOnClickListener{

            dialog.dismiss()
        }

        var totalRating=5
        dialogView.findViewById<BaseRatingBar>(R.id.ratingBar).setOnRatingChangeListener(object :
            BaseRatingBar.OnRatingChangeListener {

            override fun onRatingChange(
                ratingBar: BaseRatingBar?,
                rating: Float,
                fromUser: Boolean
            ) {
                totalRating= rating.roundToInt()
                if(rating>2.0){

                    dialogView.findViewById<Button>(R.id.feedBackButton).text=getString(R.string.rateUs)

                }else{

                    dialogView.findViewById<Button>(R.id.feedBackButton).text=getString(R.string.giveFeedBack)

                }
            }
        })
        dialogView.findViewById<Button>(R.id.feedBackButton).setOnClickListener{

            sharedPref.isAppRated=true
            if(totalRating>2){
                rate()
            }else{
                feedBack()
            }
            dialog.cancel()
        }
        dialogView.findViewById<TextView>(R.id.exitText).setOnClickListener{
            finish()
        }

    }

    override fun changeTheme(theme: Int) {
        when(theme){
            0->{
//                application.setTheme(R.style.GreenTheme)
                applyTheme(R.style.GreenTheme)
            }
            1->{
//                application.setTheme(R.style.BlueTheme)
                applyTheme(R.style.BlueTheme)
            }
            2->{
//                application.setTheme(R.style.PurpleTheme)
                applyTheme(R.style.PurpleTheme)
            }
            3->{
//                application.setTheme(R.style.YellowTheme)
                applyTheme(R.style.YellowTheme)
            }
            4->{
//                application.setTheme(R.style.PinkTheme)
                applyTheme(R.style.PinkTheme)
            }
        }
    }
    fun applyTheme(themeResId: Int) {

        setTheme(themeResId)

        recreate()
    }

}