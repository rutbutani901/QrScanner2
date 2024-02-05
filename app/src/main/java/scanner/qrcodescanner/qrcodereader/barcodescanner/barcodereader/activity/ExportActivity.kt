package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.R
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.databinding.ActivityExportBinding
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.setGradientInStatusBar
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.setLocale
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.sharedPref
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.modelClass.ExportDataModel
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException


class ExportActivity : AppCompatActivity() {

    lateinit var binding: ActivityExportBinding

    //0 for cvs
    //1 for txt
    var byDefaultExportFormat = 0

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
        binding = ActivityExportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setGradientInStatusBar(this)


        val dataList: ArrayList<ExportDataModel> =
            intent.getSerializableExtra("data") as ArrayList<ExportDataModel>
        binding.csv.setOnClickListener {
            byDefaultExportFormat = 0
            binding.csvTickIcon.setImageResource(R.drawable.ic_export_selector_yes)
            when(sharedPref.appTheme){
                0->{
                     binding.csvTickIcon.imageTintList=ContextCompat.getColorStateList(this,R.color.greenAppColor)
                }
                1->{
                     binding.csvTickIcon.imageTintList=ContextCompat.getColorStateList(this,R.color.blueThemeColor)
                }
                2->{
                     binding.csvTickIcon.imageTintList=ContextCompat.getColorStateList(this,R.color.purpleThemeColor)
                }
                3->{
                     binding.csvTickIcon.imageTintList=ContextCompat.getColorStateList(this,R.color.yellowThemeColor)
                }
                4->{
                     binding.csvTickIcon.imageTintList=ContextCompat.getColorStateList(this,R.color.pinkThemeColor)
                }
            }

            binding.textTickIcon.setImageResource(R.drawable.ic_export_selector_no)
        }
        binding.txt.setOnClickListener {
            byDefaultExportFormat = 1
            binding.csvTickIcon.setImageResource(R.drawable.ic_export_selector_no)
            binding.textTickIcon.setImageResource(R.drawable.ic_export_selector_yes)
            when(sharedPref.appTheme){
                0->{
                    binding.textTickIcon.imageTintList=ContextCompat.getColorStateList(this,R.color.greenAppColor)
                }
                1->{
                    binding.textTickIcon.imageTintList=ContextCompat.getColorStateList(this,R.color.blueThemeColor)
                }
                2->{
                    binding.textTickIcon.imageTintList=ContextCompat.getColorStateList(this,R.color.purpleThemeColor)
                }
                3->{
                    binding.textTickIcon.imageTintList=ContextCompat.getColorStateList(this,R.color.yellowThemeColor)
                }
                4->{
                    binding.textTickIcon.imageTintList=ContextCompat.getColorStateList(this,R.color.pinkThemeColor)
                }
            }

        }
        binding.export.setOnClickListener {
            when (byDefaultExportFormat) {
                0 -> {
                    makeCsv2(dataList)
                }
                1 -> {
                    createTxtFile(dataList)
                }
            }
        }
        binding.backButton.setOnClickListener{
            onBackPressed()
        }

    }


    fun makeCsv2(dataList: ArrayList<ExportDataModel>) {
        try {

            val file = File(filesDir, "${System.currentTimeMillis()}.csv")

            val fileWriter = FileWriter(file)
            val bufferedWriter = BufferedWriter(fileWriter)

            var headers = ""
            var values = ""
            dataList.forEach { headers += "${it.title}," }
            dataList.forEach { values += "${it.value}," }


            bufferedWriter.write(headers) // Write header
            bufferedWriter.newLine() // Move to the next line
            bufferedWriter.write(values) // Write data

            // Close the BufferedWriter
            bufferedWriter.close()
            shareit(file)

            // You now have a CSV file named "data.csv" in your app's internal storage.
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    fun shareit(file: File) {

        val fileUri = FileProvider.getUriForFile(this, "$packageName.provider", file)
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/csv"
        shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri)
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(Intent.createChooser(shareIntent, "Share CSV File"))

    }

    fun createTxtFile(dataList: ArrayList<ExportDataModel>) {
        try {

            val file = File(filesDir, "${System.currentTimeMillis()}.txt")


            val fileWriter = FileWriter(file)
            val bufferedWriter = BufferedWriter(fileWriter)

            var value = ""
            dataList.forEach { value += "${it.title}: ${it.value}\n" }


            bufferedWriter.write(value)

            bufferedWriter.close()

            shareTxt(file)
            // You now have a text file named "data.txt" in your app's internal storage.
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    fun shareTxt(file: File) {
        val fileUri: Uri = FileProvider.getUriForFile(this, "$packageName.provider", file)
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri)
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(Intent.createChooser(shareIntent, "Share Text File"))
    }
}