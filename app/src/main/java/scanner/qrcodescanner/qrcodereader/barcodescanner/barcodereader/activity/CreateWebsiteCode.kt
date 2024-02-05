package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.PopupWindow
import android.widget.RelativeLayout
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.R
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.databinding.ActivityCreateWebsiteCodeBinding
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.setGradientInStatusBar
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.setLocale
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.sharedPref

class CreateWebsiteCode : AppCompatActivity() {

    lateinit var binding:ActivityCreateWebsiteCodeBinding
    var pressTime: Long = 0
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            pressTime = System.currentTimeMillis()
        } else if (ev.action == MotionEvent.ACTION_UP) {
            val releaseTime = System.currentTimeMillis()
            if (releaseTime - pressTime < 200) {
                if (currentFocus != null) {

                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

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
        binding= ActivityCreateWebsiteCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setGradientInStatusBar(this)

        binding.backButton.setOnClickListener{
            onBackPressed()
        }
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
                    binding.enteredWebsiteNoInputIcon.visibility=View.GONE
                }else{

                    if(!inputErrorIconShown){

                        binding.enteredWebsiteNoInputIcon.visibility=View.VISIBLE
                        showEnteredWebsiteNoInputPopUp(binding.enteredWebsiteNoInputIcon)
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })


    }
    fun gotoNextScreen(){
        var website= binding.inputText.text.toString().trim()

        if(website != "")
        {
            binding.enteredWebsiteNoInputIcon.visibility=View.GONE

            if(!website.contains("https://")){
                website ="https://"+website
            }else{
                val substring = "https://"
                website = website.replace(Regex("(\\Q$substring\\E.*)$substring"), "$substring")
            }

            val intent= Intent(this, ViewCodeActivity::class.java)
            intent.putExtra("customGenerator",1)
            intent.putExtra("barcodeValue", website)
            intent.putExtra("barcodeType", 8)//8 is for url, barcodeType
            intent.putExtra("barcodeFormat",256)// 7 is for text type
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    var popUpWindow: PopupWindow?=null

    var inputErrorIconShown=false

    fun showEnteredWebsiteNoInputPopUp(refView:View){

        inputErrorIconShown=true
        var inflater= applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view= inflater.inflate(R.layout.item_no_input_popup,null)
        popUpWindow= PopupWindow(view,
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT,true)
        popUpWindow?.showAsDropDown(refView,0,0, Gravity.NO_GRAVITY)
        popUpWindow?.isFocusable=false
    }
}