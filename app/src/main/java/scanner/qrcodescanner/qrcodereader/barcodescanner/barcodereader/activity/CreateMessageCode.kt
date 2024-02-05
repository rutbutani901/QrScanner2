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
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.databinding.ActivityCreateMessageCodeBinding
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.setGradientInStatusBar
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.setLocale
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.sharedPref

class CreateMessageCode : AppCompatActivity() {

    lateinit var binding:ActivityCreateMessageCodeBinding
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
        binding= ActivityCreateMessageCodeBinding.inflate(layoutInflater)
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
        binding.phoneInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if(binding.phoneInput.text.toString().trim()!=""){

                    inputErrorIconShown=false
                    popUpWindow?.dismiss()
                    binding.enteredPhoneNoInputIcon.visibility=View.GONE
                }else{

                    if(!inputErrorIconShown){

                        inputErrorIconShown=true
                        binding.enteredPhoneNoInputIcon.visibility=View.VISIBLE
                        noInputPopUp(binding.phoneInput)
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }

    fun gotoNextScreen(){
        val phone=binding.phoneInput.text.toString().trim()

        if(phone != "")
        {
            // create qr
            val message=binding.messageInput.text.toString().trim()

            val barcodeValue="sms:${phone}?body=${message}"

            val intent= Intent(this@CreateMessageCode, ViewCodeActivity::class.java)
            intent.putExtra("customGenerator",1)
            intent.putExtra("barcodeValue",barcodeValue)
            intent.putExtra("barcodeType",6)// 6 is for sms type
            intent.putExtra("barcodeFormat",256)// 7 is for text type
            startActivity(intent)
        }else
        {
            if(!inputErrorIconShown){

                inputErrorIconShown=true
                binding.enteredPhoneNoInputIcon.visibility=View.VISIBLE
                noInputPopUp(binding.phoneInput)
            }
        }
    }

    var popUpWindow: PopupWindow?=null

    var inputErrorIconShown=false

    fun noInputPopUp(refView: View){


        var inflater= applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view= inflater.inflate(R.layout.item_no_input_popup,null)
        popUpWindow= PopupWindow(view,
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT,true)
        popUpWindow?.showAsDropDown(refView,0,0, Gravity.END)
        popUpWindow?.isFocusable=false
    }


    override fun onBackPressed() {
        super.onBackPressed()
    }
}