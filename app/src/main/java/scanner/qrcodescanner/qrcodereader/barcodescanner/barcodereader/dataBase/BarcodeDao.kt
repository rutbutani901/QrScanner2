package qrcodescanner.qrcodereader.barcodescanner.barcodereader.dataBase

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BarcodeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(barcodeDataModel:DataModel):Long

    @Query("SELECT * FROM barcodeDataTable WHERE barValue == :barcodeValue")
    fun doesBarcodesExist(barcodeValue: String): LiveData<DataModel>


    @Query("SELECT EXISTS(SELECT * FROM barcodeDataTable WHERE barValue = :barcodeValue)")
    fun newdoesBarcodesExist(barcodeValue: String): Boolean


    @Query("DELETE FROM barcodeDataTable WHERE id = :id")
    fun delete(id: Int)

    @Query("SELECT * FROM barcodeDataTable ORDER BY isFav DESC, id DESC")//id ASC
    fun getAllBarcodes():  LiveData<List<DataModel>>

//    @Query("SELECT * FROM barcodeDataTable WHERE type = 0 ORDER BY isFav DESC, id DESC")//id ASC
//    fun getAllScannedBarcodes():  LiveData<List<DataModel>>

    @Query("SELECT * FROM barcodeDataTable WHERE type = 0 ORDER BY isFav DESC, id DESC")//id ASC
    fun getAllScannedBarcodesLive():  LiveData<List<DataModel>>

    @Query("SELECT * FROM barcodeDataTable WHERE type = 0 ORDER BY isFav DESC, id DESC")//id ASC
    fun getAllScannedBarcodes():  List<DataModel>

    @Query("SELECT * FROM barcodeDataTable WHERE type = 0 AND barcodeType =:type ORDER BY isFav DESC, id DESC")//id ASC
    fun getFilterWiseBarcodesInScanHistory(type:Int):  LiveData<List<DataModel>>

    @Query("SELECT * FROM barcodeDataTable WHERE type = 0 AND isFav = 1 AND barcodeType =:type ORDER BY isFav DESC, id DESC")//id ASC
    fun getFilterWiseFavBarcodesInScanHistory(type:Int):  LiveData<List<DataModel>>

    @Query("SELECT * FROM barcodeDataTable WHERE type = 1 AND isFav = 1 AND barcodeType =:type ORDER BY isFav DESC, id DESC")//id ASC
    fun getFilterWiseFavBarcodesInCreateHistory(type:Int):  LiveData<List<DataModel>>


//    @Query("SELECT * FROM barcodeDataTable WHERE type = 1 ORDER BY isFav DESC, id DESC")//id ASC
//    fun getAllCreatedBarcodes():  LiveData<List<DataModel>>

    @Query("SELECT * FROM barcodeDataTable WHERE type = 1 ORDER BY isFav DESC, id DESC")//id ASC
    fun getAllCreatedBarcodesLive():  LiveData<List<DataModel>>


    @Query("SELECT * FROM barcodeDataTable WHERE type = 1 ORDER BY isFav DESC, id DESC")//id ASC
    fun getAllCreatedBarcodes():  List<DataModel>



    @Query("SELECT * FROM barcodeDataTable WHERE type = 1 AND barcodeType =:type ORDER BY isFav DESC, id DESC")//id ASC
    fun getFilterWiseBarcodesInCreateHistory(type:Int):  LiveData<List<DataModel>>

    @Query("SELECT * FROM barcodeDataTable WHERE type = 0 AND isFav = 1 ORDER BY isFav DESC, id DESC")//id ASC
    fun getAllFavInScanBarcodes():  LiveData<List<DataModel>>

    @Query("SELECT * FROM barcodeDataTable WHERE type = 1 AND isFav = 1 ORDER BY isFav DESC, id DESC")//id ASC
    fun getAllFavInCreateBarcodes():  LiveData<List<DataModel>>

    @Query("SELECT * FROM barcodeDataTable ORDER BY id DESC LIMIT 1")
    fun getLatestAdded():LiveData<DataModel>

    @Query("UPDATE barcodeDataTable SET isFav=:makeFav WHERE id=:id")
    fun updateFav( id: Int,makeFav:Boolean)

    @Query("UPDATE barcodeDataTable SET note=:notes WHERE id=:id")
    fun updateNotes( id: Int,notes:String)

    @Query("UPDATE barcodeDataTable SET barcodeType=:newType WHERE id=:id")
    fun updateBarcodeType( id: Int,newType:Int)

    @Query("delete from barcodeDataTable where id in (:idList)")
    fun deleteMultipleBarcodes(idList: List<Int>)

    @Query("delete from barcodeDataTable where id=:id")
    fun deleteBarcode(id: Int)

    @Query("UPDATE barcodeDataTable SET isFav = :makeFav WHERE id IN (:ids)")
    fun makeMultipleFav(ids:List<Int>, makeFav:Boolean)

    @Query("DELETE from barcodeDataTable")
    fun deleteAllHistory()





}