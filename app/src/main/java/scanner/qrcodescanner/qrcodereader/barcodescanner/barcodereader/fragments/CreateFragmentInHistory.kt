package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.activity.ViewCodeActivity
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.adapter.CreateInHistoryAdapter
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.databinding.FragmentCreateInHistoryBinding
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.modelClass.HistoryDataEvent
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.modelClass.ScanNowEvent
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.util.Constants
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.util.FadeOutItemAnimator
import org.greenrobot.eventbus.EventBus
import qrcodescanner.qrcodereader.barcodescanner.barcodereader.dataBase.BarcodeDbRepo
import qrcodescanner.qrcodereader.barcodescanner.barcodereader.dataBase.DataModel


class CreateFragmentInHistory : Fragment() {


    lateinit var fragmentActivity: FragmentActivity
    lateinit var binding:FragmentCreateInHistoryBinding

    var isDeleteEnable=false

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

        binding= FragmentCreateInHistoryBinding.inflate(layoutInflater)

        getAllBarcodes()

        binding.scanNow.setOnClickListener{
            EventBus.getDefault().post(ScanNowEvent(false,true))
        }

        return binding.root
    }
    lateinit var allBarcodeList:ArrayList<DataModel>
    var barcodeListAdapter: CreateInHistoryAdapter?=null

    fun getAllBarcodes()
    {

        BarcodeDbRepo.getAllCreatedBarcodesLive(fragmentActivity).observe(fragmentActivity, Observer {
            allBarcodeList =it as ArrayList<DataModel>

            val size=allBarcodeList.size

            if(!Constants.isFavEnabledInHistory && (Constants.selectedFilterType==-1)){
                if(!isDeleteEnable){
                    if(size==0){
                        Constants.isCreateHistoryEmpty=true
                        EventBus.getDefault().post(HistoryDataEvent(false,true,false))
                        binding.recycler.visibility=View.GONE
                        binding.noHistoryLayout.visibility=View.VISIBLE
                    }else{
                        Constants.isCreateHistoryEmpty=false
                        EventBus.getDefault().post(HistoryDataEvent(false,false,false))
                        binding.noHistoryLayout.visibility=View.GONE
                        binding.recycler.visibility=View.VISIBLE
                        settingAdapter(allBarcodeList)
                    }
                }
            }
        })
    }

    fun settingAdapter(barcodeList:ArrayList<DataModel>) {

        barcodeListAdapter= CreateInHistoryAdapter(fragmentActivity, barcodeList) { isLongPressed, isDeleteClicked,holderPosition ->

            if (isLongPressed) {//delete it
                isDeleteEnable=true
                EventBus.getDefault().post(HistoryDataEvent(true,false,false))
//

                if(isDeleteClicked){

                    val id= allBarcodeList[holderPosition].id
                    BarcodeDbRepo.delete(fragmentActivity, id)
                    barcodeListAdapter?.removeAtSpecific(holderPosition)

                    if(barcodeList.size==0){
                        Constants.isCreateHistoryEmpty=true
                        EventBus.getDefault().post(HistoryDataEvent(false,true,false))
                        disableDelete()
                        binding.recycler.visibility=View.GONE
                        binding.noHistoryLayout.visibility=View.VISIBLE
                    }
                }
            }
            else {
                val intent = Intent(fragmentActivity, ViewCodeActivity::class.java)
                intent.putExtra("barcodeData", barcodeList[holderPosition])
                startActivity(intent)
            }


        }
        binding.recycler.layoutManager= LinearLayoutManager(fragmentActivity, LinearLayoutManager.VERTICAL,false)
        binding.recycler.adapter=barcodeListAdapter
        binding.recycler.itemAnimator= FadeOutItemAnimator()

    }

    fun getFilterWiseBarcodes(type:Int)
    {
        if(Constants.isFavEnabledInHistory){
            getFavBarcodesInSpecificFilter(type)
        }
        else{
            BarcodeDbRepo.getFilterWiseBarcodesInCreateHistory(fragmentActivity,type).observe(fragmentActivity, Observer {
                allBarcodeList =it as ArrayList<DataModel>

                val size=allBarcodeList.size

                if(!isDeleteEnable){
                    if(size==0){
                        Constants.isCreateHistoryEmpty=true
                        EventBus.getDefault().post(HistoryDataEvent(false,true,false))
                        binding.recycler.visibility=View.GONE
                        binding.noHistoryLayout.visibility=View.VISIBLE
                    }else{
                        Constants.isCreateHistoryEmpty=false
                        EventBus.getDefault().post(HistoryDataEvent(false,false,false))
                        binding.noHistoryLayout.visibility=View.GONE
                        binding.recycler.visibility=View.VISIBLE
                        settingAdapter(allBarcodeList)
                    }
                }
            })
        }


    }

    fun getFavBarcodes(){
        BarcodeDbRepo.getAllFavCreatedBarcodes(fragmentActivity).observe(fragmentActivity, Observer {
            allBarcodeList =it as ArrayList<DataModel>

            val size=allBarcodeList.size

            if(Constants.selectedFilterType!=-1){//filter Applied and fav also
                getFavBarcodesInSpecificFilter(Constants.selectedFilterType)
            }else{
                if(!isDeleteEnable){
                    if(size==0){
                        Constants.isCreateHistoryEmpty=true
                        EventBus.getDefault().post(HistoryDataEvent(false,true,false))
                        binding.recycler.visibility=View.GONE
                        binding.noHistoryLayout.visibility=View.VISIBLE
                    }else{
                        Constants.isCreateHistoryEmpty=false
                        EventBus.getDefault().post(HistoryDataEvent(false,false,false))
                        binding.noHistoryLayout.visibility=View.GONE
                        binding.recycler.visibility=View.VISIBLE
                        settingAdapter(allBarcodeList)
                    }
                }
            }

        })
    }

    fun getFavBarcodesInSpecificFilter(type:Int){
        BarcodeDbRepo.getFilterWiseFavBarcodesInScanHistory(fragmentActivity,type).observe(fragmentActivity, Observer {
            allBarcodeList =it as ArrayList<DataModel>

            val size=allBarcodeList.size

            if(!isDeleteEnable){
                if(size==0){
                    Constants.isCreateHistoryEmpty=true
                    EventBus.getDefault().post(HistoryDataEvent(false,true,false))
                    binding.recycler.visibility=View.GONE
                    binding.noHistoryLayout.visibility=View.VISIBLE
                }else{
                    Constants.isCreateHistoryEmpty=false
                    EventBus.getDefault().post(HistoryDataEvent(false,false,false))
                    binding.noHistoryLayout.visibility=View.GONE
                    binding.recycler.visibility=View.VISIBLE
                    settingAdapter(allBarcodeList)
                }
            }

        })
    }

    fun disableDelete(){
        isDeleteEnable=false
        barcodeListAdapter?.enableLongPress(false)

    }

    fun enebleLongPress(checker:Boolean){
        if(checker) isDeleteEnable=true
        else isDeleteEnable=false
        barcodeListAdapter?.enableLongPress(checker)
    }
}





