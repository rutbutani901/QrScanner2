package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.appcompat.app.AppCompatActivity
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.R
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.databinding.ActivityCreateContactCodeBinding
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.setGradientInStatusBar
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.setLocale
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.sharedPref
import ezvcard.Ezvcard
import ezvcard.VCard
import java.io.BufferedReader
import java.io.InputStreamReader


class CreateContactCode : AppCompatActivity() {

    lateinit var binding:ActivityCreateContactCodeBinding

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
        binding= ActivityCreateContactCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setGradientInStatusBar(this)

        if(intent.type=="text/x-vcard"){

            try{
                val uri=intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)
                uri?.let {

                    val contentResolver=applicationContext.contentResolver
                    contentResolver.openInputStream(uri)?.use {
                        val bufferedReader= BufferedReader(InputStreamReader(it))
                        val stringBuilder= StringBuilder()
                        var line:String?
                        while (bufferedReader.readLine().also { line=it }!=null){
                            stringBuilder.append(line).append('\n')
                        }
                        val fileContents=stringBuilder.toString()
                        val ezvCard=Ezvcard.parse(fileContents).first()

                        getContactDataFromContactUri(ezvCard)
                    }
                    finish()

                }
            }catch (e:Exception){
                e.printStackTrace()
            }

        }
        if(intent.type=="text/plain"){

            try{
                val value=intent.getStringExtra(Intent.EXTRA_TEXT)

                value?.let {
                    val inputText= value.toString().toString()

                    val intent= Intent(this@CreateContactCode, ViewCodeActivity::class.java)

                    intent.putExtra("customGenerator",1)
                    intent.putExtra("barcodeValue",inputText)
                    intent.putExtra("barcodeType",7)// 7 is for text type
                    intent.putExtra("barcodeFormat",256)// 7 is for text type
                    startActivity(intent)

                    finish()
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
        }

        binding.backButton.setOnClickListener{
            onBackPressed()
        }


        binding.tickButton.setOnClickListener{
gotoNextScreen()

        }
        binding.create.setOnClickListener{
            gotoNextScreen()
        }

        binding.firstName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if(binding.firstName.text.toString().trim()!=""){

                    inputErrorIconShown=false
                    popUpWindow?.dismiss()
                    binding.inputErrorIcon.visibility= View.GONE
                }else{

                    if(!inputErrorIconShown){

                        binding.inputErrorIcon.visibility= View.VISIBLE
                        showEnteredWebsiteNoInputPopUp(binding.firstName)
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })


    }

    fun gotoNextScreen(){
        //if first namr not found and last not found then show error in first name
        //if first namr found and last not found no prob
        //if first namr not found and last  found no prob
        var firstName=binding.firstName.text.toString().trim()
        firstName = firstName.replace(";", "\\;")

        var lastName=binding.lastName.text.toString().trim()
        lastName = lastName.replace(";", "\\;")

        var company=binding.company.text.toString().trim()
        company = company.replace(";", "\\;")

        var jobTitle=binding.jobTitle.text.toString().trim()
        jobTitle = jobTitle.replace(";", "\\;")

        var phoneNumber=binding.phoneNumber.text.toString().trim()
        phoneNumber = phoneNumber.replace(";", "\\;")

        var email=binding.email.text.toString().trim()
        email = email.replace(";", "\\;")

        var streetAddress=binding.streetAddress.text.toString().trim()
        streetAddress = streetAddress.replace(";", "\\;")

        var zipCode=binding.zipCode.text.toString().trim()
        zipCode = zipCode.replace(";", "\\;")

        var city=binding.city.text.toString().trim()
        city = city.replace(";", "\\;")

        var region=binding.region.text.toString().trim()
        region = region.replace(";", "\\;")

        var country=binding.country.text.toString().trim()
        country = country.replace(";", "\\;")

        var website=binding.website.text.toString().trim()
        website = website.replace(";", "\\;")


        if(firstName=="")// || lastName==""
        {
            if(!inputErrorIconShown){

                binding.inputErrorIcon.visibility=View.VISIBLE
                showEnteredWebsiteNoInputPopUp(binding.firstName)
            }
//
        }
        else{// good to go

            var vCardString="BEGIN:VCARD\n" +
                    "VERSION:2.1\n"

            vCardString+= "N:${lastName};${firstName}\n"
//                if(firstName!="") firstName+=" "
            vCardString+= "FN:${firstName} ${lastName}\n"

            if(company!="")   vCardString+= "ORG:${company}\n"
            if(jobTitle!="")   vCardString+= "TITLE:${jobTitle}\n"
            if(phoneNumber!="")   vCardString+= "TEL:${phoneNumber}\n"
            if(email!="")   vCardString+= "EMAIL:${email}\n"

            val addressString=";;${streetAddress};${city};${region};${zipCode};${country}"
            if(addressString!=";;;;;") vCardString+= "ADR:${addressString}\n"
            if(website!="")   vCardString+= "URL:${website}\n"

            vCardString+="END:VCARD"

            val intent= Intent(this, ViewCodeActivity::class.java)
            intent.putExtra("customGenerator",1)
            intent.putExtra("barcodeValue",vCardString)
            intent.putExtra("barcodeType",1)//1 is for contact type bqr code
            intent.putExtra("barcodeFormat",256)// 7 is for text type
            startActivity(intent)

        }
    }

    var popUpWindow: PopupWindow?=null

    var inputErrorIconShown=false

    fun showEnteredWebsiteNoInputPopUp(refView: View){

        inputErrorIconShown=true
        val inflater= applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view= inflater.inflate(R.layout.item_no_input_popup,null)
        popUpWindow= PopupWindow(view,
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT,true)
        popUpWindow?.showAsDropDown(refView,0,0, Gravity.START)
        popUpWindow?.isFocusable=false
    }

    fun getContactDataFromContactUri(ezvCard: VCard){

        var vCardString="BEGIN:VCARD\n" +
                "VERSION:2.1\n"

        var firstName=""
        var lastName=""
        ezvCard.structuredName?.let {
            if(ezvCard.structuredName.given!=null){
                firstName=ezvCard.structuredName.given.trim()

            }
            if(ezvCard.structuredName.family!=null){
                lastName=ezvCard.structuredName.family.trim()
            }
            vCardString+= "N:${lastName};${firstName}\n"
            vCardString+= "FN:${firstName} ${lastName}\n"
        }

        ezvCard.organization?.let {

            ezvCard.organization.values[0]?.let {
                val organisation=ezvCard.organization.values[0].trim()
                if(organisation!="")   vCardString+= "ORG:${organisation}\n"
            }

        }
        if(ezvCard.titles.size!=0){
            for (item in ezvCard.titles){
                val title=item.value.trim()
                vCardString+= "TITLE:${title}\n"
            }
        }


        ezvCard.telephoneNumbers?.let {
            var numbers=ezvCard.telephoneNumbers

            for (item in numbers){
                var number= item.text.trim()
                vCardString+= "TEL:${number}\n"

            }
        }

        ezvCard.emails?.let {
            var emails=ezvCard.emails
            for (item in emails){
                val emailText= item.value.trim()
                vCardString+= "EMAIL:${emailText}\n"
            }
        }

        var addressList=ArrayList<String>()
        ezvCard.addresses?.let {
            var address=ezvCard.addresses

            for (item in address){
                var fullAddress=""

                var streetAddress=""
                var city=""
                var region=""
                var zipCode=""
                var country=""

                item.streetAddress?.let {
                    streetAddress=item.streetAddress
                }
                item.locality?.let {
                    city=item.locality
                }
                item.region?.let {
                    region=item.region
                }
                item.postalCode?.let {
                    zipCode=item.postalCode
                }
                item.country?.let {
                    country=item.country
                }


                var addressString=";;${streetAddress};${city};${region};${zipCode};${country}"
                if(addressString!=";;;;;") vCardString+= "ADR:${addressString}\n"

            }
        }

        if(ezvCard.urls.size!=0){
            for(item in ezvCard.urls){
                var website= item.value
                vCardString+= "URL:${website}\n"
            }

        }

        vCardString+="END:VCARD"

        var intent= Intent(this, ViewCodeActivity::class.java)
        intent.putExtra("customGenerator",1)
        intent.putExtra("barcodeValue",vCardString)
        intent.putExtra("barcodeType",1)//1 is for contact type bqr code
        intent.putExtra("barcodeFormat",256)// 7 is for text type
        startActivity(intent)



    }



    override fun onBackPressed() {
        super.onBackPressed()
    }


}