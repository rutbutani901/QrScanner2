package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.activity


import android.content.res.Resources
import android.os.Bundle
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.R
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.databinding.ActivityCreateFbCodeBinding
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.setGradientInStatusBar
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.setLocale
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.sharedPref
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.fragments.IdFragment
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.fragments.UrlFragment
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.viewPager.HomeViewPager


class CreateFbCode : AppCompatActivity() {

    lateinit var binding:ActivityCreateFbCodeBinding
    var isFbOrIg=0
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
        binding= ActivityCreateFbCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setGradientInStatusBar(this)

        //0 for fb
        //1 for ig
         isFbOrIg= intent.getIntExtra("fbOrIg",0)
        when(isFbOrIg){
            0->{
                binding.title.text=getString(R.string.facebbok)
                binding.fbIdText.text=getString(R.string.fbId)
            }
            1->{
                binding.title.text=getString(R.string.instagram)
                binding.fbIdText.text=getString(R.string.username)
            }
            2->{
                binding.title.text=getString(R.string.username)
                binding.fbIdText.text=getString(R.string.url)
            }
        }

        createFragments()

        binding.backButton.setOnClickListener{
            onBackPressed()
        }

    }

    var idFrag= IdFragment()
    var urlFrag= UrlFragment()

    lateinit var homeViewPager: HomeViewPager
    fun createFragments(){
        val fragList= arrayListOf(
            idFrag,
            urlFrag,
        )
        val bundle = Bundle()
        when(isFbOrIg){
            0->{
                bundle.putString("hint", getString(R.string.enter_facebook_id))
            }
            1->{
                bundle.putString("hint", getString(R.string.enterIgUsername))

            }
            2->{
                bundle.putString("hint", getString(R.string.enterTwitterUsername))

            }
        }
        bundle.putInt("isFbOrIg", isFbOrIg)
        idFrag.setArguments(bundle)

        homeViewPager= HomeViewPager(this,fragList)
        binding.viewPager2.adapter= homeViewPager
        binding.viewPager2.offscreenPageLimit= homeViewPager.itemCount


        val typedValue = TypedValue()
        val theme: Resources.Theme = this@CreateFbCode.getTheme()
        theme.resolveAttribute(R.attr.greenAppColor, typedValue, true)
        @ColorInt val greenAppColor: Int = typedValue.data

        binding.viewPager2.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback(){

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    when(position) {
                        0->{
                            when(sharedPref.appTheme){
                                0->{
                                    binding.fbIdText.setTextColor(ContextCompat.getColorStateList(this@CreateFbCode, R.color.greenAppColor))

                                }
                                1->{

                                    binding.fbIdText.setTextColor(ContextCompat.getColorStateList(this@CreateFbCode, R.color.blueThemeColor))
                                }
                                2->{

                                    binding.fbIdText.setTextColor(ContextCompat.getColorStateList(this@CreateFbCode, R.color.purpleThemeColor))
                                }
                                3->{

                                    binding.fbIdText.setTextColor(ContextCompat.getColorStateList(this@CreateFbCode, R.color.yellowThemeColor))
                                }
                                4->{

                                    binding.fbIdText.setTextColor(ContextCompat.getColorStateList(this@CreateFbCode, R.color.pinkThemeColor))
                                }
                            }

//                            binding.fbIdText.setTextColor(ContextCompat.getColorStateList(this@CreateFbCode, greenAppColor))
                            binding.fbIdSelector.visibility=View.VISIBLE
                            binding.urlSelector.visibility=View.GONE
//
                            binding.urlText.setTextColor(ContextCompat.getColorStateList(this@CreateFbCode, R.color.lightGreyColor))
//
                        }
                        1->{
                            when(sharedPref.appTheme){
                                0->{
                                    binding.urlText.setTextColor(ContextCompat.getColorStateList(this@CreateFbCode, R.color.greenAppColor))

                                }
                                1->{

                                    binding.urlText.setTextColor(ContextCompat.getColorStateList(this@CreateFbCode, R.color.blueThemeColor))
                                }
                                2->{

                                    binding.urlText.setTextColor(ContextCompat.getColorStateList(this@CreateFbCode, R.color.purpleThemeColor))
                                }
                                3->{

                                    binding.urlText.setTextColor(ContextCompat.getColorStateList(this@CreateFbCode, R.color.yellowThemeColor))
                                }
                                4->{

                                    binding.urlText.setTextColor(ContextCompat.getColorStateList(this@CreateFbCode, R.color.pinkThemeColor))
                                }
                            }

                            binding.fbIdText.setTextColor(ContextCompat.getColorStateList(this@CreateFbCode, R.color.lightGreyColor))
                            binding.fbIdSelector.visibility=View.GONE
                            binding.urlSelector.visibility=View.VISIBLE
//
//                            binding.urlText.setTextColor(ContextCompat.getColorStateList(this@CreateFbCode, greenAppColor))
//
                        }

                    }
                }
            })

        binding.fbId.setOnClickListener{
            binding.viewPager2.currentItem=0
        }
        binding.url.setOnClickListener{
            binding.viewPager2.currentItem=1
        }
    }
}