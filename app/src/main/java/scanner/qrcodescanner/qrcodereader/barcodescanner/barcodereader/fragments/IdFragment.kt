package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.PopupWindow
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.R
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.activity.ViewCodeActivity
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.databinding.FragmentIdBinding

class IdFragment : Fragment() {

    lateinit var binding: FragmentIdBinding
    lateinit var fragmentActivity: FragmentActivity

   var isFbOrIg=0



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

        binding= FragmentIdBinding.inflate(layoutInflater)

        val hint = requireArguments().getString("hint")
        isFbOrIg = requireArguments().getInt("isFbOrIg")

        binding.input.hint=hint
        binding.create.setOnClickListener{
            gotoNextScreen()
        }

        binding.input.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if(binding.input.text.toString().trim()!=""){

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

        return binding.root
    }

    fun gotoNextScreen(){
        var input= binding.input.text.toString().trim()

        if(input != "")
        {
            binding.enteredWebsiteNoInputIcon.visibility=View.GONE

            when(isFbOrIg){
                0->{
                    input="https://www.facebook.com/profile.php?id=${input}"
                }
                1->{
                    input="https://www.instagram.com/${input}"
                }
                2->{
                    input="https://www.twitter.com/${input}"
                }
            }


            val intent= Intent(fragmentActivity, ViewCodeActivity::class.java)
            intent.putExtra("customGenerator",1)
            intent.putExtra("barcodeValue", input)
            intent.putExtra("barcodeType", 8)//8 is for url, barcodeType
            intent.putExtra("barcodeFormat",256)
            startActivity(intent)
        }
    }

    var popUpWindow: PopupWindow?=null

    var inputErrorIconShown=false

    fun showEnteredWebsiteNoInputPopUp(refView:View){

        inputErrorIconShown=true
        val inflater= fragmentActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view= inflater.inflate(R.layout.item_no_input_popup,null)
        popUpWindow= PopupWindow(view,
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT,true)
        popUpWindow?.showAsDropDown(refView,0,0, Gravity.NO_GRAVITY)
        popUpWindow?.isFocusable=false
    }

}