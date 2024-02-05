package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.R

class AppIntroFirstFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_app_intro_first, container, false)
    }

}