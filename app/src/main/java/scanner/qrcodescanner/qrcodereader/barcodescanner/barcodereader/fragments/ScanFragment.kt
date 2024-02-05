package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.fragments

import android.Manifest
import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.AudioManager
import android.media.ToneGenerator
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.provider.MediaStore
import android.provider.Settings
import android.text.format.DateFormat
import android.util.Log
import android.util.Size
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.R
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.activity.HelpActivity
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.activity.InAppPurchaseActivity
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.activity.ScanResult
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.ads.AdEventListener
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.ads.AdHandler
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.barcodeHelper.BarcodeAnalyzer
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.databinding.DialogEnterBarcodeBinding
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.databinding.FragmentScanBinding
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.feedBack
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.rate
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.sharedPref
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.util.Constants
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.viewModel.BarcodeListViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import qrcodescanner.qrcodereader.barcodescanner.barcodereader.dataBase.DataModel
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.roundToInt


class ScanFragment : Fragment() {

    lateinit var barcodeListViewModel: BarcodeListViewModel
    private var processingBarcode = AtomicBoolean(false)
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var cameraProvider: ProcessCameraProvider
    private lateinit var preview: Preview
    private lateinit var imageAnalysis: ImageAnalysis

    // Select back camera
    private var cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var flashOn = false
    var cam: Camera?=null
    var cameraZoomFactor:Float=0.0f

    lateinit var binding:FragmentScanBinding
    lateinit var fragmentActivity: FragmentActivity
    private var vibrator: Vibrator? = null

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

        binding= FragmentScanBinding.inflate(layoutInflater)

        loadAdaptiveBannerAd()
        barcodeListViewModel = ViewModelProvider(fragmentActivity)[BarcodeListViewModel::class.java]
        vibrator= fragmentActivity.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator?
        cameraExecutor = Executors.newSingleThreadExecutor()


        if(fragmentActivity.sharedPref.isPremiumPurchased){

            binding.gift.visibility=View.GONE
            binding.batchScanProIcon.visibility=View.GONE

            if(fragmentActivity.sharedPref.isContinousScanningEnabled){

                binding.batchScan.background=ContextCompat.getDrawable(fragmentActivity,R.drawable.shape_storke_main_color)

            }else{
                binding.batchScan.background=ContextCompat.getDrawable(fragmentActivity,R.drawable.shape_rounded_corners)
            }

        }else{
            binding.gift.visibility=View.VISIBLE
            binding.batchScanProIcon.visibility=View.VISIBLE
            fragmentActivity.sharedPref.isContinousScanningEnabled=false
            binding.batchScan.background=ContextCompat.getDrawable(fragmentActivity,R.drawable.shape_rounded_corners)
        }

        if(ContextCompat.checkSelfPermission(fragmentActivity, android.Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED) {
            binding.grantCameraPermission.visibility=View.GONE
            binding.mainScanLayout.visibility=View.VISIBLE

            initializeCamera()
        }
        else{
            binding.grantCameraPermission.visibility=View.VISIBLE
            binding.mainScanLayout.visibility=View.GONE

            showCameraPermissionDialog()
        }

        if(fragmentActivity.sharedPref.cameraOrientationBack) cameraSelector=CameraSelector.DEFAULT_BACK_CAMERA
        else cameraSelector=CameraSelector.DEFAULT_FRONT_CAMERA

        setClickListners()

        binding.previewView.afterMeasured {
            binding.previewView.setOnTouchListener { _, event ->
                return@setOnTouchListener when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        true
                    }
                    MotionEvent.ACTION_UP -> {
                        val factory: MeteringPointFactory = SurfaceOrientedMeteringPointFactory(
                            binding.previewView.width.toFloat(), binding.previewView.height.toFloat()
                        )
                        val autoFocusPoint = factory.createPoint(event.x, event.y)
                        try {

                            cam?.cameraControl?.startFocusAndMetering(
                                FocusMeteringAction.Builder(
                                    autoFocusPoint,
                                    FocusMeteringAction.FLAG_AF
                                ).apply {
                                    //focus only when the user tap the preview
                                    disableAutoCancel()
                                }.build()
                            )
                        } catch (e: CameraInfoUnavailableException) {
                            Log.d("ERROR", "cannot access camera", e)
                        }
                        true
                    }
                    else -> false // Unhandled event.
                }
            }
        }
        return binding.root
    }

    fun checkContinousScan(){
        if(this::binding.isInitialized){

            if(fragmentActivity.sharedPref.isContinousScanningEnabled){

                binding.batchScan.background=ContextCompat.getDrawable(fragmentActivity,R.drawable.shape_storke_main_color)

            }else{
                binding.batchScan.background=ContextCompat.getDrawable(fragmentActivity,R.drawable.shape_rounded_corners)

            }
        }
    }

    fun loadAdaptiveBannerAd() {
        if(!fragmentActivity.sharedPref.isPremiumPurchased){
            if(scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.ads.AdHandler.getInstance(fragmentActivity).isNetworkAvailable(fragmentActivity)){
                scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.ads.AdHandler.getInstance(fragmentActivity)
                    .loadAdaptiveBanner(fragmentActivity, binding.bannerLayout,
                        getString(R.string.adaptiveBanner), object :
                            scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.ads.AdEventListener {
                            override fun onAdLoaded(`object`: Any?) {
                                binding.bannerLayout.visibility=View.VISIBLE
                            }
                            override fun onAdClosed() {}
                            override fun onLoadError(errorCode: String?) {
                                binding.bannerLayout.visibility=View.GONE
                            }
                        })
            }else{
                binding.bannerLayout.visibility=View.GONE

            }
        }else{
            binding.bannerLayout.visibility=View.GONE

        }

    }

    private fun showCameraPermissionDialog() {

        Log.d("ShowDialod","called")
        val dialogView: View = fragmentActivity.layoutInflater.inflate(R.layout.dialog_home_camera_permission, null)

        val dialogBuilder : AlertDialog.Builder = AlertDialog.Builder(fragmentActivity, R.style.MyDialogStyle)
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
        Dexter.withContext(fragmentActivity)
            .withPermissions(
                Manifest.permission.CAMERA
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(multiplePermissionsReport: MultiplePermissionsReport) {

                    if (multiplePermissionsReport.areAllPermissionsGranted()) {

                        binding.grantCameraPermission.visibility=View.GONE
                        binding.mainScanLayout.visibility=View.VISIBLE

                        initializeCamera()

                    }else if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied) {
                        Log.d("PermissionHandler","permamnedenied")

                        if(fragmentActivity.sharedPref.cameraPermisionPermanetDeniedFirstTime){

                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            val uri: Uri = Uri.fromParts("package",fragmentActivity.packageName, null)
                            intent.data = uri
                            cameraPermissionResultLauncher.launch(intent)
                        }
                        if(fragmentActivity.sharedPref.cameraPermisionPermanetDeniedFirstTime==false) fragmentActivity.sharedPref.cameraPermisionPermanetDeniedFirstTime=true

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

        Constants.openOtherScreenFromApp=false
        if(ContextCompat.checkSelfPermission(fragmentActivity, android.Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){
            binding.grantCameraPermission.visibility=View.GONE
            binding.mainScanLayout.visibility=View.VISIBLE

            initializeCamera()
//            scanFragment.cameraPermissionGrantedInHome()
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

    private fun flash() {
        flashOn = !flashOn
        try {
            val cam = cameraProvider.bindToLifecycle(fragmentActivity, cameraSelector, preview, imageAnalysis)
            cam.cameraControl.enableTorch(flashOn)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private fun beep() {
        val toneGen = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
        toneGen.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200)
    }
    private fun vibrate() {
        vibrator?.vibrate(100);
    }

    fun setClickListners(){
        binding.flash.setOnClickListener {
            flash()
        }
        binding.help.setOnClickListener{

            startActivity(Intent(fragmentActivity, HelpActivity::class.java))
        }
        binding.scanImage.setOnClickListener{
            checkReadExternalPermission()
        }
        binding.seekbar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    val zoom=(progress/40.0).toFloat()
                    cam?.cameraControl?.setLinearZoom(zoom.toFloat())
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            });
        binding.zoomOutIcon.setOnClickListener{
            cam?.cameraControl?.setLinearZoom(0.0F)
            binding.seekbar.progress=0
        }
        binding.zoomInIcon.setOnClickListener{
            cam?.let {

                cam!!.cameraControl.setLinearZoom(1.0F)
            }
            binding.seekbar.progress=40
        }


        binding.openSetting.setOnClickListener{
            Constants.openOtherScreenFromApp=true
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri: Uri = Uri.fromParts("package",fragmentActivity.packageName, null)
            intent.data = uri
            cameraPermissionResultLauncher.launch(intent)
        }


        binding.like.setOnClickListener{
            showRateDialog()
        }
        binding.gift.setOnClickListener{
            startActivity(Intent(fragmentActivity, InAppPurchaseActivity::class.java))
        }

        binding.barcode.setOnClickListener{
showEnterBarcodeDialog()
        }
        binding.batchScan.setOnClickListener{

            if(fragmentActivity.sharedPref.isPremiumPurchased){

                if(fragmentActivity.sharedPref.isContinousScanningEnabled){
                    fragmentActivity.sharedPref.isContinousScanningEnabled=false
                    binding.batchScan.background=ContextCompat.getDrawable(fragmentActivity,R.drawable.shape_rounded_corners)
                }else{
                    fragmentActivity.sharedPref.isContinousScanningEnabled=true
                    binding.batchScan.background=ContextCompat.getDrawable(fragmentActivity,R.drawable.shape_storke_main_color)
                }

            }else{
                fragmentActivity.sharedPref.isContinousScanningEnabled=false
                startActivity(Intent(fragmentActivity, InAppPurchaseActivity::class.java))
            }
        }
    }

    private fun showRateDialog() {


        val dialogView: View = fragmentActivity.layoutInflater.inflate(R.layout.dialog_rate, null)

        val dialogBuilder : AlertDialog.Builder = AlertDialog.Builder(fragmentActivity, R.style.MyDialogStyle)
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

            fragmentActivity.sharedPref.isAppRated=true
            if(totalRating>2){
                fragmentActivity.rate()
            }else{
                fragmentActivity.feedBack()
            }
            dialog.cancel()
        }
        dialogView.findViewById<TextView>(R.id.exitText).setOnClickListener{
            fragmentActivity.finish()
        }

    }

    lateinit var enterBarcodeDialog: DialogEnterBarcodeBinding
    fun showEnterBarcodeDialog()
    {
        enterBarcodeDialog= DialogEnterBarcodeBinding.inflate(layoutInflater)

        var dialog= MaterialAlertDialogBuilder(fragmentActivity,R.style.ThemeOverlay_App_MaterialAlertDialog)
            .setView(enterBarcodeDialog.root)
            .setOnCancelListener(object : DialogInterface.OnCancelListener{
                override fun onCancel(dialog: DialogInterface?) {
                }

            })
            .show()

        dialog.window?.setBackgroundDrawable(resources.getDrawable(R.drawable.shape_rounded_corners))
        dialog.setCanceledOnTouchOutside(false)
        enterBarcodeDialog.cross.setOnClickListener{

            dialog.cancel()
        }

        enterBarcodeDialog.ok.setOnClickListener{
            val value=enterBarcodeDialog.inputText.editableText.toString()
            val intent = Intent(fragmentActivity, ScanResult::class.java)
            intent.putExtra("barcodeValue", value)
            intent.putExtra("barcodeFormat", 256)
            intent.putExtra("barcodeValueType", 7)
            startActivity(intent)

            dialog.cancel()
        }
    }





    fun checkReadExternalPermission(){
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU){
            if(ContextCompat.checkSelfPermission(fragmentActivity, android.Manifest.permission.READ_MEDIA_IMAGES)== PackageManager.PERMISSION_GRANTED) {

                openPhotoChooseIntent()
            }
            else {
                requestImagePickerPermission(Manifest.permission.READ_MEDIA_IMAGES)
            }
        }
        else{
            if(ContextCompat.checkSelfPermission(fragmentActivity, android.Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
            {
                openPhotoChooseIntent()

            }
            else {
                requestImagePickerPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }
    private fun openPhotoChooseIntent() {
        Constants.openOtherScreenFromApp=true
        val getInt = Intent(Intent.ACTION_GET_CONTENT)
        getInt.type = "image/*"
        val pickInt = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickInt.type = "image/*"
        val chooserInt = Intent.createChooser(getInt, "Select Image")
        chooserInt.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickInt))
        filePicherResultLauncher.launch(chooserInt)
    }
    private var filePicherResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        Constants.openOtherScreenFromApp=false
        if (result.resultCode == Activity.RESULT_OK) {

            Glide.with(fragmentActivity).asBitmap().load(result.data?.data)
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

    override fun onDestroy() {
        super.onDestroy()
        Constants.openOtherScreenFromApp=false
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
                    Toast.makeText(fragmentActivity,getString(R.string.noBarcodesFound), Toast.LENGTH_SHORT).show()
                }else{
                    val barcodeValue=barcodes[0].rawValue
                    val barcodeFormat=barcodes[0].format
                    val barcodeValueType=barcodes[0].valueType

                    val intent = Intent(fragmentActivity, ScanResult::class.java)
                    intent.putExtra("barcodeValue", barcodeValue)
                    intent.putExtra("barcodeFormat", barcodeFormat)
                    intent.putExtra("barcodeValueType", barcodeValueType)
                    startActivity(intent)
                }
            }
            .addOnFailureListener {
                Toast.makeText(fragmentActivity,"Failed", Toast.LENGTH_SHORT).show()
            }

    }

    private fun requestImagePickerPermission(permission:String){
        Dexter.withContext(fragmentActivity)
            .withPermissions(
                permission
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(multiplePermissionsReport: MultiplePermissionsReport) {

                    if (multiplePermissionsReport.areAllPermissionsGranted()) {

                        openPhotoChooseIntent()
                    }else if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied) {

                        if(fragmentActivity.sharedPref.imagePickerPermisionPermanetDeniedFirstTime){

                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            val uri: Uri = Uri.fromParts("package",fragmentActivity.packageName, null)
                            intent.data = uri
                            imagePickerPermissionLauncher.launch(intent)
                        }
                        if(fragmentActivity.sharedPref.imagePickerPermisionPermanetDeniedFirstTime==false) fragmentActivity.sharedPref.imagePickerPermisionPermanetDeniedFirstTime=true

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
            if(ContextCompat.checkSelfPermission(fragmentActivity, android.Manifest.permission.READ_MEDIA_IMAGES)==
                PackageManager.PERMISSION_GRANTED) {

                openPhotoChooseIntent()
            }

        }
        else{
            if(ContextCompat.checkSelfPermission(fragmentActivity, android.Manifest.permission.READ_EXTERNAL_STORAGE)==
                PackageManager.PERMISSION_GRANTED) {

                openPhotoChooseIntent()
            }
        }
    }

    fun initializeCamera() {
        startCamera()
    }

    private fun startCamera() {
        try {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(fragmentActivity)

            cameraProviderFuture.addListener(Runnable{

                cameraProvider = cameraProviderFuture.get()

                preview = Preview.Builder().build()
                binding.previewView.implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                preview.setSurfaceProvider(binding.previewView.surfaceProvider)


                // Setup the ImageAnalyzer for the ImageAnalysis use case
                val builder = ImageAnalysis.Builder().setTargetResolution(Size(1080, 1920))
                //val builder = ImageAnalysis.Builder().setTargetResolution(Size(120, 500))


                imageAnalysis = builder.build()
                    .also {
                        it.setAnalyzer(cameraExecutor, BarcodeAnalyzer { barcodeValue, barcodeFormat,barcodeValueType ,bitmape->//
                            if (processingBarcode.compareAndSet(false, true)) {

                                if(fragmentActivity.sharedPref.playSound){
                                    beep()
                                }
                                if(fragmentActivity.sharedPref.vibrateOnScan){
                                    vibrate()
                                }

                                if(fragmentActivity.sharedPref.confirmScanManually){

                                    binding.manualScan.visibility=View.VISIBLE
                                    binding.manualScan.setOnClickListener{

                                        val scanContinous=fragmentActivity.sharedPref.isContinousScanningEnabled
                                        if(scanContinous){

                                            val creationDate=curentDataTime()
                                            val dataModel= DataModel(
                                                barcodeValue,
                                                barcodeFormat.toInt(),
                                                creationDate,
                                                "",
                                                false,
                                                barcodeValueType,
                                                false
                                            )

                                            // barcodeListViewModel.insert(fragmentActivity,dataModel)
                                            var calledOneTime=false
                                            if(!fragmentActivity.sharedPref.isDuplicateBarcodeEnable){

                                                CoroutineScope(Dispatchers.IO).launch {
                                                    if(!barcodeListViewModel.newdoesBarcodesExist(fragmentActivity,barcodeValue)){
                                                        if(!calledOneTime){

                                                            barcodeListViewModel.insert(fragmentActivity,dataModel)
                                                            calledOneTime=true
                                                        }
                                                    }
                                                }
                                            }else{
                                                CoroutineScope(Dispatchers.IO).launch {
                                                    barcodeListViewModel.insert(fragmentActivity,dataModel)
                                                }

                                            }
                                            processingBarcode.set(false)

                                        }
                                        else{
                                            val intent = Intent(fragmentActivity, ScanResult::class.java)
                                            intent.putExtra("barcodeValue", barcodeValue)
                                            intent.putExtra("barcodeFormat", barcodeFormat.toInt())
                                            intent.putExtra("barcodeValueType", barcodeValueType)
                                            startActivity(intent)
                                        }
                                    }

                                }
                                else{
                                    binding.manualScan.visibility=View.GONE
//
                                    val scanContinous=fragmentActivity.sharedPref.isContinousScanningEnabled
                                    if(scanContinous){

                                        val creationDate=curentDataTime()
                                        val dataModel= DataModel(
                                            barcodeValue,
                                            barcodeFormat.toInt(),
                                            creationDate,
                                            "",
                                            false,
                                            barcodeValueType,
                                            false
                                        )

                                        // barcodeListViewModel.insert(fragmentActivity,dataModel)
                                        var calledOneTime=false
                                        if(!fragmentActivity.sharedPref.isDuplicateBarcodeEnable){

                                            CoroutineScope(Dispatchers.IO).launch {
                                                if(!barcodeListViewModel.newdoesBarcodesExist(fragmentActivity,barcodeValue)){
                                                    if(!calledOneTime){

                                                        barcodeListViewModel.insert(fragmentActivity,dataModel)
                                                        calledOneTime=true
                                                    }
                                                }
                                            }
                                        }else{
                                            CoroutineScope(Dispatchers.IO).launch {
                                                barcodeListViewModel.insert(fragmentActivity,dataModel)
                                            }

                                        }
                                        processingBarcode.set(false)

                                    }
                                    else{
                                        val intent = Intent(fragmentActivity, ScanResult::class.java)
                                        intent.putExtra("barcodeValue", barcodeValue)
                                        intent.putExtra("barcodeFormat", barcodeFormat.toInt())
                                        intent.putExtra("barcodeValueType", barcodeValueType)
                                        startActivity(intent)
                                    }
                                }


                            }
                        })
                    }
                try {
                    cameraProvider.unbindAll()
                    cam = cameraProvider.bindToLifecycle(fragmentActivity, cameraSelector, preview, imageAnalysis)
                    //cam.cameraControl.startFocusAndMetering()
                    autoFocus()

                    cam?.let {
                        if (cam!!.cameraInfo.hasFlashUnit()) {
                            cam!!.cameraControl.enableTorch(flashOn)
                            cam!!.cameraControl.setLinearZoom(cameraZoomFactor)
                            binding.flash.visibility = View.VISIBLE
                        }
                    }

                } catch (e: Exception) {
                    Log.e("PreviewUseCase", "Binding failed! :(", e)
                }
            }, ContextCompat.getMainExecutor(fragmentActivity))

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun curentDataTime():String
    {
        var dateTime=""
        val currentTime: Date = Calendar.getInstance().getTime()
        val df = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())//MM GIVES 03
        val formattedDate: String = df.format(currentTime)// 18 marc/2023

        dateTime += formattedDate+"   "

        val delegate = "hh:mm aaa"
        var time= DateFormat.format(delegate, Calendar.getInstance().time)// 11:17 pm
        dateTime += time

        return dateTime
    }

    fun autoFocus()
    {
        binding.previewView.afterMeasured {
            val autoFocusPoint = SurfaceOrientedMeteringPointFactory(1f, 1f)
                .createPoint(.5f, .5f)
            try {
                val autoFocusAction = FocusMeteringAction.Builder(
                    autoFocusPoint,
                    FocusMeteringAction.FLAG_AF
                ).apply {
                    //start auto-focusing after 2 seconds
                    setAutoCancelDuration(2, TimeUnit.SECONDS)
                }.build()


                cam?.cameraControl?.startFocusAndMetering(autoFocusAction)

            } catch (e: CameraInfoUnavailableException) {
                Log.d("ERROR", "cannot access camera", e)
            }

        }
    }

    override fun onResume() {
        super.onResume()
        processingBarcode.set(false)
        binding.manualScan.visibility=View.GONE
        if(fragmentActivity.sharedPref.cameraOrientationBack) cameraSelector=CameraSelector.DEFAULT_BACK_CAMERA
        else cameraSelector=CameraSelector.DEFAULT_FRONT_CAMERA
        startCamera()

    }


    override fun onDestroyView() {
        super.onDestroyView()
        cameraExecutor.shutdown()
        binding.manualScan.visibility=View.GONE
        vibrator?.cancel();
    }

}