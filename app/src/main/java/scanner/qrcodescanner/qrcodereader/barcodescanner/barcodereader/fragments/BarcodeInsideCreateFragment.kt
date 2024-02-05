package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.R
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.activity.CreateBarcodeInputActivity
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.databinding.FragmentBarcodeInsideCreateBinding
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.modelClass.BarcodeModelClass

class BarcodeInsideCreateFragment : Fragment() {

    lateinit var fragmentActivity: FragmentActivity
    lateinit var binding:FragmentBarcodeInsideCreateBinding

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

        binding= FragmentBarcodeInsideCreateBinding.inflate(layoutInflater)

        setClickListners()
        return binding.root
    }

    fun setClickListners(){
        binding.qrcode.setOnClickListener{
            val barcodeData= BarcodeModelClass("QR CODE",getString(R.string.text),256)
            val intent= Intent(fragmentActivity, CreateBarcodeInputActivity::class.java)
            intent.putExtra("barcodeData",barcodeData)
            startActivity(intent)
        }
        binding.dataMatrix.setOnClickListener{
            val barcodeData= BarcodeModelClass("Data Matrix",getString(R.string.textWithoutSpecialCharacters),16)
            val intent= Intent(fragmentActivity, CreateBarcodeInputActivity::class.java)
            intent.putExtra("barcodeData",barcodeData)
            startActivity(intent)
        }
        binding.pdf.setOnClickListener{
            val barcodeData= BarcodeModelClass("PDF 417",getString(R.string.text),2048)
            val intent= Intent(fragmentActivity, CreateBarcodeInputActivity::class.java)
            intent.putExtra("barcodeData",barcodeData)
            startActivity(intent)
        }
        binding.aztec.setOnClickListener{
            val barcodeData= BarcodeModelClass("AZtec",getString(R.string.textWithoutSpecialCharacters),4096)
            val intent= Intent(fragmentActivity, CreateBarcodeInputActivity::class.java)
            intent.putExtra("barcodeData",barcodeData)
            startActivity(intent)
        }
        binding.ean13.setOnClickListener{
            val barcodeData= BarcodeModelClass("EAN13",getString(R.string.twelveDigitsWithOneChecksum),32)
            val intent= Intent(fragmentActivity, CreateBarcodeInputActivity::class.java)
            intent.putExtra("barcodeData",barcodeData)
            startActivity(intent)
        }
        binding.ean8.setOnClickListener{
            val barcodeData= BarcodeModelClass("EAN8",getString(R.string.eightDigits),64)
            val intent= Intent(fragmentActivity, CreateBarcodeInputActivity::class.java)
            intent.putExtra("barcodeData",barcodeData)
            startActivity(intent)
        }
        binding.upce.setOnClickListener{
            val barcodeData= BarcodeModelClass("UPC E",getString(R.string.sevenDigitsWithOneChecksum),1024)
            val intent= Intent(fragmentActivity, CreateBarcodeInputActivity::class.java)
            intent.putExtra("barcodeData",barcodeData)
            startActivity(intent)
        }
        binding.upca.setOnClickListener{
            val barcodeData= BarcodeModelClass("UPC A",getString(R.string.elevenDigitsWithOneChecksum),512)
            val intent= Intent(fragmentActivity, CreateBarcodeInputActivity::class.java)
            intent.putExtra("barcodeData",barcodeData)
            startActivity(intent)
        }
        binding.code128.setOnClickListener{
            val barcodeData= BarcodeModelClass("Code 128",getString(R.string.textWithoutSpecialCharacters),1)
            val intent= Intent(fragmentActivity, CreateBarcodeInputActivity::class.java)
            intent.putExtra("barcodeData",barcodeData)
            startActivity(intent)
        }
        binding.code93.setOnClickListener{
            val barcodeData= BarcodeModelClass("Code 93",getString(R.string.textInUpperWithoutSpecial),4)
            val intent= Intent(fragmentActivity, CreateBarcodeInputActivity::class.java)
            intent.putExtra("barcodeData",barcodeData)
            startActivity(intent)
        }
        binding.code39.setOnClickListener{
            val barcodeData= BarcodeModelClass("Code 39",getString(R.string.textInUpperWithoutSpecial),2)
            val intent= Intent(fragmentActivity, CreateBarcodeInputActivity::class.java)
            intent.putExtra("barcodeData",barcodeData)
            startActivity(intent)
        }
        binding.codabar.setOnClickListener{
            val barcodeData= BarcodeModelClass("Codabar",getString(R.string.digits),8)
            val intent= Intent(fragmentActivity, CreateBarcodeInputActivity::class.java)
            intent.putExtra("barcodeData",barcodeData)
            startActivity(intent)
        }
        binding.itf.setOnClickListener{
            val barcodeData= BarcodeModelClass("ITF",getString(R.string.evenNumberOfDigits),128)
            val intent= Intent(fragmentActivity, CreateBarcodeInputActivity::class.java)
            intent.putExtra("barcodeData",barcodeData)
            startActivity(intent)
        }

    }
}