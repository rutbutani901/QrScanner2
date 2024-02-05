package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.activity

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.FileProvider
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.R
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.databinding.ActivityAboutUsBinding
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.setGradientInStatusBar
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.setLocale
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.sharedPref
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class AboutUsActivity : AppCompatActivity() {

    lateinit var binding:ActivityAboutUsBinding

    var barcodeValue="https://play.google.com/store/apps/details?id=${packageName}"
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

        binding= ActivityAboutUsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setGradientInStatusBar(this)
        binding.backButton.setOnClickListener{
            onBackPressed()
        }

        generateQrImage(BarcodeFormat.QR_CODE, 1000)

        binding.share.setOnClickListener{
            if(barcodeImageToShare!=null){
                shareImageandText(barcodeImageToShare!!)
            }
        }
        binding.save.setOnClickListener{
            barcodeImageToShare?.let {
                val intent = Intent(this, SaveQrCodeActivity::class.java)

                // Convert the Bitmap to a byte array
                val stream = ByteArrayOutputStream()
                it.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                val byteArray = stream.toByteArray()
                intent.putExtra("bitmap", byteArray)
                startActivity(intent)
            }
        }
    }
    var barcodeImageToShare: Bitmap?=null

    fun generateQrImage(format: BarcodeFormat, width: Int) {

        val multiFormatWriter = MultiFormatWriter();
        try {
            val hintMap = mapOf(
                EncodeHintType.MARGIN to 0
            )
            val bitMatrix = multiFormatWriter.encode(barcodeValue, format, width, width, hintMap)
            val barcodeEncoder = BarcodeEncoder();

            if (format.toString().contains("QR_CODE") || format.toString()
                    .contains("DATA_MATRIX") || format.toString().contains("AZTEC")
            ) {

                val bitmap = barcodeEncoder.createBitmap(bitMatrix)

                binding.qrImage.visibility = View.VISIBLE
                binding.barcodeImage.visibility = View.GONE
                binding.qrImage.setImageBitmap(bitmap)
                barcodeImageToShare=bitmap
            } else {

                val bitmap = barcodeEncoder.createBitmap(bitMatrix)
                binding.qrImage.visibility = View.GONE
                binding.barcodeImage.visibility = View.VISIBLE
                binding.barcodeImage.setImageBitmap(bitmap)
                barcodeImageToShare=bitmap
            }

        } catch (e: Exception) {
            Log.d("erroror", "Could not generate QR image")

        }
    }

    private fun shareImageandText(bitmap: Bitmap) {
        val uri = getImageToShare(bitmap)
        val intent = Intent(Intent.ACTION_SEND)

        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.putExtra(Intent.EXTRA_TEXT, barcodeValue)// add barcode value here
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here")
        intent.type = "image/png"
        startActivity(Intent.createChooser(intent, "Share Via"))
    }

    private fun getImageToShare(bitmap: Bitmap): Uri? {
        val imagefolder = File(cacheDir, "images")
        var uri: Uri? = null
        try {
            imagefolder.mkdirs()
            val file = File(imagefolder, "shared_image.png")
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream)
            outputStream.flush()
            outputStream.close()
            uri = FileProvider.getUriForFile(this, "$packageName.provider", file)
        } catch (e: Exception) {
            Toast.makeText(this, "" + e.message, Toast.LENGTH_LONG).show()
        }
        return uri
    }

}