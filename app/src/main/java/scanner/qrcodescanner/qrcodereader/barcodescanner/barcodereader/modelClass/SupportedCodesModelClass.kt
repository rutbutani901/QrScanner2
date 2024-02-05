package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.modelClass

import android.graphics.drawable.Icon

data class SupportedCodesModelClass(
    var codeTitle:String,
    var codeIcon: Int,
    var isSupported:Boolean
    )