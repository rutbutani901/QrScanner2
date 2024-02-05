package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.CompoundButton
import android.widget.PopupWindow
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.R
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.databinding.ActivityCreateEventCodeBinding
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.setGradientInStatusBar
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.setLocale
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.sharedPref
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class CreateEventCode : AppCompatActivity() , DatePickerDialog.OnDateSetListener {

    lateinit var binding:ActivityCreateEventCodeBinding
    var allDayEvent=false

    var startYear:Int=0
    var startDate:Int=0
    var startMonth:Int=0

    var endYear:Int=0
    var endDate:Int=0
    var endMonth:Int=0

    var startHour:Int=-1
    var startMin:Int=0
    var endHour:Int=-1
    var endMin:Int=0
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
        binding= ActivityCreateEventCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setGradientInStatusBar(this)
        binding.backButton.setOnClickListener{
            onBackPressed()
        }
//        satView.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked -> }
//        )
        binding.checkbox.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->

            val startTime=binding.startTimeText.text.toString().trim()
            val endTime=binding.endTimeText.text.toString().trim()

            if (isChecked) {
                // Switch is ON
                allDayEvent=true
                binding.startTimeLayout.visibility=View.GONE
                binding.endTimeLayout.visibility=View.GONE
                binding.guideline2.setGuidelinePercent(1.0F)
                binding.startTimeInputErrorIcon.visibility= View.GONE
                binding.endTimeInputErrorIcon.visibility= View.GONE

            } else {
                // Switch is OFF
                allDayEvent=false
                binding.startTimeLayout.visibility=View.VISIBLE
                binding.endTimeLayout.visibility=View.VISIBLE
                binding.guideline2.setGuidelinePercent(0.5F)

//                if(startTime==""){
//                    binding.startTimeInputErrorIcon.visibility= View.VISIBLE
//                }else{
//                    binding.startTimeInputErrorIcon.visibility= View.GONE
//                }
//
//                if(endTime==""){
//                    binding.endTimeInputErrorIcon.visibility= View.VISIBLE
//                }else{
//                    binding.startTimeInputErrorIcon.visibility= View.GONE
//                }
            }
        })

        binding.startDateLayout.setOnClickListener{
            val mcurrentDate = Calendar.getInstance()
            val mYear = mcurrentDate[Calendar.YEAR]
            val mMonth = mcurrentDate[Calendar.MONTH]
            val mDay = mcurrentDate[Calendar.DAY_OF_MONTH]

            val mDatePicker: DatePickerDialog = DatePickerDialog(this, R.style.DialogTheme, object :
                DatePickerDialog.OnDateSetListener {
                override fun onDateSet(
                    view: android.widget.DatePicker?,
                    year: Int,
                    month: Int,
                    dayOfMonth: Int
                ) {

                    startYear=year
                    startDate=dayOfMonth
                    startMonth=month
                    binding.startDateText.text="${month+1}/${dayOfMonth}/${year}"
                    binding.startDateInputErrorIcon.visibility=View.GONE

                }

            }, mYear, mMonth, mDay)

            mDatePicker.show()

        }
        binding.endDateLayout.setOnClickListener{
            val mcurrentDate = Calendar.getInstance()
            val mYear = mcurrentDate[Calendar.YEAR]
            val mMonth = mcurrentDate[Calendar.MONTH]
            val mDay = mcurrentDate[Calendar.DAY_OF_MONTH]

            val mDatePicker: DatePickerDialog = DatePickerDialog(this, R.style.DialogTheme, object :
                DatePickerDialog.OnDateSetListener {
                override fun onDateSet(
                    view: android.widget.DatePicker?,
                    year: Int,
                    month: Int,
                    dayOfMonth: Int
                ) {
                    endYear=year
                    endDate=dayOfMonth
                    endMonth=month

                    binding.endDateText.text="${month+1}/${dayOfMonth}/${year}"

                    binding.endDateInputErrorIcon.visibility=View.GONE
                }

            }, mYear, mMonth, mDay)

            mDatePicker.show()

        }
        binding.tickButton.setOnClickListener{
            gotoNextScrren()
        }
        binding.create.setOnClickListener{
            gotoNextScrren()
        }

        binding.title.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if(binding.title.text.toString().trim()!=""){

                    titleInputErrorIconShown=false
                    titlePopUpWindow?.dismiss()
                    binding.titleInputErrorIcon.visibility= View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
        binding.startTimeLayout.setOnClickListener{
            val c = Calendar.getInstance()

            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(
                this,
                R.style.TimePickerTheme,
                { view, hourOfDay, selectedMinute ->

                    var amPmChecker=""
                    //view.is24HourView
                    if(hourOfDay<12){
                        amPmChecker="A.M"
                    }else{
                        amPmChecker="P.M"
                    }
                    startHour=hourOfDay
                    startMin=selectedMinute

                    binding.startTimeInputErrorIcon.visibility=View.GONE
                    binding.startTimeText.text="${hourOfDay}:${selectedMinute}  ${amPmChecker}"
                },
                hour,
                minute,
                true
            )

            timePickerDialog.show()
        }
        binding.endTimeLayout.setOnClickListener{


            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)
            val timePickerDialog = TimePickerDialog(
                this,
                R.style.TimePickerTheme,
                { view, hourOfDay, selectedMinute ->

                    var amPmChecker=""
                    //view.is24HourView
                    if(hourOfDay<12){
                        amPmChecker="A.M"
                    }else{
                        amPmChecker="P.M"
                    }

                    endHour=hourOfDay
                    endMin=selectedMinute
                    binding.endTimeText.text="${hourOfDay}:${selectedMinute}  ${amPmChecker}"

                    binding.endTimeInputErrorIcon.visibility=View.GONE
                },
                hour,
                minute,
                true
            )

            timePickerDialog.show()
        }
    }

    fun gotoNextScrren(){
        title= binding.title.text.toString().trim()
        location= binding.location.text.toString().trim()
        description= binding.description.text.toString().trim()

        if(title==""){

            if(!titleInputErrorIconShown){

                titleInputErrorIconShown=true
                titleNoInputPopUp(binding.title)
                binding.titleInputErrorIcon.visibility= View.VISIBLE
            }

        }

        val startDate=binding.startDateText.text.toString().trim()
        val endDate=binding.endDateText.text.toString().trim()
        val startTime=binding.startTimeText.text.toString().trim()
        val endTime=binding.endTimeText.text.toString().trim()

        if(startDate==""){
            binding.startDateInputErrorIcon.visibility=View.VISIBLE
        }
        if(endDate==""){
            binding.endDateInputErrorIcon.visibility=View.VISIBLE
        }
        if(allDayEvent)
        {
            if(startDate!="" && endDate!="" && title != ""){
                checkNewer()
            }
        }
        else{
            if(startTime==""){
                binding.startTimeInputErrorIcon.visibility=View.VISIBLE
            }
            if(endTime==""){
                binding.endTimeInputErrorIcon.visibility=View.VISIBLE
            }
            if(startDate!="" && endDate!="" && startTime!="" && endTime!="" && title != ""){
                checkNewer()
            }
        }
    }
    var titlePopUpWindow: PopupWindow?=null
    var titleInputErrorIconShown=false
    fun titleNoInputPopUp(refView: View){

        var inflater= applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view= inflater.inflate(R.layout.item_no_input_popup,null)
        titlePopUpWindow= PopupWindow(view,
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT,true)
        titlePopUpWindow?.showAsDropDown(refView,0,0, Gravity.END)
        titlePopUpWindow?.isFocusable=false
    }


    var title=""
    var location=""
    var description=""
    fun checkNewer()
    {
        val firstDate = Calendar.getInstance()
        val secondDate = Calendar.getInstance()
        firstDate.set(startYear,startMonth,startDate)
        secondDate.set(endYear,endMonth,endDate)

        if (secondDate.timeInMillis >= firstDate.timeInMillis) {

            if(allDayEvent)
            {
                getStartEndDate()
                var veventString="BEGIN:VCALENDAR\n"+
                        "VERSION:2.0\n"+
                        "PRODID:${title}\n"+
                        "BEGIN:VEVENT\n"+
                        "DTSTART:${startDateString}\n"+
                        "DTEND:${endDateString}\n"

                if(!location.equals("null") && !location.equals("")) veventString+="LOCATION:${location}\n"
                if(!description.equals("null") && !description.equals("")) veventString+="DESCRIPTION:${description}\n"

                veventString+="END:VEVENT\n"+"END:VCALENDAR"

                val intent= Intent(this@CreateEventCode, ViewCodeActivity::class.java)
                intent.putExtra("customGenerator",1)
                intent.putExtra("barcodeValue",veventString)
                intent.putExtra("barcodeType",11)//1 is for calender event
                intent.putExtra("barcodeFormat",256)// 7 is for text type
                startActivity(intent)
            }
            else{
                val firstTime = Date()
                val secondTime = Date()

                firstTime.hours = startHour
                firstTime.minutes = startMin
                secondTime.hours = endHour
                secondTime.minutes =endMin

                if(secondDate.timeInMillis > firstDate.timeInMillis){

                    dataApproved()
                }else{// they are equal
                    if (secondTime.time >= firstTime.time) {

                        dataApproved()

                    } else {
                        Log.d("dateChekerTime","smaller")
                    }
                }

            }

        }
        else {
            Log.d("dateCheker","smaller")
        }
    }

    var startDateString=""
    var endDateString=""

    fun getStartEndDate()
    {
        startDateString=""
        endDateString=""
        startDateString+=startYear.toString()
        if((startMonth/10)<1) startDateString+="0${startMonth+1}"
        else startDateString+=(startMonth+1)

        if((startDate/10<1))  startDateString+="0${startDate}"
        else startDateString+=startDate.toString()

        endDateString+=endYear.toString()
        if((endMonth/10)<1) endDateString+="0${endMonth+1}"
        else endDateString+=(endMonth+1)

        if((endDate/10<1))  endDateString+="0${endDate}"
        else endDateString+=endDate.toString()


    }
    fun mergeDateTime(year:Int,month:Int, date:Int,hour:Int, min:Int): String
    {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR,year)
        calendar.set(Calendar.MONTH,month)
        calendar.set(Calendar.DAY_OF_MONTH,date)
        // calendar.set(year,month,date)
        calendar.set(Calendar.HOUR_OF_DAY,hour) // Set the hour (3:00 PM)
        calendar.set(Calendar.MINUTE,min) // Set the min (3:00 PM)
        val dateFormat = SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'")
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"))
        val timestamp: String = dateFormat.format(calendar.time)
        return timestamp
    }


    fun dataApproved()
    {
        var startValue= mergeDateTime(startYear,startMonth,startDate,startHour,startMin)
        var endValue=mergeDateTime(endYear,endMonth,endDate,endHour,endMin)
        var veventString="BEGIN:VCALENDAR\n"+
                "VERSION:2.0\n"+
                "PRODID:${title}\n"+
                "BEGIN:VEVENT\n"+
                "DTSTART:${startValue}\n"+
                "DTEND:${endValue}\n"

        if(!location.equals("null") && !location.equals("")) veventString+="LOCATION:${location}\n"
        if(!description.equals("null") && !description.equals("")) veventString+="DESCRIPTION:${description}\n"

        veventString+="END:VEVENT\n"+"END:VCALENDAR"
        var intent= Intent(this@CreateEventCode, ViewCodeActivity::class.java)
        intent.putExtra("customGenerator",1)
        intent.putExtra("barcodeValue",veventString)
        intent.putExtra("barcodeType",11)//11 is for calender event
        intent.putExtra("barcodeFormat",256)// 7 is for text type

        startActivity(intent)
    }

    override fun onDateSet(
        view: android.widget.DatePicker?,
        year: Int,
        month: Int,
        dayOfMonth: Int
    ) {
        val mCalendar: Calendar = Calendar.getInstance()
        mCalendar.set(Calendar.YEAR, year)
        mCalendar.set(Calendar.MONTH, month)
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        val selectedDate: String = DateFormat.getDateInstance(DateFormat.FULL).format(mCalendar.getTime())
        binding.startDateText.setText(selectedDate)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}