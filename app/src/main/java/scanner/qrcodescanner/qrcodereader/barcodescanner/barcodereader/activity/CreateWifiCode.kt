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
import android.widget.*
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.R
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.databinding.ActivityCreateWifiCodeBinding
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.databinding.DialogWifiPasswordNotCorrectBinding
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.setGradientInStatusBar
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.setLocale
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.sharedPref
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CreateWifiCode : AppCompatActivity() {

    lateinit var binding:ActivityCreateWifiCodeBinding
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
        binding= ActivityCreateWifiCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setGradientInStatusBar(this)
        createDropdown()

        binding.backButton.setOnClickListener{
            onBackPressed()
        }

        binding.tickButton.setOnClickListener{
           gotoNextScreen()

        }
        binding.create.setOnClickListener{
            gotoNextScreen()
        }

        binding.networkName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if(binding.networkName.text.toString().trim()!=""){

                    inputErrorIconShown=false
                    namePopUpWindow?.dismiss()
                    binding.inputErrorIcon.visibility= View.GONE
                }
//                else{
//
//                    if(!inputErrorIconShown){
//
//                        binding.inputErrorIcon.visibility= View.VISIBLE
//                        inputErrorIconShown=true
//                        showEnteredWebsiteNoInputPopUp(binding.networkName)
//                    }
//                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        binding.password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if(binding.password.text.toString().trim()!=""){

                    passwordInputErrorIconShown=false
                    passwordPopUpWindow?.dismiss()
                    binding.passwordInputErrorIcon.visibility= View.GONE
                }
//                else{
//
//                    if(!inputErrorIconShown){
//
//                        passwordInputErrorIconShown=true
//                        binding.passwordInputErrorIcon.visibility= View.VISIBLE
//                        showEnteredWebsiteNoInputPopUp(binding.password)
//                    }
//                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }

    fun gotoNextScreen(){
        val selected= binding.spinner2.selectedItem.toString()


        var password=binding.password.text.toString()
        var name=binding.networkName.text.toString()


        if(name == "")
        {
            if(!inputErrorIconShown){

                binding.inputErrorIcon.visibility= View.VISIBLE
                inputErrorIconShown=true
                nameNoInputPopup(binding.networkName)
            }
        }
        else
        {
            if(selected == getString(R.string.wpa))
            {
                if(password=="") {

                    if(!passwordInputErrorIconShown){

                        passwordInputErrorIconShown=true
                        binding.passwordInputErrorIcon.visibility= View.VISIBLE
                        passwordNoInputPopUp(binding.password)
                    }

                }
                else if(password.length<8) {

                    encryptionNotCorrectDialog()
                }
                else{

                    passwordInputErrorIconShown=false

                    name = name.replace(":", "\\:").replace(";", "\\;")
                    password = password.replace(":", "\\:").replace(";", "\\;")

                    val wifiConfigStr = "WIFI:S:${name};T:WPA;P:${password};;"

                    val intent= Intent(this, ViewCodeActivity::class.java)
                    intent.putExtra("customGenerator",1)
                    intent.putExtra("barcodeValue",wifiConfigStr)
                    intent.putExtra("barcodeType",9)// 9 is for wifi type
                    intent.putExtra("barcodeFormat",256)// 7 is for text type
                    startActivity(intent)
                }
            }
            else{
                var encrypType=""
                if(selected == getString(R.string.wep)){
                    encrypType="WEP"

                    if(password==""){

                        if(!passwordInputErrorIconShown){

                            passwordInputErrorIconShown=true
                            binding.passwordInputErrorIcon.visibility= View.VISIBLE
                            passwordNoInputPopUp(binding.password)
                        }

                    }else{

                        startViewCodeActivity(name,encrypType,password)
                    }

                }
                else {
                    encrypType="nopass"
                    password=""

                    startViewCodeActivity(name,encrypType,password)
                }

            }
        }
    }

    fun startViewCodeActivity(name:String, encrypType:String, password:String){

        var newName = name.replace(":", "\\:").replace(";", "\\;")
        var newPassword = password.replace(":", "\\:").replace(";", "\\;")


        val wifiConfigStr = "WIFI:S:${newName};T:${encrypType};P:${newPassword};;"

        var intent= Intent(this, ViewCodeActivity::class.java)
        intent.putExtra("customGenerator",1)
        intent.putExtra("barcodeValue",wifiConfigStr)
        intent.putExtra("barcodeType",9)// 9 is for wifi type
        intent.putExtra("barcodeFormat",256)// 7 is for text type
        startActivity(intent)
    }




    lateinit var dialogWifiPasswordNotCorrectBinding: DialogWifiPasswordNotCorrectBinding

    fun encryptionNotCorrectDialog() {
        dialogWifiPasswordNotCorrectBinding = DialogWifiPasswordNotCorrectBinding.inflate(layoutInflater)

        var dialog = MaterialAlertDialogBuilder(this,R.style.ThemeOverlay_App_MaterialAlertDialog)
            .setView(dialogWifiPasswordNotCorrectBinding.root)
            .setOnCancelListener { }
            .show()
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)

        dialog.window?.setBackgroundDrawable(resources.getDrawable(R.drawable.shape_rounded_corners))

        dialogWifiPasswordNotCorrectBinding.ok.setOnClickListener {

            dialog.cancel()
        }
    }




    fun createDropdown()
    {
        val spinner = binding.spinner2 as Spinner
        val encryptionType = arrayOf(
            getString(R.string.wpa),
            getString(R.string.wep),
            getString(R.string.noneWifi)
        )
        val langAdapter = ArrayAdapter<CharSequence>(this, R.layout.spinner_text, encryptionType)
        langAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown)
        binding.spinner2.setAdapter(langAdapter)


        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()

                if(selectedItem == getString(R.string.noneWifi))
                {
                    binding.password.visibility= View.GONE

                    passwordInputErrorIconShown=false
                    binding.passwordInputErrorIcon.visibility= View.GONE
                    passwordPopUpWindow?.dismiss()

                }else
                {
                    binding.password.visibility= View.VISIBLE
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

    }

    var namePopUpWindow: PopupWindow?=null
    var passwordPopUpWindow: PopupWindow?=null

    var inputErrorIconShown=false
    var passwordInputErrorIconShown=false

    fun nameNoInputPopup(refView: View){


        val inflater= applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view= inflater.inflate(R.layout.item_no_input_popup,null)
        namePopUpWindow= PopupWindow(view,
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT,true)
        namePopUpWindow?.showAsDropDown(refView,0,0, Gravity.END)
        namePopUpWindow?.isFocusable=false
    }

    fun passwordNoInputPopUp(refView: View){


        val inflater= applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view= inflater.inflate(R.layout.item_no_input_popup,null)
        passwordPopUpWindow= PopupWindow(view,
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT,true)
        passwordPopUpWindow?.showAsDropDown(refView,0,0, Gravity.END)
        passwordPopUpWindow?.isFocusable=false
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}