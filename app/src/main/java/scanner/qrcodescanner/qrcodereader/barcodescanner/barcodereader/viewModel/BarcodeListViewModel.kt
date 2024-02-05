package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import qrcodescanner.qrcodereader.barcodescanner.barcodereader.dataBase.BarcodeDbRepo
import qrcodescanner.qrcodereader.barcodescanner.barcodereader.dataBase.DataModel

class BarcodeListViewModel: ViewModel()  {


    suspend fun insert(context: Context, barcode: DataModel):Long
    {
        return BarcodeDbRepo.insert(context,barcode)
    }

    fun doesBarcodesExist(context: Context, barcodeValue: String):LiveData<DataModel>
    {
        return BarcodeDbRepo.doesBarcodesExist(context,barcodeValue)
    }
    fun newdoesBarcodesExist(context: Context, barcodeValue: String):Boolean
    {
        return BarcodeDbRepo.newdoesBarcodesExist(context,barcodeValue)
    }

    fun delete(context: Context, id:Int)
    {
        BarcodeDbRepo.delete(context, id)
    }

    fun deleteAll(context: Context){
        BarcodeDbRepo.deleteAllHistory(context)
    }

    fun getAllUserData(context: Context): LiveData<List<DataModel>>
    {
        return BarcodeDbRepo.getAllBarcodes(context)
    }

    fun getLatestAdded(context: Context): LiveData<DataModel>
    {
        return BarcodeDbRepo.getLatestAdded(context)
    }

    fun updateFav(context: Context,id:Int,isFav:Boolean)
    {
        BarcodeDbRepo.updateFav(context,id,isFav)
    }
    fun updateNotes(context: Context,id:Int,note:String)
    {
        BarcodeDbRepo.updateNotes(context,id,note)
    }
    fun updateBarcodeType(context: Context,id:Int,newType:Int)
    {
        BarcodeDbRepo.updateBarcodeType(context,id,newType)
    }
}