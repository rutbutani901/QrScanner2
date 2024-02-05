package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.activity

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.R
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.adapter.BarcodeFunctionalityAdapter
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.databinding.ActivityScanCodeShowInfoBinding
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.databinding.DialogNoteBinding
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.setGradientInStatusBar
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.setLocale
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.sharedPref
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.modelClass.BarcodeFunctionalityModelClass
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.viewModel.BarcodeListViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import qrcodescanner.qrcodereader.barcodescanner.barcodereader.dataBase.DataModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*

class ScanCodeShowInfoActivity : AppCompatActivity() {

    lateinit var binding:ActivityScanCodeShowInfoBinding

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

    var barcode: DataModel?=null
    var barcodeValue: String = ""
    var barcodeFormat: Int = 0
    var barcodeValueType: Int = 0
    var note=""
    var comesFromBarcodeCreation=false



    var barcodeInsertedId=-1L
    var isFav:Boolean=false

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
        binding= ActivityScanCodeShowInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setGradientInStatusBar(this)
        barcodeListViewModel = ViewModelProvider(this).get(BarcodeListViewModel::class.java)
        var installedAppName = ""


        binding.backButton.setOnClickListener{
            onBackPressed()
        }
        var comeFromOtherBarcodes = false

//        if (intent.hasExtra("customGenerator"))// generate qr
//        {
//            comesFromBarcodeCreation=true
//            barcodeValue = intent.getStringExtra("barcodeValue").toString()
//            barcodeValueType = intent.getIntExtra("barcodeValueType", 0)
//            barcodeFormat = intent.getIntExtra("barcodeFormat", 0)
//            isFav=false
//            if (intent.hasExtra("installedAppName")) {
//
//                installedAppName = intent.getStringExtra("installedAppName").toString()
//            }
//            if (intent.hasExtra("otherBarcodes")) {
//                comeFromOtherBarcodes = intent.getBooleanExtra("otherBarcodes", false)
//            }
//        }
//        else {// generate scanned imagges
//
//            comesFromBarcodeCreation=false
//            barcodeValue = intent.getStringExtra("barcodeValue").toString()
//            barcodeFormat = intent.getIntExtra("barcodeFormat", 0)
//            barcodeValueType = intent.getIntExtra("barcodeValueType", 0)
//            note = intent.getStringExtra("barcodeNote").toString()
//
//
//
//        }

        barcodeValue = intent.getStringExtra("barcodeValue").toString()
        barcodeFormat = intent.getIntExtra("barcodeFormat", 0)
        barcodeValueType = intent.getIntExtra("barcodeValueType", 0)
        note = intent.getStringExtra("barcodeNote").toString()
        isFav = intent.getBooleanExtra("isFav",false)
        barcodeInsertedId = intent.getLongExtra("barcodeInsertedId",-1L)

        binding.productLogo.visibility= View.GONE
        binding.productDescription.visibility= View.GONE
        binding.productTitle.visibility= View.GONE
        binding.resultLayout.visibility= View.VISIBLE
        when(barcodeValueType){
            8->{
                binding.title.text=getString(R.string.website)
                binding.resultTitle.text=getString(R.string.website)
                binding.resultLogo.setImageResource(R.drawable.ic_website)
            }
            4->{
                binding.title.text=getString(R.string.phone)
                binding.resultTitle.text=getString(R.string.phone)
                binding.resultLogo.setImageResource(R.drawable.ic_phone)

            }
            1->{
                binding.title.text=getString(R.string.contact)
                binding.resultTitle.text=getString(R.string.contact)
                binding.resultLogo.setImageResource(R.drawable.ic_contact)
            }
            9->{
                binding.title.text=getString(R.string.newWifi)
                binding.resultTitle.text=getString(R.string.newWifi)
                binding.resultLogo.setImageResource(R.drawable.ic_wifi)
            }
            11->{
                binding.title.text=getString(R.string.event)
                binding.resultTitle.text=getString(R.string.event)
                binding.resultLogo.setImageResource(R.drawable.ic_event)

            }
            7->{
                binding.title.text=getString(R.string.text)
                binding.resultTitle.text=getString(R.string.text)
                binding.resultLogo.setImageResource(R.drawable.ic_text)
            }
            2->{
                binding.title.text=getString(R.string.newEmail)
                binding.resultTitle.text=getString(R.string.newEmail)
                binding.resultLogo.setImageResource(R.drawable.ic_mail)
            }
            6->{
                binding.title.text=getString(R.string.message)
                binding.resultTitle.text=getString(R.string.message)
                binding.resultLogo.setImageResource(R.drawable.ic_message)
            }
            10->{
                binding.title.text=getString(R.string.location)
                binding.resultTitle.text=getString(R.string.location)
                binding.resultLogo.setImageResource(R.drawable.ic_location)
            }
            5->{
                binding.title.text=getString(R.string.product)
                binding.resultTitle.text=getString(R.string.product)
                binding.resultLogo.setImageResource(R.drawable.ic_product)
            }
            15->{
                binding.title.text=getString(R.string.facebbok)
                binding.resultTitle.text=getString(R.string.facebbok)
                binding.resultLogo.setImageResource(R.drawable.ic_facebook)
            }
            16->{
                binding.title.text=getString(R.string.instagram)
                binding.resultTitle.text=getString(R.string.instagram)
                binding.resultLogo.setImageResource(R.drawable.ic_instagram)
            }
            17->{
                binding.title.text=getString(R.string.twitter)
                binding.resultTitle.text=getString(R.string.twitter)
                binding.resultLogo.setImageResource(R.drawable.ic_twitter)
            }
            18->{
                binding.title.text=getString(R.string.whatsapp)
                binding.resultTitle.text=getString(R.string.whatsapp)
                binding.resultLogo.setImageResource(R.drawable.ic_whatsapp)
            }
            19->{
                binding.title.text=getString(R.string.viber)
                binding.resultTitle.text=getString(R.string.viber)
                binding.resultLogo.setImageResource(R.drawable.ic_viber)
            }
            else->{
                binding.title.text=getString(R.string.text)
                binding.resultTitle.text=getString(R.string.text)
                binding.resultLogo.setImageResource(R.drawable.ic_text)
            }
        }
        binding.more.setOnClickListener{
            showMoreFeaturesDialog(binding.more)
        }
        if(note != "") {
            binding.notes.visibility = View.VISIBLE
            binding.notes.text = note
        }else{
            binding.notes.visibility=View.GONE
        }

//        if(!TextUtils.isEmpty(note)){
//            binding.notes.visibility= View.VISIBLE
//            binding.notes.text="${getString(R.string.note)}:- \n"+note
//        }else{
//            binding.notes.visibility= View.GONE
//        }

        val format = barcodeFormatMap.get(barcodeFormat)
        format?.let {
            generateQrImage(barcodeValue, format, 1000)
        }

        val funcList= ArrayList<BarcodeFunctionalityModelClass>()
        funcList.add(BarcodeFunctionalityModelClass(getString(R.string.showInfo),R.drawable.ic_info))
        settingAdapter(funcList,barcodeValue)

    }
    lateinit var barcodeFuncAdapter: BarcodeFunctionalityAdapter
    fun settingAdapter(barcodeFunList:ArrayList<BarcodeFunctionalityModelClass>, barcodeValue:String) {

        barcodeFuncAdapter= BarcodeFunctionalityAdapter(this,barcodeFunList) {
                holderTitle,holderValue->//holderName

            if(holderTitle == getString(R.string.showInfo))
            {
               finish()
            }

        }

        val myLinearLayoutManager = object : LinearLayoutManager(this) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        binding.otherRecycler.layoutManager = myLinearLayoutManager
        binding.otherRecycler.adapter=barcodeFuncAdapter
    }





    fun showMoreFeaturesDialog(buttonView:View){
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val popupView: View = inflater.inflate(R.layout.more_function_scan_code_show_info, null)

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

        popupView.findViewById<LinearLayout>(R.id.note).setOnClickListener{
            popupWindow.dismiss()
            showNotesDialog()
        }
        popupView.findViewById<LinearLayout>(R.id.save).setOnClickListener{
            popupWindow.dismiss()
            saveBarcodeImage()
        }
        popupView.findViewById<LinearLayout>(R.id.collect).setOnClickListener{
            popupWindow.dismiss()
            performFavTask()
        }

        popupView.findViewById<LinearLayout>(R.id.share).setOnClickListener{
            popupWindow.dismiss()
            shareQrCode()
        }

    }
    fun saveBarcodeImage(){
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
    lateinit var notesDialog: DialogNoteBinding
    fun showNotesDialog()
    {
        notesDialog= DialogNoteBinding.inflate(layoutInflater)
        notesDialog.inputText.setText(note)
        var dialog= MaterialAlertDialogBuilder(this,R.style.ThemeOverlay_App_MaterialAlertDialog)
            .setView(notesDialog.root)
            .setOnCancelListener(object : DialogInterface.OnCancelListener{
                override fun onCancel(dialog: DialogInterface?) {
                }

            })
            .show()

        dialog.window?.setBackgroundDrawable(resources.getDrawable(R.drawable.shape_rounded_corners))
        dialog.setCanceledOnTouchOutside(false)
        notesDialog.cross.setOnClickListener{

            dialog.cancel()
        }
        notesDialog.ok.isSelected=true

        notesDialog.ok.setOnClickListener{

            note=notesDialog.inputText.editableText.toString()

            note=note.trim()// remove starting space

            if(barcodeInsertedId.toInt()!=-1){

                barcodeListViewModel.updateNotes(this,barcodeInsertedId.toInt(), note)
            }

            if(note!="") {

                binding.notes.visibility=View.VISIBLE
                binding.notes.text= note
            }
            else{//first there was note and then u remove so close view

                if(binding.notes.visibility== View.VISIBLE) {

                    binding.notes.visibility= View.GONE
                }
            }
            dialog.cancel()
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
    fun shareQrCode(){

        if(barcodeImageToShare!=null){
            shareImageandText(barcodeImageToShare!!)
        }
    }



    override fun onBackPressed() {
        val intent=Intent(this, ScanResult::class.java)
        intent.putExtra("isFav",isFav)
        intent.putExtra("note",note)
        setResult(Activity.RESULT_OK,intent)
        super.onBackPressed()
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

        } catch (e: Exception) {
            Log.d("erroror", "Could not generate QR image")

        }
    }


}