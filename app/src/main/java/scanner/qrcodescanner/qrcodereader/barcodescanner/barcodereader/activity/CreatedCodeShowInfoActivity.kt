package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.activity

import android.app.Activity
import android.content.*
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract
import android.provider.ContactsContract
import android.text.format.DateFormat
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import biweekly.Biweekly
import biweekly.ICalendar
import biweekly.component.VEvent
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.R
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.databinding.ActivityCreatedCodeShowInfoBinding
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.databinding.DialogNoteBinding
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.setGradientInStatusBar
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.setLocale
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.sharedPref
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.modelClass.BarcodeFunctionalityModelClass
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.modelClass.ContactFieldsModelClass
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.modelClass.ExportDataModel
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.modelClass.WifiModelClass
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.util.Constants
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.util.WifiConnector
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.viewModel.BarcodeListViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import ezvcard.Ezvcard
import ezvcard.VCard
import qrcodescanner.qrcodereader.barcodescanner.barcodereader.dataBase.DataModel
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.adapter.*
import java.net.URLDecoder
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


class CreatedCodeShowInfoActivity : AppCompatActivity() {

    lateinit var binding:ActivityCreatedCodeShowInfoBinding

    lateinit var barcodeListViewModel: BarcodeListViewModel
    var isFav=false
    var note:String=""
    var favNotesDataUpdated=true
    var barcode: DataModel?=null
    val barcodeFormatHashMap: HashMap<Int, String> = hashMapOf(
        32 to "EAN_13",
        512 to "UPC_A",
        2 to "CODE_39",
        4 to "CODE_93",
        256 to "QR_CODE",
        8 to "CODABAR",
        4096 to "AZTEC",
        1 to "CODE_128",
        16 to "DATA_MATRIX",
        64 to "EAN_8",
        128 to "ITF",
        2048 to "PDF_417",
        1024 to "UPC_E"
    )
    var barcodeInsertedId=-1L


    //    var textToSearchOnWeb=""
    var fullName=""
    var job=""
    var phone=""
    var email=""
    var company=""
    var street=""
    var locality=""
    var region=""
    var zipCode=""
    var url=""
    var country=""
    var poBox=""

    var totalAddress= ArrayList<String>()
    var totalNumber= ArrayList<String>()
    var totalemails= ArrayList<String>()
    var totalUrls= ArrayList<String>()


    var wifiName=""
    var wifiEncrypType=""
    var wifiPassword=""
    var isWifiHidden=""

    var eventTitle=""
    var eventStartDate=""
    var eventEndDate=""
    var eventLocation=""
    var eventDescription=""
    var eventStartYear=0
    var eventStartMonth=0
    var eventStartDay=0
    var eventStartHour=0
    var eventStartMin=0
    var eventEndYear=0
    var eventEndMonth=0
    var eventEndDay=0
    var eventEndHour=0
    var eventEndMin=0

    var geoLocation=""
    var websiteUrl=""



    var formattedBarcodeValue=""


    var barcodeValueType:Int=0

    var creationDateTime:String=""


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
        binding= ActivityCreatedCodeShowInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setGradientInStatusBar(this)



        barcodeListViewModel = ViewModelProvider(this).get(BarcodeListViewModel::class.java)
        barcodeInsertedId=-1
        var barcodeValue:String=""
        var barcodeFormat:Int=0


        barcodeValue = intent.getStringExtra("barcodeValue").toString()
        barcodeFormat = intent.getIntExtra("barcodeFormat", 0)
        barcodeValueType = intent.getIntExtra("barcodeType", 0)
        creationDateTime= intent.getStringExtra("creationDateTime").toString()
        note= intent.getStringExtra("note").toString()
        barcodeInsertedId= intent.getLongExtra("barcodeInsertedId",-1)
        isFav=intent.getBooleanExtra("isFav",false)

        if(sharedPref.isPremiumPurchased){
            binding.pro.visibility=View.GONE
        }else{
            binding.pro.visibility=View.VISIBLE
        }

        var formatText= barcodeFormatHashMap[barcodeFormat]
        if(formatText==null) {
            formatText=getString(R.string.barcodeFormatNotFound)
        }

        if(note != "") {
            binding.notes.visibility = View.VISIBLE
            binding.notes.text = note
        }else{
            binding.notes.visibility=View.GONE
        }


        binding.backButton.setOnClickListener{
            onBackPressed()
        }
//        binding.copy.setOnClickListener{
//            copyToClipBoard()
//        }
//        binding.share.setOnClickListener{
//            shareValue()
//        }
        binding.more.setOnClickListener{
            showMoreFeaturesDialog(binding.more)
        }
        binding.export.setOnClickListener{
            export()
        }
        binding.showCode.setOnClickListener{
            onBackPressed()
        }
        if(barcodeValueType==8) //isfrom website
        {
            Log.d("InElseChecker","no")
            binding.productLogo.visibility= View.GONE
            binding.productDescription.visibility= View.GONE
            binding.productTitle.visibility= View.GONE

            binding.resultLayout.visibility= View.VISIBLE
            binding.recycler.visibility= View.VISIBLE

            binding.title.text=getString(R.string.website)
            binding.resultLogo.setImageResource(R.drawable.ic_website)

            websiteUrl=barcodeValue
            formattedBarcodeValue=websiteUrl

            exportDataList.clear()
            exportDataList.add(ExportDataModel("Website",websiteUrl))
            if(sharedPref.openWebsiteAutomatically)  openWebsite(websiteUrl)

            val list= arrayListOf<String>(
                barcodeValue
            )
            setWebsiteAdapter(list)


            val funcList= ArrayList<BarcodeFunctionalityModelClass>()

            if(barcodeValue.startsWith("https://www.facebook.com/profile.php?id=")){
                binding.title.text=getString(R.string.facebbok)
                binding.resultLogo.setImageResource(R.drawable.ic_facebook)
                exportDataList.add(ExportDataModel("Facebook Profile",websiteUrl))

                funcList.add(BarcodeFunctionalityModelClass(getString(R.string.open),R.drawable.open_website_icon,barcodeValue))
                settingAdapter(funcList,barcodeValue)
            }
            else if(barcodeValue.startsWith("https://www.instagram.com/")){
                binding.title.text=getString(R.string.instagram)
                binding.resultLogo.setImageResource(R.drawable.ic_instagram)
                exportDataList.add(ExportDataModel("Instagram Profile",websiteUrl))

                funcList.add(BarcodeFunctionalityModelClass(getString(R.string.open),R.drawable.open_website_icon,barcodeValue))
                settingAdapter(funcList,barcodeValue)
            }
            else if(barcodeValue.startsWith("https://www.twitter.com/")){
                binding.title.text=getString(R.string.twitter)
                binding.resultLogo.setImageResource(R.drawable.ic_twitter)
                exportDataList.add(ExportDataModel("Twitter Profile",websiteUrl))

                funcList.add(BarcodeFunctionalityModelClass(getString(R.string.open),R.drawable.open_website_icon,barcodeValue))
                settingAdapter(funcList,barcodeValue)
            }
            else if(barcodeValue.startsWith("whatsapp://send?phone=")){
                binding.title.text=getString(R.string.whatsapp)
                binding.resultLogo.setImageResource(R.drawable.ic_whatsapp)
                exportDataList.add(ExportDataModel("Whatsapp",barcodeValue))

                funcList.add(BarcodeFunctionalityModelClass(getString(R.string.whatsapp),R.drawable.open_website_icon,barcodeValue))
                settingAdapter(funcList,barcodeValue)
            }
            else if(barcodeValue.startsWith("viber://add?number=")){
                binding.title.text=getString(R.string.viber)
                binding.resultLogo.setImageResource(R.drawable.ic_viber)
                exportDataList.add(ExportDataModel("Viber",websiteUrl))

                funcList.add(BarcodeFunctionalityModelClass(getString(R.string.viber),R.drawable.open_website_icon,barcodeValue))
                settingAdapter(funcList,barcodeValue)
            }
            else{
                binding.title.text=getString(R.string.website)
                binding.resultLogo.setImageResource(R.drawable.ic_website)
                exportDataList.add(ExportDataModel("Website",websiteUrl))
            }
        }
        else if(barcodeValueType==4) //isfrom phone
        {

            binding.productLogo.visibility= View.GONE
            binding.productDescription.visibility= View.GONE
            binding.productTitle.visibility= View.GONE

            binding.resultLayout.visibility= View.VISIBLE
            binding.recycler.visibility= View.VISIBLE

            binding.title.text=getString(R.string.phone)


            binding.resultLogo.setImageResource(R.drawable.ic_phone)

            smsPhone=barcodeValue

            exportDataList.clear()
            exportDataList.add(ExportDataModel("Phone",phone))

            formattedBarcodeValue=phone

            var list= arrayListOf<String>(
                barcodeValue
            )
            setTextAdapter(list)

            val funcList= ArrayList<BarcodeFunctionalityModelClass>()
            funcList.add(BarcodeFunctionalityModelClass(getString(R.string.call),R.drawable.call_icon,smsPhone))
            funcList.add(BarcodeFunctionalityModelClass(getString(R.string.addContact),R.drawable.add_contact_icon_with_person))
            settingAdapter(funcList,barcodeValue)
        }
        else if(barcodeValueType==1)// is for contact
        {
            exportDataList.clear()
            binding.productLogo.visibility= View.GONE
            binding.productDescription.visibility= View.GONE
            binding.productTitle.visibility= View.GONE


            binding.recycler.visibility= View.VISIBLE


            binding.title.text=getString(R.string.contact)


            binding.resultLogo.setImageResource(R.drawable.ic_contact)

            val funcList=generateRedableContact(barcodeValue)
            if(funcList.isNotEmpty()){

                val fieldsList=ArrayList<ContactFieldsModelClass>()
                if(fullName!="") {
                    fieldsList.add(ContactFieldsModelClass(R.drawable.ic_person_contact,fullName))
                    exportDataList.add(ExportDataModel("FullName",fullName))
                }
                if(company!="") {
                    fieldsList.add(ContactFieldsModelClass(R.drawable.ic_organization,company))
                    exportDataList.add(ExportDataModel("Organization",company))
                }
                if(job!="")  {
                    fieldsList.add(ContactFieldsModelClass(R.drawable.ic_job,job))
                    exportDataList.add(ExportDataModel("Job",job))
                }
                if(totalAddress.isNotEmpty()){
                    var counter=0
                    for(item in totalAddress){
                        fieldsList.add(ContactFieldsModelClass(R.drawable.location_icon,item))
                        exportDataList.add(ExportDataModel("Address ${counter}",item))
                        counter++
                    }
                }
                if(totalNumber.isNotEmpty()){
                    var counter=0
                    for(item in totalNumber){
                        fieldsList.add(ContactFieldsModelClass(R.drawable.call_icon,item))
                        exportDataList.add(ExportDataModel("Phone ${counter}",item))
                        counter++
                    }
                }
                if(totalemails.isNotEmpty()){
                    var counter=0
                    for(item in totalemails){
                        fieldsList.add(ContactFieldsModelClass(R.drawable.email_icon,item))
                        exportDataList.add(ExportDataModel("Email ${counter}",item))
                        counter++
                    }
                }
                if(totalUrls.isNotEmpty()){
                    var counter=0
                    for(item in totalUrls){
                        fieldsList.add(ContactFieldsModelClass(R.drawable.website_icon,item))
                        exportDataList.add(ExportDataModel("Url ${counter}",item))
                        counter++
                    }
                }

                setContactAdapter(fieldsList)


                settingAdapter(funcList,barcodeValue)
            }else{

            }
        }
        else if(barcodeValueType==9)// for wifi
        {
            binding.productLogo.visibility= View.GONE
            binding.productDescription.visibility= View.GONE
            binding.productTitle.visibility= View.GONE

            binding.recycler.visibility= View.VISIBLE


            binding.title.text=getString(R.string.newWifi)


            binding.resultLogo.setImageResource(R.drawable.ic_wifi)

            retrieveWifiData(barcodeValue)

            val fieldsList=ArrayList<WifiModelClass>()
            val wifiFuncList= ArrayList<BarcodeFunctionalityModelClass>()

            wifiFuncList.add(BarcodeFunctionalityModelClass(getString(R.string.connectToWifi),R.drawable.wifi_icon))

            if(!wifiName.equals("")) {
                fieldsList.add(WifiModelClass(getString(R.string.wifi),wifiName))
                wifiFuncList.add(BarcodeFunctionalityModelClass(getString(R.string.copyNetWorkNmae),R.drawable.copy_clipboard_icon_in_white))

                exportDataList.add(ExportDataModel("Wi-Fi",wifiName))
            }
            if(!wifiPassword.equals("")) {
                fieldsList.add(WifiModelClass(getString(R.string.wifiPassword),wifiPassword))
                wifiFuncList.add(BarcodeFunctionalityModelClass(getString(R.string.copyPassword),R.drawable.copy_clipboard_icon_in_white))
                exportDataList.add(ExportDataModel("Password",wifiPassword))
            }
            if(!wifiEncrypType.equals("")) {
                fieldsList.add(WifiModelClass(getString(R.string.encryption),wifiEncrypType))
                exportDataList.add(ExportDataModel("EncryptionType",wifiEncrypType))
            }
            if(!isWifiHidden.equals("")){
                fieldsList.add(WifiModelClass(getString(R.string.isHidden),isWifiHidden))
                exportDataList.add(ExportDataModel("IsWifiHidden",isWifiHidden))
            }


            setWifiAdapter(fieldsList)
            settingAdapter(wifiFuncList,barcodeValue)

        }
        else if(barcodeValueType==11)// event
        {
            binding.productLogo.visibility= View.GONE
            binding.productDescription.visibility= View.GONE
            binding.productTitle.visibility= View.GONE

            binding.recycler.visibility= View.VISIBLE

            binding.title.text=getString(R.string.event)


            binding.resultLogo.setImageResource(R.drawable.ic_event)

            val funcList= ArrayList<BarcodeFunctionalityModelClass>()
            funcList.add(BarcodeFunctionalityModelClass(getString(R.string.addEvent),R.drawable.event_icon))
            settingAdapter(funcList,barcodeValue)

            val outPutstring=extractCalender(barcodeValue)
            formattedBarcodeValue=outPutstring


            val fieldsList=ArrayList<WifiModelClass>()

            exportDataList.clear()
            if(!eventTitle.equals("")){
                fieldsList.add(WifiModelClass(getString(R.string.eventTitle),eventTitle))
                exportDataList.add(ExportDataModel("EventTitle",eventTitle))
            }
            if(!eventStartDate.equals("")){
                fieldsList.add(WifiModelClass(getString(R.string.eventStartDate),eventStartDate))
                exportDataList.add(ExportDataModel("EventStartDate",eventStartDate))
            }
            if(!eventEndDate.equals("")){
                fieldsList.add(WifiModelClass(getString(R.string.eventEndDate),eventEndDate))
                exportDataList.add(ExportDataModel("EventEndDate",eventEndDate))
            }
            if(!eventLocation.equals("")){
                fieldsList.add(WifiModelClass(getString(R.string.eventLocation),eventLocation))
                exportDataList.add(ExportDataModel("EventLocation",eventLocation))
            }
            if(!eventDescription.equals("")) {
                fieldsList.add(WifiModelClass(getString(R.string.eventDescription),eventDescription))
                exportDataList.add(ExportDataModel("EventDescription",eventDescription))
            }

            setWifiAdapter(fieldsList)

        }
        else if(barcodeValueType==7)// for text type
        {
            binding.productLogo.visibility= View.GONE
            binding.productDescription.visibility= View.GONE
            binding.productTitle.visibility= View.GONE

            binding.recycler.visibility= View.VISIBLE


            binding.title.text=getString(R.string.text)


            binding.resultLogo.setImageResource(R.drawable.ic_text)

            barcode?.let {
                val format=barcode!!.barFormat
                if(format==16 || format==2048 || format==4096 || format==1  || format==4 || format==2 || format==8
                    || format==128 ){
//                    binding.title.text=getString(R.string.barCode)
                }
            }
//            textToSearchOnWeb=barcodeValue
            formattedBarcodeValue=barcodeValue

            exportDataList.clear()
            exportDataList.add(ExportDataModel("Text",barcodeValue))
            var list= arrayListOf<String>(
                barcodeValue
            )
            setTextAdapter(list)

            val funcList= ArrayList<BarcodeFunctionalityModelClass>()
            funcList.add(BarcodeFunctionalityModelClass(getString(R.string.google),R.drawable.open_website_icon))
            settingAdapter(funcList,barcodeValue)
        }
        else if(barcodeValueType==2)// for EMAIL type
        {
            binding.productLogo.visibility= View.GONE
            binding.productDescription.visibility= View.GONE
            binding.productTitle.visibility= View.GONE

            binding.recycler.visibility= View.VISIBLE

            binding.title.text=getString(R.string.newEmail)


            binding.resultLogo.setImageResource(R.drawable.ic_mail)


            getDataFromEmailTypeFormat(barcodeValue)

//            formattedBarcodeValue="Email:- "+emailAddress+ "\n"+
//                    "Subject:- "+emailSubject+"\n"+
//                    "Message:- "+emailMessage

            exportDataList.clear()
            formattedBarcodeValue=""
            val fieldsList=ArrayList<WifiModelClass>()

            if(!emailAddress.equals("")) {
                fieldsList.add(WifiModelClass(getString(R.string.address),emailAddress))
                formattedBarcodeValue +="Email:- "+emailAddress+ "\n"
                exportDataList.add(ExportDataModel("Email Address",emailAddress))
            }
            if(!emailSubject.equals("")) {
                fieldsList.add(WifiModelClass(getString(R.string.subject),emailSubject))
                formattedBarcodeValue +="Subject:- "+emailSubject+"\n"
                exportDataList.add(ExportDataModel("Subject",emailSubject))
            }
            if(!emailMessage.equals("")) {
                fieldsList.add(WifiModelClass(getString(R.string.body),emailMessage))
                formattedBarcodeValue +="Message:- "+emailMessage
                exportDataList.add(ExportDataModel("Message",emailMessage))
            }

            setWifiAdapter(fieldsList)


            if(emailAddress!=""){
                val funcList= ArrayList<BarcodeFunctionalityModelClass>()
                funcList.add(BarcodeFunctionalityModelClass(getString(R.string.sendEmail),R.drawable.email_icon))
                settingAdapter(funcList,barcodeValue)
            }
        }
        else if(barcodeValueType==6)// for sms type
        {
            binding.productLogo.visibility= View.GONE
            binding.productDescription.visibility= View.GONE
            binding.productTitle.visibility= View.GONE

            binding.recycler.visibility= View.VISIBLE

            binding.title.text=getString(R.string.message)


            binding.resultLogo.setImageResource(R.drawable.ic_message)


            getValueFromSms(barcodeValue)


            val fieldsList=ArrayList<ContactFieldsModelClass>()
            exportDataList.clear()
            formattedBarcodeValue=""
            if(smsPhone!=""){
                formattedBarcodeValue+="Phone:- ${smsPhone}\n"
                exportDataList.add(ExportDataModel("Phone",smsPhone))


                fieldsList.add(ContactFieldsModelClass(R.drawable.call_icon,smsPhone))
                val funcList= ArrayList<BarcodeFunctionalityModelClass>()
                funcList.add(BarcodeFunctionalityModelClass(getString(R.string.sendSms),R.drawable.send_sms_icon))
                funcList.add(BarcodeFunctionalityModelClass(getString(R.string.addContact),R.drawable.add_contact_icon_with_person))

                settingAdapter(funcList,barcodeValue)
            }
            if(smsMessage!=""){
                formattedBarcodeValue+="Message:- ${smsMessage}"
                exportDataList.add(ExportDataModel("Message",smsMessage))

                fieldsList.add(ContactFieldsModelClass(R.drawable.email_icon,smsMessage))
            }
            setContactAdapter(fieldsList)
        }
        else if(barcodeValueType==10)// for geo type
        {
            binding.productLogo.visibility= View.GONE
            binding.productDescription.visibility= View.GONE
            binding.productTitle.visibility= View.GONE

            binding.recycler.visibility= View.VISIBLE

            binding.title.text=getString(R.string.location)


            binding.resultLogo.setImageResource(R.drawable.ic_location)


            try{

                val latitudeEndIndex = barcodeValue.indexOf(",")
                val latitude = barcodeValue.substring(4, latitudeEndIndex)
                val longitude = barcodeValue.substringAfter(",")

                geoLocation = "${latitude},${longitude}"

                formattedBarcodeValue="Latitude:- "+latitude+ "\n"+ "Longitude:- "+longitude

                exportDataList.clear()
                exportDataList.add(ExportDataModel("Latitude",latitude))
                exportDataList.add(ExportDataModel("Longitude",longitude))

                val fieldsList=ArrayList<WifiModelClass>()


                if(geoLocation!=""){
                    fieldsList.add(WifiModelClass("Latitude",latitude))
                    fieldsList.add(WifiModelClass("Longitude",longitude))
                    val funcList = ArrayList<BarcodeFunctionalityModelClass>()
                    funcList.add(
                        BarcodeFunctionalityModelClass(getString(R.string.showLocation), R.drawable.current_location_icon,
                            geoLocation
                        )
                    )
                    settingAdapter(funcList, barcodeValue)
                }
                setWifiAdapter(fieldsList)


            }catch (e:Exception){
                e.printStackTrace()
            }

        }
        else if(barcodeValueType==5)//product
        {
            binding.productLogo.visibility= View.GONE
            binding.productDescription.visibility= View.GONE
            binding.productTitle.visibility= View.GONE

            binding.recycler.visibility= View.VISIBLE

            binding.title.text=getString(R.string.product)


            binding.resultLogo.setImageResource(R.drawable.ic_product)


            val fieldsList=ArrayList<WifiModelClass>()
            fieldsList.add(WifiModelClass(getString(R.string.barcodeValue),barcodeValue))


            var type=""
            barcode?.let {
                val format=barcode!!.barFormat

                if(format==32 || format==64){
                    fieldsList.add(WifiModelClass(getString(R.string.barcodeType),"EAN"))
                    type="EAN"

                }
                if(format==1024 || format==512){
                    fieldsList.add(WifiModelClass(getString(R.string.barcodeType),"UPC"))
                    type="UPC"
                }
            }
            formattedBarcodeValue="barcodeValue: ${barcodeValue}\nbarcode type: ${type}"

            exportDataList.clear()
            exportDataList.add(ExportDataModel("Barcode Value",barcodeValue))
            exportDataList.add(ExportDataModel("Barcode Type:",type))


            setWifiAdapter(fieldsList)
            val funcList= ArrayList<BarcodeFunctionalityModelClass>()
            Constants.productUrlList.forEach{

                funcList.add(BarcodeFunctionalityModelClass(it.title,R.drawable.open_website_icon,it.url))

            }
            settingAdapter(funcList,barcodeValue)

//            val funcList= ArrayList<BarcodeFunctionalityModelClass>()
//            funcList.add(BarcodeFunctionalityModelClass(getString(R.string.google),R.drawable.open_website_icon))
//            funcList.add(BarcodeFunctionalityModelClass(getString(R.string.searchProductOnAmazon),R.drawable.open_website_icon))
//            funcList.add(BarcodeFunctionalityModelClass(getString(R.string.searchProductOnEbay),R.drawable.open_website_icon))
//            funcList.add(BarcodeFunctionalityModelClass(getString(R.string.searchProductOnFoodFacts),R.drawable.open_website_icon))
//            funcList.add(BarcodeFunctionalityModelClass(getString(R.string.searchProductOnWallmart),R.drawable.open_website_icon))
//
//            settingAdapter(funcList,barcodeValue)
        }
        else{
            Log.d("InElseChecker","yes")
            binding.productLogo.visibility= View.GONE
            binding.productDescription.visibility= View.GONE
            binding.productTitle.visibility= View.GONE

            binding.recycler.visibility= View.VISIBLE
            binding.title.text=getString(R.string.text)
            binding.resultLogo.setImageResource(R.drawable.ic_website)
            barcode?.let {
                val format=barcode!!.barFormat
                if(format==16 || format==2048 || format==4096 || format==1  || format==4 || format==2 || format==8
                    || format==128 ){
//                    binding.title.text=getString(R.string.barCode)
                }
            }
//            textToSearchOnWeb=barcodeValue
            formattedBarcodeValue=barcodeValue

            var list= arrayListOf<String>(
                barcodeValue
            )
            setTextAdapter(list)
            exportDataList.clear()
            exportDataList.add(ExportDataModel("Barcode Value",barcodeValue))

            val funcList= ArrayList<BarcodeFunctionalityModelClass>()
            funcList.add(BarcodeFunctionalityModelClass(getString(R.string.google),R.drawable.open_website_icon))
            settingAdapter(funcList,barcodeValue)

        }

    }
    val exportDataList=ArrayList<ExportDataModel>()
    fun export(){

        if(note!=""){
            exportDataList.add(ExportDataModel("Note",note))
        }
        exportDataList.add(ExportDataModel("Time",curentDataTime()))

        val intent= Intent(this, ExportActivity::class.java)
        intent.putExtra("data",exportDataList)
        startActivity(intent)
    }


    fun manipulateBarcodeValue(barcodeValue: String):String
    {
        val titleStartIndex=barcodeValue.indexOf("SUMMARY:")+8
        val titleEndIndex=barcodeValue.indexOf("\n",titleStartIndex)
        val title=barcodeValue.substring(titleStartIndex,titleEndIndex)// getting title completety correct

        val preString="BEGIN:VCALENDAR\n"+
                "VERSION:2.0\n"+
                "PRODID:${title}\n"+
                barcodeValue +
                "END:VCALENDAR"

        return preString
    }

    fun extractCalender( inputString: String):String
    {
        var barcodeValue=inputString

        if(!barcodeValue.startsWith("BEGIN:VCALENDAR")){
            if(barcodeValue.startsWith("BEGIN:VEVENT")){

                var changeInputString= manipulateBarcodeValue(barcodeValue)
                if(!changeInputString.isEmpty()){
                    barcodeValue=changeInputString
                }
            }else{
                return barcodeValue
            }
        }

        var finalOutputSting=""
        try{
            val ical: ICalendar = Biweekly.parse(barcodeValue).first()
            val event: VEvent = ical.getEvents().get(0)

            var dateStart=""
            event.dateStart?.let {
                dateStart= event.dateStart.value.toString()
            }

            var dateEnd=""
            event.dateEnd?.let {
                dateEnd = event.dateEnd.value.toString()

            }
            event.location?.let {
                eventLocation = event.location.value.toString()
            }
            event.description?.let {
                eventDescription = event.description.value.toString()
            }
            val pattern = Regex("(?<=PRODID:)[^\n\r]+")

            try{
                val matchResult = pattern.find(barcodeValue)
                eventTitle = matchResult?.value.toString()
            }catch (e:Exception) {
                e.printStackTrace()
                // MATCH NOT FOUND
            }
            val utc = TimeZone.getTimeZone("UTC")
            val sourceFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US)
            var destFormat= SimpleDateFormat()
            if(!dateStart.contains("00:00:00")) {
                destFormat = SimpleDateFormat("dd-MMM-yyyy HH:mm aa")
            }else{
                destFormat = SimpleDateFormat("dd-MMM-yyyy")
            }

            sourceFormat.timeZone = utc
            val convertedDateStart = sourceFormat.parse(dateStart)


            convertedDateStart?.let {
                calculateParametersToAddInCalernder(convertedDateStart,true)
                eventStartDate=destFormat.format(convertedDateStart)
            }

            val convertedDateEnd = sourceFormat.parse(dateEnd)
            convertedDateEnd?.let {

                calculateParametersToAddInCalernder(convertedDateEnd,false)
                eventEndDate=destFormat.format(convertedDateEnd)
            }

            if(!eventTitle.equals("")) finalOutputSting+="EventTitle:- "+eventTitle+"\n"
            if(!eventStartDate.equals("")) finalOutputSting+="EventStartDate:- "+eventStartDate+"\n"
            if(!eventEndDate.equals("")) finalOutputSting+="EventEndDate:- "+eventEndDate+"\n"
            if(!eventLocation.equals("")) finalOutputSting+="EventLocation:- "+eventLocation+"\n"
            if(!eventDescription.equals("")) finalOutputSting+="EventDecription:- "+eventDescription+"\n"

        }catch (e:Exception) {
            e.printStackTrace()
        }
        return finalOutputSting
    }

    fun calculateParametersToAddInCalernder(eventDates: Date, isStart:Boolean)
    {

        val formatter = SimpleDateFormat("dd");
        val dateString = formatter.format( eventDates);
        if(isStart) eventStartDay=dateString.toInt()
        else        eventEndDay=dateString.toInt()


        val formatterM = SimpleDateFormat("MM");
        val dateStringM = formatterM.format( eventDates);

        if(isStart) eventStartMonth=dateStringM.toInt()
        else        eventEndMonth=dateStringM.toInt()

        val formatterY = SimpleDateFormat("yyyy");
        val dateStringY = formatterY.format( eventDates);
        if(isStart) eventStartYear=dateStringY.toInt()
        else        eventEndYear=dateStringY.toInt()

        val formatterH = SimpleDateFormat("HH");
        val dateStringH = formatterH.format( eventDates);
        if(isStart) eventStartHour=dateStringH.toInt()
        else        eventEndHour=dateStringH.toInt()

        val formatterm = SimpleDateFormat("mm");
        val dateStringm = formatterm.format( eventDates);
        if(isStart) eventStartMin=dateStringm.toInt()
        else        eventEndMin=dateStringm.toInt()

    }

    fun addToCalender()
    {
        val startDate = Calendar.getInstance()
        startDate.set(Calendar.YEAR, eventStartYear)
        startDate.set(Calendar.MONTH, Calendar.MARCH)
        startDate.set(Calendar.DAY_OF_MONTH, eventStartDay)
        startDate.set(Calendar.HOUR_OF_DAY, eventStartHour)
        startDate.set(Calendar.MINUTE, eventStartMin)
        startDate.set(Calendar.SECOND, 0)

        val endDate = Calendar.getInstance()
        endDate.set(Calendar.YEAR, eventEndYear)
        endDate.set(Calendar.MONTH, Calendar.MARCH)
        endDate.set(Calendar.DAY_OF_MONTH, eventEndDay)
        endDate.set(Calendar.HOUR_OF_DAY, eventEndHour)
        endDate.set(Calendar.MINUTE, eventEndMin)
        endDate.set(Calendar.SECOND, 0)

        val intent = Intent(Intent.ACTION_INSERT)
        intent.type = "vnd.android.cursor.item/event"
        intent.putExtra(CalendarContract.Events.TITLE, "Title")
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, "Location")
        intent.putExtra(CalendarContract.Events.DESCRIPTION, "Description")
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startDate.timeInMillis)
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endDate.timeInMillis)//endDate.timeInMillis

        startActivity(intent)

    }


    fun showMoreFeaturesDialog(buttonView:View){
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val popupView: View = inflater.inflate(R.layout.more_function_created_code_show_info, null)

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
        popupView.findViewById<LinearLayout>(R.id.copy).setOnClickListener{
            popupWindow.dismiss()
            copyToClipBoard()

        }
        popupView.findViewById<LinearLayout>(R.id.collect).setOnClickListener{
            popupWindow.dismiss()
            performFavTask()
        }
        popupView.findViewById<LinearLayout>(R.id.share).setOnClickListener{
            popupWindow.dismiss()
           shareValue()
        }
    }

    fun performFavTask(){
        isFav=!isFav
        if(barcodeInsertedId.toInt()!=-1){
            barcodeListViewModel.updateFav(this,barcodeInsertedId.toInt(), isFav)
        }

//        if(barcode==null) {
//            favNotesDataUpdated=false
//        }else {
//            if(barcodeInsertedId.toInt()!=-1){
//                barcodeListViewModel.updateFav(this,barcodeInsertedId.toInt(), isFav)
//            }
//        }
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

            if(barcode==null)
            {
                favNotesDataUpdated=false

            }else {

                if(barcodeInsertedId.toInt()!=-1){

                    barcodeListViewModel.updateNotes(this,barcodeInsertedId.toInt(), note)
                }
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


    fun shareValue(){

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, formattedBarcodeValue)
        startActivity(intent)
    }
    fun copyToClipBoard(){

        val clipboard: ClipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(getString(R.string.barcodeValueCopied), formattedBarcodeValue)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, "${getString(R.string.copied)}:${formattedBarcodeValue}", Toast.LENGTH_SHORT).show()
    }

    fun getDataFromEmailTypeFormat(barcodeValue: String) {

        val emailString = barcodeValue

        val patternList=ArrayList<Pattern>()
        patternList.add(Pattern.compile("MATMSG:TO:(.*?);SUB:(.*?);BODY:(.*?);;"))
        patternList.add(Pattern.compile("MATMSG:TO:(.*?);BODY:(.*?);SUB:(.*?);;"))
        patternList.add(Pattern.compile("MATMSG:SUB:(.*?);TO:(.*?);BODY:(.*?);;"))
        patternList.add(Pattern.compile("MATMSG:SUB:(.*?);BODY:(.*?);TO:(.*?);;"))
        patternList.add(Pattern.compile("MATMSG:BODY:(.*?);TO:(.*?);SUB:(.*?);;"))
        patternList.add(Pattern.compile("MATMSG:BODY:(.*?);SUB:(.*?);TO:(.*?);;"))

        patternList.add(Pattern.compile("mailto:(.*?)?subject=(.*?)&body=(.*?)"))

        for(item in patternList)
        {
            val matcher: Matcher =item.matcher(emailString)
            if(matcher.find())
            {
                if(item.toString().startsWith("mailto")) {
                    getEmailFromEncoder(barcodeValue)
                }else{
                    emailMatchFound(matcher)
                }
                break
            }
        }
    }

    var smsPhone=""
    var smsMessage=""

    fun getValueFromSms(barcodeValue: String)
    {
        if(barcodeValue.startsWith("SMSTO:"))
        {
            val pattern = Pattern.compile("SMSTO:(\\d+):(.+)")
            val matcher = pattern.matcher(barcodeValue)
            smsPhone = if (matcher.matches()) matcher.group(1) else ""
            smsMessage = if (matcher.matches()) matcher.group(2) else ""

        }else{

            smsPhone = barcodeValue.substringAfter("sms:").substringBefore("?")
            phone=smsPhone
            val bodyStart = barcodeValue.indexOf("body=") + 5
            smsMessage = URLDecoder.decode(barcodeValue.substring(bodyStart), "UTF-8")

        }


    }

    fun sendSms()
    {
        val smsIntent = Intent(Intent.ACTION_SENDTO)
        smsIntent.data = Uri.parse("smsto:$smsPhone")
        smsIntent.putExtra("sms_body", smsMessage)
        startActivity(smsIntent)
    }


    var emailAddress=""
    var emailSubject=""
    var emailMessage=""

    fun getEmailFromEncoder(barcodeValue: String)
    {
        emailAddress = barcodeValue.substringAfter("mailto:").substringBefore("?")

        // Extract the subject and decode it
        val subjectStart = barcodeValue.indexOf("subject=") + 8
        val subjectEnd = barcodeValue.indexOf("&", startIndex = subjectStart)
        emailSubject = URLDecoder.decode(barcodeValue.substring(subjectStart, subjectEnd), "UTF-8")

        // Extract the body and decode it
        val bodyStart = barcodeValue.indexOf("body=") + 5
        emailMessage = URLDecoder.decode(barcodeValue.substring(bodyStart), "UTF-8")

    }

    fun emailMatchFound(matcher: Matcher)
    {
        emailAddress = ""
        emailSubject = ""
        emailMessage = ""
        emailAddress = matcher.group(1)?.toString().toString() // Wi-Fi network name
        emailAddress = emailAddress.substring(0, emailAddress.length)

        emailSubject = matcher.group(2)?.toString().toString()// Wi-Fi network type
        emailMessage = matcher.group(3)?.toString().toString()// Wi-Fi network password

        emailAddress=emailAddress.replace("\\;", ";")
        emailSubject =emailSubject.replace("\\;", ";")
        emailMessage =emailMessage.replace("\\;", ";")
    }

    fun generateRedableContact(inputString:String):ArrayList<BarcodeFunctionalityModelClass>
    {
        val contactList=ArrayList<BarcodeFunctionalityModelClass>()

        val vcard: VCard? = Ezvcard.parse(inputString).first()

        vcard?.let {
            contactList.add(BarcodeFunctionalityModelClass(getString(R.string.addContact),R.drawable.contact_icon))

            var fullNameFound=false
            vcard.formattedName?.let {
                it.value?.let {
                    fullName = vcard.formattedName.value// first name and last name
                    formattedBarcodeValue +="Full Name:-"+fullName+"\n"
                    fullNameFound=true
                }
            }
            if(!fullNameFound){
                vcard.structuredName.family?.let {

                    fullName = vcard.structuredName.family// first name and last name
                    formattedBarcodeValue +="Full Name:-"+fullName+"\n"

                }
            }
            if(vcard.titles.size!=0){
                val jobText:String? = vcard.titles[0].value
                jobText?.let{
                    job=jobText.trim()
                    formattedBarcodeValue+="Job Title:-"+job+"\n"
                }
            }
            var isAddress=false
            vcard.organization?.let {
                val companyText=vcard.organization?.values?.firstOrNull()
                company=  companyText.toString().trim()
                formattedBarcodeValue+="Company:-"+company+"\n"
            }

            totalAddress.clear()
            vcard.addresses?.let {

                for(item in vcard.addresses){
                    var address=""
                    if(item.poBox == null) {

                        item.streetAddress?.let {
                            street=item.streetAddress.trim()
                            address+="${street},"
                        }
                        item.region?.let {
                            region=item.region.trim()
                            address+="${region},"
                        }
                        item.locality?.let {
                            locality=item.locality.trim()
                            address+="${locality},"
                        }

                        item.postalCode?.let {
                            zipCode=item.postalCode.trim()
                            address+="${zipCode},"
                        }
                        item.country?.let {
                            country=item.country.trim()
                            address+="${country},"
                        }

                        if(address != ""){
                            isAddress=true
                            totalAddress.add(address.trim())

                            formattedBarcodeValue+="Address:-"+address.trim()+"\n"
                        }
                    }
                    else  {

                        isAddress=true
                        poBox=item.poBox
                        formattedBarcodeValue+="Address:-"+poBox+"\n"
                        totalAddress.add(poBox.trim())
                    }
                }
                if(isAddress){
                    contactList.add(BarcodeFunctionalityModelClass(getString(R.string.viewAddress),R.drawable.current_location_icon,totalAddress[0]))
                }
            }
            totalNumber.clear()
            if(vcard.telephoneNumbers.size!=0){

                for(item in vcard.telephoneNumbers){

                    phone= item.text.trim()

                    if(phone != "null") {

                        var title="${getString(R.string.dial)} ${phone}"
                        totalNumber.add(phone)
                        contactList.add(BarcodeFunctionalityModelClass(title,R.drawable.call_icon,phone))
                        formattedBarcodeValue+="Phone:-"+phone+"\n"
                    }
                }
            }
            totalemails.clear()
            if(vcard.emails.size!=0){

                for(item in vcard.emails){

                    email= item.value.trim()

                    if(email != "null") {

                        var title="${getString(R.string.email)} ${email}"
                        totalemails.add(email)
                        contactList.add(BarcodeFunctionalityModelClass(title,R.drawable.email_icon,email))
                        formattedBarcodeValue+="Email:-"+email+"\n"
                    }
                }
            }
            totalUrls.clear()
            if(vcard.urls.size!=0){

                for(item in vcard.urls){

                    val websiteInContact= item.value.trim()

                    if(websiteInContact != "null" && websiteInContact!="") {

                        totalUrls.add(websiteInContact)
                        contactList.add(BarcodeFunctionalityModelClass("${getString(R.string.openWebsite)} ${websiteInContact}",R.drawable.website_icon,websiteInContact))
                        formattedBarcodeValue+="Website:-"+websiteInContact+"\n"
                    }
                }
            }
        }

        return contactList
    }


    fun retrieveWifiData(bValue:String)
    {
        val wifiString = bValue
        val patternList=ArrayList<Pattern>()
        patternList.add(Pattern.compile("WIFI:S:(.*?);T:(.*?);P:(.*?);H:(.*?);"))
        patternList.add(Pattern.compile("WIFI:S:(.*?);P:(.*?);T:(.*?);H:(.*?);"))
        patternList.add(Pattern.compile("WIFI:T:(.*?);S:(.*?);P:(.*?);H:(.*?);"))
        patternList.add(Pattern.compile("WIFI:T:(.*?);P:(.*?);S:(.*?);H:(.*?);"))
        patternList.add(Pattern.compile("WIFI:P:(.*?);T:(.*?);S:(.*?);H:(.*?);"))
        patternList.add(Pattern.compile("WIFI:P:(.*?);S:(.*?);T:(.*?);H:(.*?);"))

        patternList.add(Pattern.compile("WIFI:S:(.*?);T:(.*?);P:(.*?);;"))
        patternList.add(Pattern.compile("WIFI:S:(.*?);P:(.*?);T:(.*?);;"))
        patternList.add(Pattern.compile("WIFI:T:(.*?);S:(.*?);P:(.*?);;"))
        patternList.add(Pattern.compile("WIFI:T:(.*?);P:(.*?);S:(.*?);;"))
        patternList.add(Pattern.compile("WIFI:P:(.*?);T:(.*?);S:(.*?);;"))
        patternList.add(Pattern.compile("WIFI:P:(.*?);S:(.*?);T:(.*?);;"))
        for(item in patternList)
        {
            val matcher: Matcher =item.matcher(wifiString)
            if(matcher.find())
            {
                wifiMatchFound(matcher,bValue)
                break
            }
        }

    }

    fun wifiMatchFound(matcher: Matcher, barcodeValue: String)
    {

        val endTrueString="H:true;;"
        var endFalseString="H:false;;"
        if(barcodeValue.endsWith(endTrueString) || barcodeValue.endsWith(endFalseString)) {

            wifiName = matcher.group(1)?.toString().toString() // Wi-Fi network name
            wifiEncrypType = matcher.group(2)?.toString().toString()// Wi-Fi network type
            wifiPassword = matcher.group(3)?.toString().toString()// Wi-Fi network password
            isWifiHidden = matcher.group(4)?.toString().toString()// Wi-Fi network password

            wifiName = wifiName.replace("\\:", ":").replace("\\;", ";")
            wifiPassword = wifiPassword.replace("\\:", ":").replace("\\;", ";")
        }else {
            wifiName = matcher.group(1)?.toString().toString() // Wi-Fi network name
            wifiEncrypType = matcher.group(2)?.toString().toString()// Wi-Fi network type
            wifiPassword = matcher.group(3)?.toString().toString()// Wi-Fi network password

            wifiName = wifiName.replace("\\:", ":").replace("\\;", ";")
            wifiPassword = wifiPassword.replace("\\:", ":").replace("\\;", ";")

        }

        formattedBarcodeValue="WI-FI Name:- "+wifiName+"\n"+"EncryptionType:- "+wifiEncrypType
        if(!wifiPassword.equals("")) formattedBarcodeValue +="\n"+"Password:- "+wifiPassword
        if(!isWifiHidden.equals("")) formattedBarcodeValue +="\n"+"Hidden:- "+isWifiHidden


    }

    lateinit var websiteAdapter: WebsiteAdapter
    fun setWebsiteAdapter(website: ArrayList<String>) {

        websiteAdapter = WebsiteAdapter(this, website) {website->

            openUrl(website)
        }
        binding.recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recycler.adapter = websiteAdapter

    }


    lateinit var textAdapter: TextAdapter
    fun setTextAdapter(website: ArrayList<String>) {

        textAdapter = TextAdapter(this, website) {

        }
        binding.recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recycler.adapter = textAdapter

    }

    lateinit var contactAdapter: ContactAdapter
    fun setContactAdapter(contactFieldsList: ArrayList<ContactFieldsModelClass>) {

        contactAdapter = ContactAdapter(this, contactFieldsList) {

        }
        binding.recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recycler.adapter = contactAdapter

    }

    lateinit var wifiAdapter: WifiAdapter
    fun setWifiAdapter(wifiList: ArrayList<WifiModelClass>) {

        wifiAdapter = WifiAdapter(this, wifiList) {


        }
        binding.recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recycler.adapter = wifiAdapter

    }


    lateinit var barcodeFuncAdapter: BarcodeFunctionalityAdapter
    fun settingAdapter(barcodeFunList:ArrayList<BarcodeFunctionalityModelClass>, barcodeValue:String) {

        barcodeFuncAdapter= BarcodeFunctionalityAdapter(this,barcodeFunList) {
                holderTitle,holderValue->//holderName

            if(barcodeValueType==5){//product
                openProductUrl(barcodeValue,holderValue)
            }else{
                if(holderTitle == getString(R.string.addContact))
                {
                    addToContact()
                }
                if(holderTitle == getString(R.string.viewAddress))
                {
                    viewAddress(holderValue)
                }
                if(holderTitle == getString(R.string.showLocation))//check this
                {
                    viewAddress(holderValue)
                }
                if(holderTitle.contains(getString(R.string.dial)))//done
                {
                    dial(holderValue)

                }
                if(holderTitle.contains(getString(R.string.email)))//done
                {
                    sendEmail(holderValue)
                }
                if(holderTitle.contains(getString(R.string.openWebsite)))
                {
                    openWebsite(holderValue)
                }
                if(holderTitle.contains(getString(R.string.open)))
                {
                    openWebsite(holderValue)
                }
                if(holderTitle.contains(getString(R.string.whatsapp)))
                {
                    openWhatsApp(holderValue,0)
                }
                if(holderTitle.contains(getString(R.string.viber)))
                {
                    openWhatsApp(holderValue,1)
                }

                if(holderTitle == getString(R.string.connectToWifi))
                {
                    connectToWifi()

                }
                if(holderTitle.equals(getString(R.string.copyPassword)))
                {
                    copyWifiPassword()
                }
                if(holderTitle.equals(getString(R.string.copyNetWorkNmae)))
                {
                    copyNetworkName()
                }
                if(holderTitle.equals(getString(R.string.addEvent)))
                {
                    addToCalender()

                }
                if(holderTitle.equals(getString(R.string.sendEmail)))
                {
                    sendEmailFromEmailQr()
                }
                if(holderTitle.equals(getString(R.string.sendSms)))
                {
                    sendSms()
                }
                if(holderTitle.equals(getString(R.string.call)))
                {
                    dial(holderValue)
                }

                if(holderTitle.equals(getString(R.string.google)))
                {
                    openUrl("https://www.google.com/search?q=$barcodeValue")
                }

                if(holderTitle.equals(getString(R.string.searchProductOnAmazon)))
                {
                    openUrl("https://www.amazon.in/s?k=${barcodeValue}")
                }
                if(holderTitle.equals(getString(R.string.searchProductOnEbay)))
                {
                    openUrl("https://www.ebay.com/sch/i.html?_from=R40&_trksid=p2540003.m570.l1313&_nkw=$barcodeValue")
                }

                if(holderTitle.equals(getString(R.string.searchProductOnFoodFacts)))
                {
                    openUrl("https://world.openfoodfacts.org/product/$barcodeValue")
                }
                if(holderTitle.equals(getString(R.string.searchProductOnWallmart)))
                {
                    openUrl("https://www.walmart.com/search?q=${barcodeValue}")
                }
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
    fun openProductUrl(barcodeValue: String,holderValue: String){

        val url= holderValue.replace("{code}",barcodeValue)
        val intent=Intent(this, OtherSitesActivity::class.java)
        intent.putExtra("link",url)
        startActivity(intent)
    }

    fun sendEmailFromEmailQr()// commented intent is for search on web
    {

        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:") // only email apps should handle this

        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(emailAddress))
        intent.putExtra(Intent.EXTRA_SUBJECT, emailSubject)
        intent.putExtra(Intent.EXTRA_TEXT, emailMessage)
        startActivity(intent)

    }

    fun openUrl(url:String) {

        val intent= Intent(this, OtherSitesActivity::class.java)
        intent.putExtra("link",url)
        startActivity(intent)

    }

    fun addToContact()
    {
        val intent = Intent(ContactsContract.Intents.Insert.ACTION)
        intent.type = ContactsContract.RawContacts.CONTENT_TYPE

        if(!fullName.equals("null") && !fullName.equals("")){

            intent.putExtra(ContactsContract.Intents.Insert.NAME, fullName)
        }
        if(!job.equals("null") && !job.equals("")){// NOT BEEN ADDED

            intent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, job)
        }
        if(!company.equals("null") && !company.equals("")){

            intent.putExtra(ContactsContract.Intents.Insert.COMPANY, company)
        }

        if(totalNumber.size!=0){

            if(totalNumber.size>3){
                intent.putExtra(ContactsContract.Intents.Insert.PHONE, totalNumber[0])
                intent.putExtra(ContactsContract.Intents.Insert.SECONDARY_PHONE, totalNumber[1])
                intent.putExtra(ContactsContract.Intents.Insert.TERTIARY_PHONE, totalNumber[2])
            }else{
                when(totalNumber.size){
                    1->{
                        intent.putExtra(ContactsContract.Intents.Insert.PHONE, totalNumber[0])
                    }
                    2->{
                        intent.putExtra(ContactsContract.Intents.Insert.PHONE, totalNumber[0])
                        intent.putExtra(ContactsContract.Intents.Insert.SECONDARY_PHONE, totalNumber[1])
                    }
                    3->{
                        intent.putExtra(ContactsContract.Intents.Insert.PHONE, totalNumber[0])
                        intent.putExtra(ContactsContract.Intents.Insert.SECONDARY_PHONE, totalNumber[1])
                        intent.putExtra(ContactsContract.Intents.Insert.TERTIARY_PHONE, totalNumber[2])
                    }
                }
            }
        }
        else{
            if(smsPhone!=""){
                intent.putExtra(ContactsContract.Intents.Insert.PHONE, smsPhone)
            }
        }

        if(totalemails.size!=0){

            if(totalemails.size>3){
                intent.putExtra(ContactsContract.Intents.Insert.EMAIL, totalemails[0])
                intent.putExtra(ContactsContract.Intents.Insert.SECONDARY_EMAIL, totalemails[1])
                intent.putExtra(ContactsContract.Intents.Insert.TERTIARY_EMAIL, totalemails[2])
            }else{
                when(totalemails.size){
                    1->{
                        intent.putExtra(ContactsContract.Intents.Insert.EMAIL, totalemails[0])
                    }
                    2->{
                        intent.putExtra(ContactsContract.Intents.Insert.EMAIL, totalemails[0])
                        intent.putExtra(ContactsContract.Intents.Insert.SECONDARY_EMAIL, totalemails[1])
                    }
                    3->{
                        intent.putExtra(ContactsContract.Intents.Insert.EMAIL, totalemails[0])
                        intent.putExtra(ContactsContract.Intents.Insert.SECONDARY_EMAIL, totalemails[1])
                        intent.putExtra(ContactsContract.Intents.Insert.TERTIARY_EMAIL, totalemails[2])
                    }
                }
            }
        }

        if(totalUrls.size!=0){

            val data = ArrayList<ContentValues>()
            val row1 = ContentValues()
            row1.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE)
            row1.put(ContactsContract.CommonDataKinds.Website.URL, totalUrls[0])
            row1.put(ContactsContract.CommonDataKinds.Website.LABEL, "abc")
            row1.put(ContactsContract.CommonDataKinds.Website.TYPE, ContactsContract.CommonDataKinds.Website.TYPE_HOME)
            data.add(row1)
            intent.putExtra(ContactsContract.Intents.Insert.DATA, data)
        }

//        if(!url.equals("null") && !url.equals("")){
//
//            val data = ArrayList<ContentValues>()
//            val row1 = ContentValues()
//            row1.put(ContactsContract.Data.MIMETYPE, Website.CONTENT_ITEM_TYPE)
//            row1.put(Website.URL, url)
//            row1.put(Website.LABEL, "abc")
//            row1.put(Website.TYPE, Website.TYPE_HOME)
//            data.add(row1)
//            intent.putExtra(ContactsContract.Intents.Insert.DATA, data)
//        }


        if(totalAddress.size!=0){
            intent.putExtra(ContactsContract.Intents.Insert.POSTAL, totalAddress[0])
        }
//        if(!fullAddress.equals("null") && !fullAddress.equals("")){
//
//            intent.putExtra(ContactsContract.Intents.Insert.POSTAL, fullAddress)
//        }

        if(!note.equals("")){
            intent.putExtra(ContactsContract.Intents.Insert.NOTES, note)
        }
        startActivity(intent)
    }
    fun viewAddress( addressToSearch:String)
    {

        val encodedAddress = Uri.encode(addressToSearch)
        val searchUri = "https://www.google.com/maps/search/?api=1&query=$encodedAddress"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(searchUri))
        intent.setPackage("com.google.android.apps.maps")
        startActivity(intent)
    }

    fun dial(numberToDial:String)
    {

        val dialerUri = "tel:" + numberToDial
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse(dialerUri))
        startActivity(intent)
    }

    fun sendEmail(repEmail:String)
    {
        val mailtoUri = "mailto:$repEmail"
        val intent = Intent(Intent.ACTION_SENDTO, Uri.parse(mailtoUri))
        startActivity(Intent.createChooser(intent, "Send Email"))
    }

    fun openWebsite(websiteUrl:String)
    {
        var newUrl=websiteUrl
        // Remain:-check if  barcodeCodeType is of product type the remove https . when prduct barcode is scanned
        if(!newUrl.contains("https://")){
            newUrl ="https://"+newUrl
        }
        try{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(newUrl))
            startActivity(intent)
        }catch (e:Exception)
        {
            Toast.makeText(this, getString(R.string.invalidUrl), Toast.LENGTH_SHORT).show()
        }

    }
    fun openWhatsApp(uri:String,media:Int)
    {
        try {

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            if(media==0) intent.setPackage("com.whatsapp")

            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                Toast.makeText(this, getString(R.string.whatsappIsNotInstalled), Toast.LENGTH_SHORT).show()
            }
        } catch (e: java.lang.Exception) {
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

    fun connectToWifi()
    {
        WifiConnector().connect(
            this,
            wifiEncrypType,
            wifiName,
            wifiPassword,
            false,
            "",
            "",
            "",
            ""
        )
    }
    fun copyWifiPassword()
    {
        val clipboard: ClipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        var value=wifiPassword
        val clip = ClipData.newPlainText("PasswordCopied", value)
        clipboard.setPrimaryClip(clip)

        Toast.makeText(this, "${getString(R.string.copied)}:${getString(R.string.wifiPassword)}:${value}", Toast.LENGTH_SHORT).show()
    }
    fun copyNetworkName()
    {
        val clipboard: ClipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        var value=wifiName
        val clip = ClipData.newPlainText("NetworkNameCopied", value)
        clipboard.setPrimaryClip(clip)

        Toast.makeText(this, "${getString(R.string.copied)}:${getString(R.string.networkName)}:${value}", Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {

        val intent=Intent(this, ViewCodeActivity::class.java)
        intent.putExtra("isFav",isFav)
        intent.putExtra("note",note)
        setResult(Activity.RESULT_OK,intent)
        super.onBackPressed()

    }
}