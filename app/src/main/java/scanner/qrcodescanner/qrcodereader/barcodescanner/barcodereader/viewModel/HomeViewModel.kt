package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.viewModel

import androidx.lifecycle.ViewModel

class HomeViewModel: ViewModel() {

    private var previousFragmentIndex=-1

    fun getPreviousFragmentIndex()=previousFragmentIndex

    fun setpreviousFragmentIndex(prevFragIndex:Int){
        this.previousFragmentIndex=prevFragIndex
    }
}