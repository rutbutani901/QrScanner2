package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.core.content.ContextCompat
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.R
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.databinding.ActivitySaveQrCodeBinding
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.setGradientInStatusBar
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.setLocale
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.sharedPref
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class SaveQrCodeActivity : AppCompatActivity() {

    lateinit var binding:ActivitySaveQrCodeBinding

    var barcodeImage: Bitmap?=null
    var byDefaultSaveFormat = 0
    //0 for jpg
    //1 for png
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
        binding= ActivitySaveQrCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.jpg.isSelected=true

        setGradientInStatusBar(this)
        val byteArray = intent.getByteArrayExtra("bitmap")

// Check if the byte array is not null
        if (byteArray != null) {
            // Convert the byte array back to a Bitmap
            barcodeImage = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

        } else {
            // Handle the case where the byte array is null or empty
        }

        binding.jpg.setOnClickListener {
            byDefaultSaveFormat = 0
            binding.jpg.isSelected=true
            binding.png.isSelected=false
            binding.jpgTick.setImageResource(R.drawable.ic_export_selector_yes)
            when(sharedPref.appTheme){
                0->{
                    binding.jpgTick.imageTintList= ContextCompat.getColorStateList(this,R.color.greenAppColor)
                }
                1->{
                    binding.jpgTick.imageTintList= ContextCompat.getColorStateList(this,R.color.blueThemeColor)
                }
                2->{
                    binding.jpgTick.imageTintList= ContextCompat.getColorStateList(this,R.color.purpleThemeColor)
                }
                3->{
                    binding.jpgTick.imageTintList= ContextCompat.getColorStateList(this,R.color.yellowThemeColor)
                }
                4->{
                    binding.jpgTick.imageTintList= ContextCompat.getColorStateList(this,R.color.pinkThemeColor)
                }
            }

            binding.pngTick.setImageResource(R.drawable.ic_export_selector_no)
        }
        binding.png.setOnClickListener {
            byDefaultSaveFormat = 1
            binding.jpg.isSelected=false
            binding.png.isSelected=true
            binding.jpgTick.setImageResource(R.drawable.ic_export_selector_no)
            binding.pngTick.setImageResource(R.drawable.ic_export_selector_yes)
            when(sharedPref.appTheme){
                0->{
                    binding.pngTick.imageTintList=ContextCompat.getColorStateList(this,R.color.greenAppColor)
                }
                1->{
                    binding.pngTick.imageTintList=ContextCompat.getColorStateList(this,R.color.blueThemeColor)
                }
                2->{
                    binding.pngTick.imageTintList=ContextCompat.getColorStateList(this,R.color.purpleThemeColor)
                }
                3->{
                    binding.pngTick.imageTintList=ContextCompat.getColorStateList(this,R.color.yellowThemeColor)
                }
                4->{
                    binding.pngTick.imageTintList=ContextCompat.getColorStateList(this,R.color.pinkThemeColor)
                }
            }

        }
        binding.save.setOnClickListener {
            if(barcodeImage!=null){
                when (byDefaultSaveFormat) {
                    0 -> {
                        val isSaved= saveBitmapToGallery(barcodeImage!!,System.currentTimeMillis().toString(),"jpg")
                        if(isSaved){
                            Toast.makeText(this,getString(R.string.saved),Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(this,getString(R.string.cantSave),Toast.LENGTH_SHORT).show()
                        }
                    }
                    1 -> {
                        val isSaved=saveBitmapToGallery(barcodeImage!!,System.currentTimeMillis().toString(),"png")
                        if(isSaved){
                            Toast.makeText(this,getString(R.string.saved),Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(this,getString(R.string.cantSave),Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            else{
                Toast.makeText(this,getString(R.string.cantSave),Toast.LENGTH_SHORT).show()
            }

        }
        binding.backButton.setOnClickListener{
            onBackPressed()
        }
    }

    fun saveBitmapToGallery(bitmap: Bitmap, title: String,extension:String): Boolean {
        val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val imageFile = File(directory, "$title.${extension}")

        try {

            val outputStream: OutputStream = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()

            MediaScannerConnection.scanFile(this, arrayOf(imageFile.toString()),null,null)

            // Insert the image into the Android MediaStore so it appears in the gallery
//            val values = ContentValues().apply {
//                put(MediaStore.Images.Media.TITLE, title)
//                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
//                put(MediaStore.Images.Media.DATA, imageFile.absolutePath)
//            }
//
//            this.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

}