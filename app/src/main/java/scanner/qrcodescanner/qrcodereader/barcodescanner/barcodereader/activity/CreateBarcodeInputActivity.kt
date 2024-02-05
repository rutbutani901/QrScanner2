package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.TextView
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.R
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.databinding.ActivityCreateBarcodeInputBinding
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.setGradientInStatusBar
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.setLocale
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.sharedPref
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.modelClass.BarcodeModelClass
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter

class CreateBarcodeInputActivity : AppCompatActivity() {

    lateinit var binding:ActivityCreateBarcodeInputBinding
    var isValidInput=false

    lateinit var barcode: BarcodeModelClass
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
        binding= ActivityCreateBarcodeInputBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setGradientInStatusBar(this)



       barcode=intent.getSerializableExtra("barcodeData") as BarcodeModelClass

        if(barcode.barcodeFormat==256 ||
            barcode.barcodeFormat==16 ||
            barcode.barcodeFormat==2048 ||
            barcode.barcodeFormat==4096 ||
            barcode.barcodeFormat==1 ||
            barcode.barcodeFormat==4 ||
            barcode.barcodeFormat==2 ){

            //binding.inputText.inputType=InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE
        }
        else{
            binding.inputText.inputType= InputType.TYPE_CLASS_NUMBER
        }

        binding.backButton.setOnClickListener{
            onBackPressed()
        }

        binding.inputText.hint=barcode.description
        binding.title.text=barcode.barcodeName
        binding.tickButton.setOnClickListener{

            gotoNextScreen()

        }

        binding.create.setOnClickListener{

            gotoNextScreen()

        }


        binding.inputText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if(binding.inputText.text.toString().trim()!=""){

                    inputErrorIconShown=false
                    popUpWindow?.dismiss()
                    binding.textNoInputIcon.visibility=View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

    }

    fun gotoNextScreen(){
        val inputText=binding.inputText.text.toString().trim()
        val multiFormatWriter= MultiFormatWriter();
        val formatValue=barcode.barcodeFormat
        when(formatValue){

            256->{
                isValidInput=true
            }
            16 ->{
                try{

                    multiFormatWriter.encode(inputText, BarcodeFormat.DATA_MATRIX, 1, 1)
                    isValidInput=true

                }catch (e:java.lang.Exception){
                    isValidInput=false
                }


            }
            2048->{
                try{

                    multiFormatWriter.encode(inputText, BarcodeFormat.PDF_417, 1, 1)
                    isValidInput=true

                }catch (e:java.lang.Exception){
                    isValidInput=false
                }
            }
            4096->{
                try{

                    multiFormatWriter.encode(inputText, BarcodeFormat.AZTEC, 1, 1)
                    isValidInput=true

                }catch (e:java.lang.Exception){
                    isValidInput=false
                }
            }
            32->{
                try{


                    multiFormatWriter.encode(inputText, BarcodeFormat.EAN_13, 1, 1)
                    isValidInput=true

                }catch (e:java.lang.Exception){
                    isValidInput=false
                }

            }
            64->{
                try{
                    multiFormatWriter.encode(inputText, BarcodeFormat.EAN_8, 1, 1)
                    isValidInput=true

                }catch (e:java.lang.Exception){
                    isValidInput=false
                }
            }
            1024->{
                try{

                    multiFormatWriter.encode(inputText, BarcodeFormat.UPC_E, 1, 1)
                    isValidInput=true

                }catch (e:java.lang.Exception){
                    isValidInput=false
                }
            }
            512->{
                try{


                    multiFormatWriter.encode(inputText, BarcodeFormat.UPC_A, 1, 1)
                    isValidInput=true

                }catch (e:java.lang.Exception){
                    isValidInput=false
                }
            }
            1->{
                try{

                    multiFormatWriter.encode(inputText, BarcodeFormat.CODE_128, 1, 1)
                    isValidInput=true

                }catch (e:java.lang.Exception){
                    isValidInput=false
                }
            }
            4->{
                try{


                    multiFormatWriter.encode(inputText, BarcodeFormat.CODE_93, 1, 1)
                    isValidInput=true

                }catch (e:java.lang.Exception){
                    isValidInput=false
                }
            }
            2->{
                try{

                    multiFormatWriter.encode(inputText, BarcodeFormat.CODE_39, 1, 1)
                    isValidInput=true

                }catch (e:java.lang.Exception){
                    isValidInput=false
                }
            }
            8->{
                try{

                    multiFormatWriter.encode(inputText, BarcodeFormat.CODABAR, 1, 1)
                    isValidInput=true

                }catch (e:java.lang.Exception){
                    isValidInput=false
                }
            }
            128->{

                try{


                    multiFormatWriter.encode(inputText, BarcodeFormat.ITF, 1, 1)
                    isValidInput=true

                }catch (e:java.lang.Exception){
                    isValidInput=false
                }}

        }


        if(inputText != ""){

            if(isValidInput)
            {
                val intent= Intent(this, ViewCodeActivity::class.java)
                intent.putExtra("customGenerator",1)
                intent.putExtra("barcodeValue",inputText)
                if(formatValue==32 || formatValue==64|| formatValue==1024||formatValue==512){
                    intent.putExtra("barcodeType",5)// 7 means product type
                }else{
                    intent.putExtra("barcodeType",7)// 7 means text type
                }
                intent.putExtra("otherBarcodes",true)// 7 means text type
                intent.putExtra("barcodeFormat",barcode.barcodeFormat)//256


                startActivity(intent)
            }
            else {

                if(!inputErrorIconShown){

                    inputErrorIconShown=true
                    binding.textNoInputIcon.visibility=View.VISIBLE
                    noInputPopUp(binding.textNoInputIcon,true)
                }
            }

        }
        else{

            if(!inputErrorIconShown){

                inputErrorIconShown=true
                binding.textNoInputIcon.visibility=View.VISIBLE
                noInputPopUp(binding.textNoInputIcon,false)
            }
        }
    }

    var popUpWindow: PopupWindow?=null

    var inputErrorIconShown=false



    fun noInputPopUp(refView: View, isInValidInput:Boolean){

        val inflater= applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.item_no_input_popup,null)
        popUpWindow= PopupWindow(view,
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT,true)

        if(isInValidInput) view.findViewById<TextView>(R.id.title).text=getString(R.string.invalidBarcodeInputType)
        else               view.findViewById<TextView>(R.id.title).text=getString(R.string.noInputFoundText)
        popUpWindow?.showAsDropDown(refView,0,10, Gravity.END)
        popUpWindow?.isFocusable=false
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}