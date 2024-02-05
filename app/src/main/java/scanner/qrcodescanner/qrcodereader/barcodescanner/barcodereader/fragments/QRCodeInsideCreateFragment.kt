package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.R
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.activity.*
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.ads.AdEventListener
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.ads.AdHandler
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.databinding.FragmentQRCodeInsideCreateBinding
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.sharedPref
import com.google.android.gms.ads.nativead.NativeAd
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.activity.*

class QRCodeInsideCreateFragment : Fragment() {

    lateinit var binding: FragmentQRCodeInsideCreateBinding
    lateinit var fragmentActivity: FragmentActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentActivity=context as FragmentActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(activity!=null) fragmentActivity=activity as FragmentActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentQRCodeInsideCreateBinding.inflate(layoutInflater)

        loadNewNativeAd()


        setClickListners()
        return binding.root
    }

    fun loadNewNativeAd() {

        if (!fragmentActivity.sharedPref.isPremiumPurchased) {
            if (scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.ads.AdHandler.getInstance(fragmentActivity).isNetworkAvailable(fragmentActivity)) {

                binding.adContainer.visibility = View.GONE
                binding.shimmerLayout.root.visibility = View.VISIBLE

                scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.ads.AdHandler.getInstance(fragmentActivity).loadNativeAd(
                    fragmentActivity,
                    getString(R.string.nativeAdvanced),
                    object :
                        scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.ads.AdEventListener {
                        override fun onAdLoaded(adObject: Any?) {
                            if (adObject != null) {

                                binding.adContainer.visibility = View.VISIBLE
                                binding.shimmerLayout.root.visibility = View.GONE

                                scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.ads.AdHandler.getInstance(fragmentActivity).showNativeAd(
                                    fragmentActivity,
                                    binding.adContainer,
                                    adObject as NativeAd, false
                                )
                            } else {
                                binding.adContainer.visibility = View.GONE
                                binding.shimmerLayout.root.visibility = View.GONE
                            }
                        }

                        override fun onAdClosed() {}
                        override fun onLoadError(errorCode: String?) {

                            binding.adContainer.visibility = View.GONE
                            binding.shimmerLayout.root.visibility = View.GONE
                        }
                    })

            } else {

                binding.adLinearLayout.visibility = View.GONE

            }
        }else{
            binding.adLinearLayout.visibility = View.GONE

        }


    }

    fun setClickListners(){

        binding.clipboard.setOnClickListener{
            try {
                val clipboard: ClipboardManager? = fragmentActivity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
                clipboard?.let {
                    if (clipboard.hasPrimaryClip()) {
                        val clipData: ClipData = clipboard.primaryClip!!
                        if (clipData.itemCount > 0) {
                            val item = clipData.getItemAt(0)
                            val clipBoardData:CharSequence?= item.text

                            clipBoardData?.let {
                                if(clipBoardData.isNotEmpty()){

                                    val intent= Intent(fragmentActivity, ViewCodeActivity::class.java)
                                    intent.putExtra("customGenerator",1)
                                    intent.putExtra("barcodeValue", clipBoardData)
                                    intent.putExtra("barcodeType", 7)//8 is for url, barcodeType
                                    intent.putExtra("barcodeFormat",256)// 7 is for text type
                                    startActivity(intent)
                                }else
                                {
                                    Toast.makeText(fragmentActivity,getString(R.string.noClipboardDataFound),
                                        Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }else{
                        Toast.makeText(fragmentActivity,getString(R.string.noClipboardDataFound),
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
        binding.website.setOnClickListener{

            val intent= Intent(fragmentActivity, CreateWebsiteCode::class.java)
            startActivity(intent)
        }
        binding.phone.setOnClickListener{
            val intent= Intent(fragmentActivity, CreatePhoneCode::class.java)
            startActivity(intent)
        }
        binding.text.setOnClickListener{
            val intent= Intent(fragmentActivity, CreateTextCode::class.java)
            startActivity(intent)
        }
        binding.facebook.setOnClickListener{
            val intent= Intent(fragmentActivity, CreateFbCode::class.java)
            intent.putExtra("fbOrIg",0)
            startActivity(intent)
        }
        binding.instagram.setOnClickListener{
            val intent= Intent(fragmentActivity, CreateFbCode::class.java)
            intent.putExtra("fbOrIg",1)
            startActivity(intent)
        }
        binding.event.setOnClickListener{
            val intent= Intent(fragmentActivity, CreateEventCode::class.java)
            startActivity(intent)
        }
        binding.message.setOnClickListener{
            val intent= Intent(fragmentActivity, CreateMessageCode::class.java)
            startActivity(intent)
        }
        binding.wifi.setOnClickListener{
            val intent= Intent(fragmentActivity, CreateWifiCode::class.java)
            startActivity(intent)
        }
        binding.email.setOnClickListener{
            val intent= Intent(fragmentActivity, CreateEmailCode::class.java)
            startActivity(intent)
        }
        binding.contact.setOnClickListener{
            val intent= Intent(fragmentActivity, CreateContactCode::class.java)
            startActivity(intent)
        }
        binding.twitter.setOnClickListener{
            val intent= Intent(fragmentActivity, CreateFbCode::class.java)
            intent.putExtra("fbOrIg",2)
            startActivity(intent)
        }
        binding.whatsapp.setOnClickListener{
            val intent= Intent(fragmentActivity, CreateWhatsappCode::class.java)
            intent.putExtra("media",0)
            startActivity(intent)
        }
        binding.viber.setOnClickListener{
            val intent= Intent(fragmentActivity, CreateWhatsappCode::class.java)
            intent.putExtra("media",1)
            startActivity(intent)
        }

    }

}