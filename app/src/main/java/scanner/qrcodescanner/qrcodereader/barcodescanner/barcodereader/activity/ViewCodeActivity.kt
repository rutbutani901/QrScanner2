package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.*
import android.text.format.DateFormat
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.PixelCopy
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.print.PrintHelper
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.NetworkChangeReceiver
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.R
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.databinding.ActivityViewCreatedCodeBinding
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.setGradientInStatusBar
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.setLocale
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.sharedPref
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.viewModel.BarcodeListViewModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import qrcodescanner.qrcodereader.barcodescanner.barcodereader.dataBase.DataModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class ViewCodeActivity : AppCompatActivity() {
    lateinit var barcodeListViewModel: BarcodeListViewModel

    val barcodeValueTypeHashMap: HashMap<Int, String> = hashMapOf(
        11 to "Calender Event",
        1 to "Contact",
        12 to "Driver License",
        2 to "Email",
        10 to "Geo",
        3 to "ISBN",
        4 to "Phone",
        5 to "Product",
        6 to "SMS",
        7 to "Text",
        0 to "Unknown",
        8 to "URL",
        9 to "WIFI"
    )
    val barcodeFormatMap: HashMap<Int, BarcodeFormat> = hashMapOf(
        32 to BarcodeFormat.EAN_13,
        512 to BarcodeFormat.UPC_A,
        2 to BarcodeFormat.CODE_39,
        4 to BarcodeFormat.CODE_93,
        256 to BarcodeFormat.QR_CODE,
        8 to BarcodeFormat.CODABAR,
        4096 to BarcodeFormat.AZTEC,//
        1 to BarcodeFormat.CODE_128,
        16 to BarcodeFormat.DATA_MATRIX,//
        64 to BarcodeFormat.EAN_8,
        128 to BarcodeFormat.ITF,
        2048 to BarcodeFormat.PDF_417,
        1024 to BarcodeFormat.UPC_E,
    )


    var barcodeValue: String = ""
    var barcodeFormat: Int = 0
    var barcodeType: Int = 0
    var note=""
    var comesFromBarcodeCreation=false

    var canPrint=true
    lateinit var binding:ActivityViewCreatedCodeBinding

    var barcodeInsertedId=-1L
    var isFav:Boolean=false

    override fun onCreate(savedInstanceState: Bundle?) {

        setLocale(sharedPref.languageCode)
        if(savedInstanceState==null){
            if (intent.hasExtra("customGenerator")) {//generate qr
//                Constant.interstitialId?.let {
//                    loadInterAd(4)
//                }
            }else{
//                Constant.interstitialId?.let {
//                    loadInterAd(1)
//                }

            }

        }
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
        super.onCreate(savedInstanceState)
        binding= ActivityViewCreatedCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setGradientInStatusBar(this)
        barcodeListViewModel = ViewModelProvider(this).get(BarcodeListViewModel::class.java)
        var installedAppName = ""


        if(sharedPref.isPremiumPurchased){
            binding.pro.visibility=View.GONE
        }else{
            binding.pro.visibility=View.VISIBLE
        }


        binding.backButton.setOnClickListener{
            onBackPressed()
        }
        var comeFromOtherBarcodes = false

        if (intent.hasExtra("customGenerator"))// generate qr
        {
            comesFromBarcodeCreation=true
            barcodeValue = intent.getStringExtra("barcodeValue").toString()
            barcodeType = intent.getIntExtra("barcodeType", 0)
            barcodeFormat = intent.getIntExtra("barcodeFormat", 0)
            isFav=false
            if (intent.hasExtra("installedAppName")) {

                installedAppName = intent.getStringExtra("installedAppName").toString()
            }
            if (intent.hasExtra("otherBarcodes")) {
                comeFromOtherBarcodes = intent.getBooleanExtra("otherBarcodes", false)
            }
        }
        else {// generate scanned imagges

            comesFromBarcodeCreation=false
//            barcodeValue = intent.getStringExtra("barcodeValue").toString()
//            barcodeFormat = intent.getIntExtra("barcodeFormat", 0)
//            barcodeType = intent.getIntExtra("barcodeValueType", 0)
//            note = intent.getStringExtra("barcodeNote").toString()
            barcode= intent.getSerializableExtra("barcodeData") as DataModel

            barcode?.let {
                barcodeValue=barcode!!.barValue
                barcodeType=barcode!!.barcodeType
                barcodeFormat=barcode!!.barFormat
                creationDateTime=barcode!!.creationTime
                isFav=barcode!!.isFav
                note=barcode!!.note

                barcodeInsertedId=barcode!!.id.toLong()
            }
        }



        val connectionLiveData = NetworkChangeReceiver(this)
        connectionLiveData.observe(this, androidx.lifecycle.Observer { isConnected ->
            isConnected?.let {
                if(isConnected){
                    binding.checkInternetLayout.visibility=View.GONE
                    binding.topCurversBg.visibility=View.VISIBLE

                    if(canPrint){// print layout is invisible
                        binding.scrollView.visibility=View.VISIBLE

                    }else{
                        binding.printPageFormat.visibility=View.VISIBLE
                    }
                    binding.more.visibility=View.VISIBLE

                }else{
                    binding.checkInternetLayout.visibility=View.VISIBLE
                    binding.topCurversBg.visibility=View.GONE
                    binding.scrollView.visibility=View.GONE
                    binding.printPageFormat.visibility=View.GONE
                    binding.more.visibility=View.GONE
                }
            }
        })

        binding.tapToRetry.setOnClickListener{
            binding.tapToRetry.text=getString(R.string.loading)
            Handler(Looper.getMainLooper()).postDelayed({
                binding.tapToRetry.text=getString(R.string.tapToRetry)
            },1000)
        }


        binding.more.setOnClickListener{
            if(canPrint){
                showMoreFeaturesDialog(binding.more)

            }else{
                binding.sharePngButton.visibility= View.VISIBLE
                binding.pro.visibility= View.VISIBLE
                binding.scrollView.visibility= View.VISIBLE
                binding.printPageFormat.visibility= View.GONE
                binding.more.setImageResource(R.drawable.ic_more)
                binding.more.setPadding(8,8,8,8)
                canPrint=!canPrint
            }
        }


        var barcodeFormatText = ""
        var format = barcodeFormatMap.get(barcodeFormat)
        if (format == null) {
            barcodeFormatText = getString(R.string.barcodeFormatNotFound)
        } else {
            barcodeFormatText = format.toString()
            generateQrImage(barcodeValue, format, 1000)
        }

        var barcodeValueTypeText = ""
        var type = barcodeValueTypeHashMap.get(barcodeType)
        if (type == null) {
            barcodeValueTypeText = getString(R.string.barcodeTypeNotSupported)

        } else {
            barcodeValueTypeText = type
        }


        if (barcodeValueTypeText.equals("URL")) barcodeValueTypeText = getString(R.string.website)

        binding.sharePngButton.setOnClickListener{

            if(barcodeImageToShare!=null){
                shareImageandText(barcodeImageToShare!!)
            }
        }

        binding.showInfo.setOnClickListener{
            Log.d("CallingCreatedShow","yes")
            val intent=Intent(this@ViewCodeActivity, CreatedCodeShowInfoActivity::class.java)
            intent.putExtra("barcodeValue",barcodeValue)
            intent.putExtra("barcodeType",barcodeType)
            intent.putExtra("barcodeFormat",barcodeFormat)
            intent.putExtra("isFav",isFav)
            intent.putExtra("note",note)
            intent.putExtra("barcodeInsertedId",barcodeInsertedId)
            intent.putExtra("creationDateTime",creationDateTime)
            resultLauncher.launch(intent)
        }
        binding.save.setOnClickListener{

            barcodeImageToShare?.let {
                val intent = Intent(this, SaveQrCodeActivity::class.java)

                // Convert the Bitmap to a byte array
                val stream = ByteArrayOutputStream()
                it.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                val byteArray = stream.toByteArray()
                intent.putExtra("bitmap", byteArray)
                startActivity(intent)
            }

        }

    }

    var resultLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            data?.let {
                isFav = it.getBooleanExtra("isFav",isFav)
                note = it.getStringExtra("note").toString()
                if(barcodeInsertedId.toInt()!=-1){
                    barcodeListViewModel.updateNotes(this,barcodeInsertedId.toInt(), note)
                }
            }
        }
    }


    fun showMoreFeaturesDialog(buttonView:View){
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val popupView: View = inflater.inflate(R.layout.more_function_view_code, null)

        if(isFav){
            popupView.findViewById<ImageView>(R.id.collectIcon).setImageResource(R.drawable.ic_fav_fill_history)
        }else{
            popupView.findViewById<ImageView>(R.id.collectIcon).setImageResource(R.drawable.ic_fav)
        }

        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true // lets taps outside the popup also dismiss it

        val popupWindow = PopupWindow(popupView, width, height, focusable)

        popupWindow.setElevation(20F);
        popupWindow.setBackgroundDrawable( ColorDrawable(Color.WHITE));
        popupWindow.showAsDropDown(buttonView, 0, 15, Gravity.NO_GRAVITY)

        popupView.findViewById<LinearLayout>(R.id.collect).setOnClickListener{
            popupWindow.dismiss()
            performFavTask()
        }
        popupView.findViewById<LinearLayout>(R.id.print).setOnClickListener{
            popupWindow.dismiss()
            binding.sharePngButton.visibility= View.GONE
            binding.pro.visibility= View.GONE
            binding.scrollView.visibility= View.GONE
            binding.printPageFormat.visibility= View.VISIBLE
            binding.more.setImageResource(R.drawable.cross_icon)
            binding.more.setPadding(1,1,1,1)
            binding.more.imageTintList= ContextCompat.getColorStateList(this,R.color.white)

            makePdf()
            canPrint=!canPrint

        }
    }
    fun performFavTask(){
        isFav=!isFav
        barcode?.let {
            if(barcodeInsertedId.toInt()!=-1){
                barcodeListViewModel.updateFav(this,barcodeInsertedId.toInt(), isFav)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    fun makePdf(){


        binding.printImage.setImageBitmap(barcodeImageToShare)

        binding.printLayoutButton.setOnClickListener{

            createBitmapFromLayout(binding.pageCardView){
                    newBitmap->

                val photoPrinter = PrintHelper(this@ViewCodeActivity)
                photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
                photoPrinter.printBitmap("Print Bitmap", newBitmap)
            }
            binding.scrollView.visibility= View.VISIBLE
            binding.sharePngButton.visibility= View.VISIBLE
            binding.pro.visibility= View.VISIBLE
            binding.printPageFormat.visibility= View.GONE
            binding.more.setImageResource(R.drawable.ic_more)
            binding.more.setPadding(8,8,8,8)
            canPrint=!canPrint

        }

    }




    private  fun createBitmapFromLayout(view:View,callbacks: (bitmap: Bitmap)-> Unit){
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val bitmap= Bitmap.createBitmap(view.width,view.height, Bitmap.Config.ARGB_8888)
            val location=IntArray(2)
            view.getLocationInWindow(location)
            PixelCopy.request(
                window,
                Rect(location[0],location[1],location[0]+view.width,location[1]+view.height),
                bitmap,{
                    if(it== PixelCopy.SUCCESS){
                        callbacks.invoke(bitmap)
                    }
                },
                Handler(Looper.getMainLooper())
            )
        }else{
            val bitmap= Bitmap.createBitmap(view.width,view.height, Bitmap.Config.ARGB_8888)
            val canvas= Canvas(bitmap)
            val bgDrawable=view.background
            if(bgDrawable!=null){
                bgDrawable.draw(canvas)
            }else{
                canvas.drawColor(Color.WHITE)
            }
            view.draw(canvas)
            callbacks.invoke(bitmap)
        }

    }

    var barcodeImageToShare: Bitmap?=null

    private fun shareImageandText(bitmap: Bitmap) {
        val uri = getImageToShare(bitmap)
        val intent = Intent(Intent.ACTION_SEND)

        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.putExtra(Intent.EXTRA_TEXT, barcodeValue)// add barcode value here
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here")
        intent.type = "image/png"
        startActivity(Intent.createChooser(intent, "Share Via"))
    }

    private fun getImageToShare(bitmap: Bitmap): Uri? {
        val imagefolder = File(cacheDir, "images")
        var uri: Uri? = null
        try {
            imagefolder.mkdirs()
            val file = File(imagefolder, "shared_image.png")
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream)
            outputStream.flush()
            outputStream.close()
            uri = FileProvider.getUriForFile(this, "$packageName.provider", file)
        } catch (e: Exception) {
            Toast.makeText(this, "" + e.message, Toast.LENGTH_LONG).show()
        }
        return uri
    }


    fun generateQrImage(barcodevalue: String, format: BarcodeFormat, width: Int) {

        val multiFormatWriter = MultiFormatWriter();
        try {
            val hintMap = mapOf(
                EncodeHintType.MARGIN to 0
            )
            val bitMatrix = multiFormatWriter.encode(barcodevalue, format, width, width, hintMap)
            val barcodeEncoder = BarcodeEncoder();

            if (format.toString().contains("QR_CODE") || format.toString()
                    .contains("DATA_MATRIX") || format.toString().contains("AZTEC")
            ) {

                val bitmap = barcodeEncoder.createBitmap(bitMatrix)

                binding.qrImage.visibility = View.VISIBLE
                binding.barcodeImage.visibility = View.GONE
                binding.qrImage.setImageBitmap(bitmap)
                barcodeImageToShare=bitmap
            } else {

                val bitmap = barcodeEncoder.createBitmap(bitMatrix)
                binding.qrImage.visibility = View.GONE
                binding.barcodeImage.visibility = View.VISIBLE
                binding.barcodeImage.setImageBitmap(bitmap)
                barcodeImageToShare=bitmap
            }

            if(comesFromBarcodeCreation){

                saveBarcodeInDb()
            }

        } catch (e: Exception) {
            Log.d("erroror", "Could not generate QR image")

        }
    }
    var barcode: DataModel?=null
    var creationDateTime:String=""
    fun saveBarcodeInDb() {
         creationDateTime = curentDataTime()
        barcode = DataModel(
            barcodeValue,
            barcodeFormat,
            creationDateTime,
            "",
            false,
            barcodeType,
            true
        )
        var calledOneTime=false
        if(!sharedPref.isDuplicateBarcodeEnable){
            barcodeListViewModel.doesBarcodesExist(this,barcodeValue).observe(this@ViewCodeActivity, androidx.lifecycle.Observer {

                if(it==null){

                    if(!calledOneTime){
                        CoroutineScope(Dispatchers.IO).launch {
                            barcodeInsertedId =barcodeListViewModel.insert(this@ViewCodeActivity,barcode!!)

                        }
                        calledOneTime=true
                    }

                }
            })


        }else{
            CoroutineScope(Dispatchers.IO).launch {
                barcodeInsertedId = barcodeListViewModel.insert(this@ViewCodeActivity,barcode!!)
            }

        }
    }

    fun curentDataTime(): String {
        var dateTime = ""
        val currentTime: Date = Calendar.getInstance().getTime()
        val df = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())//MM GIVES 03
        val formattedDate: String = df.format(currentTime)// 18 marc/2023

        dateTime += formattedDate + "   "

        val delegate = "hh:mm aaa"
        var time = DateFormat.format(delegate, Calendar.getInstance().time)// 11:17 pm
        dateTime += time

        return dateTime
    }
}