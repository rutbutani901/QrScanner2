package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.viewModel

import androidx.lifecycle.ViewModel
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.R
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.modelClass.LanguageModelClass

class LanguageViewModel: ViewModel() {


    private val languageList= arrayListOf(
        LanguageModelClass(R.drawable.lang_english_icon,"en","English",true),
        LanguageModelClass(R.drawable.lang_spain_icon,"es","Español",false),
        LanguageModelClass(R.drawable.lang_france_icon,"fr","Français",false),
        LanguageModelClass(R.drawable.lang_german_icon,"de","Deutsch",false),
        LanguageModelClass(R.drawable.lang_italy_icon,"it","Italiano",false),
        LanguageModelClass(R.drawable.lang_portugal_icon,"pt","Português",false),
        LanguageModelClass(R.drawable.lang_korean_icon,"ko","한국인",false)
    )

    fun getLanList()=languageList
}