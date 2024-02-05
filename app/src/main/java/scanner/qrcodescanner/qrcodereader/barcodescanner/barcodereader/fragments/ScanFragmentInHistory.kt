package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.activity.ScanResult
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.adapter.ScanInHistoryAdapter
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.databinding.FragmentScanInHistoryBinding
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.modelClass.HistoryDataEvent
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.modelClass.ScanNowEvent
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.util.Constants
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.util.FadeOutItemAnimator
import org.greenrobot.eventbus.EventBus
import qrcodescanner.qrcodereader.barcodescanner.barcodereader.dataBase.BarcodeDbRepo
import qrcodescanner.qrcodereader.barcodescanner.barcodereader.dataBase.DataModel


class ScanFragmentInHistory : Fragment() {


    lateinit var binding:FragmentScanInHistoryBinding
    lateinit var fragmentActivity: FragmentActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)

        fragmentActivity=context as FragmentActivity
//        EventBus.getDefault().register(this)

    }

    override fun onDestroy() {
        super.onDestroy()
//        EventBus.getDefault().unregister(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(activity!=null) fragmentActivity=activity as FragmentActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentScanInHistoryBinding.inflate(layoutInflater)

        getAllBarcodes()

        binding.scanNow.setOnClickListener{
            EventBus.getDefault().post(ScanNowEvent(true,false))
        }

        return binding.root
    }

    lateinit var allBarcodeList:ArrayList<DataModel>
    var barcodeListAdapter: ScanInHistoryAdapter?=null


    fun getAllBarcodes()
    {
        BarcodeDbRepo.getAllScannedBarcodesLive(fragmentActivity).observe(fragmentActivity, Observer {
            allBarcodeList =it as ArrayList<DataModel>

            val size=allBarcodeList.size

            if(!Constants.isFavEnabledInHistory && (Constants.selectedFilterType==-1)){
                if(!isDeleteEnable){
                    if(size==0){

                       Constants.isScanHistoryEmpty=true
                        EventBus.getDefault().post(HistoryDataEvent(false,true,true))
                        binding.recycler.visibility=View.GONE
                        binding.noHistoryLayout.visibility=View.VISIBLE
                    }else{
                        Constants.isScanHistoryEmpty=false
                        EventBus.getDefault().post(HistoryDataEvent(false,false,true))
                        binding.noHistoryLayout.visibility=View.GONE
                        binding.recycler.visibility=View.VISIBLE
                        settingAdapter(allBarcodeList)
                    }
                }
            }

        })
    }
//    fun getAllBarcodes()
//    {
//        CoroutineScope(Dispatchers.IO).launch {
//            allBarcodeList=BarcodeDbRepo.getAllScannedBarcodes(fragmentActivity) as ArrayList<DataModel>
//
//            val size=allBarcodeList.size
//
//            if(size==0){
//                binding.recycler.visibility=View.GONE
//                binding.noHistoryLayout.visibility=View.VISIBLE
//            }else{
//                binding.noHistoryLayout.visibility=View.GONE
//                binding.recycler.visibility=View.VISIBLE
//
//                fragmentActivity.runOnUiThread{
//
//                    settingAdapter(allBarcodeList)
//                }
//            }
//        }
//
//    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    fun onMessageEvent(event: ChangesInDbEvents) {
//
//        if(event.changesDone){
//            getAllBarcodes()
//        }
//    }

    var isDeleteEnable=false

    fun settingAdapter(barcodeList:ArrayList<DataModel>) {

        barcodeListAdapter= ScanInHistoryAdapter(fragmentActivity, barcodeList) { isLongPressed, isDeleteClicked,holderPosition ->

            if (isLongPressed) {//delete it
                isDeleteEnable=true
                EventBus.getDefault().post(HistoryDataEvent(true,false,true))

                if(isDeleteClicked){
                    val id= allBarcodeList[holderPosition].id
                    BarcodeDbRepo.delete(fragmentActivity, id)
                    barcodeListAdapter?.removeAtSpecific(holderPosition)


                    if(barcodeList.size==0){
                        Constants.isScanHistoryEmpty=true
                        EventBus.getDefault().post(HistoryDataEvent(false,true,true))
                        disableDelete()
                        binding.recycler.visibility=View.GONE
                        binding.noHistoryLayout.visibility=View.VISIBLE
                    }
                }
            }
            else {

                val intent = Intent(fragmentActivity, ScanResult::class.java)
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
            BarcodeDbRepo.getFilterWiseBarcodesInScanHistory(fragmentActivity,type).observe(fragmentActivity, Observer {
                allBarcodeList =it as ArrayList<DataModel>

                val size=allBarcodeList.size

                if(!isDeleteEnable){
                    if(size==0){
                        Constants.isScanHistoryEmpty=true
                       EventBus.getDefault().post(HistoryDataEvent(false,true,true))
                        binding.recycler.visibility=View.GONE
                        binding.noHistoryLayout.visibility=View.VISIBLE
                    }else{
                        Constants.isScanHistoryEmpty=false
                        EventBus.getDefault().post(HistoryDataEvent(false,false,true))
                        binding.noHistoryLayout.visibility=View.GONE
                        binding.recycler.visibility=View.VISIBLE
                        settingAdapter(allBarcodeList)
                    }
                }

            })
        }

    }

    fun getFavBarcodes(){
        BarcodeDbRepo.getAllFavScanBarcodes(fragmentActivity).observe(fragmentActivity, Observer {
            allBarcodeList =it as ArrayList<DataModel>

            val size=allBarcodeList.size

            if(Constants.selectedFilterType!=-1){//filter Applied and fav also
                getFavBarcodesInSpecificFilter(Constants.selectedFilterType)
            }else{
                if(!isDeleteEnable){
                    if(size==0){
                        Constants.isScanHistoryEmpty=true
                       EventBus.getDefault().post(HistoryDataEvent(false,true,true))
                        binding.recycler.visibility=View.GONE
                        binding.noHistoryLayout.visibility=View.VISIBLE
                    }else{
                        Constants.isScanHistoryEmpty=false
                        EventBus.getDefault().post(HistoryDataEvent(false,false,true))
                        binding.noHistoryLayout.visibility=View.GONE
                        binding.recycler.visibility=View.VISIBLE
                        settingAdapter(allBarcodeList)
                    }
                }
            }
//            if(Constants.isFavEnabledInHistory){
//                if(!isDeleteEnable){
//                    if(size==0){
//                        binding.recycler.visibility=View.GONE
//                        binding.noHistoryLayout.visibility=View.VISIBLE
//                    }else{
//                        binding.noHistoryLayout.visibility=View.GONE
//                        binding.recycler.visibility=View.VISIBLE
//                        settingAdapter(allBarcodeList)
//                    }
//                }
//            }

        })
    }

    fun getFavBarcodesInSpecificFilter(type:Int){
        BarcodeDbRepo.getFilterWiseFavBarcodesInScanHistory(fragmentActivity,type).observe(fragmentActivity, Observer {
            allBarcodeList =it as ArrayList<DataModel>

            val size=allBarcodeList.size

            if(!isDeleteEnable){
                if(size==0){
                    Constants.isScanHistoryEmpty=true
                   EventBus.getDefault().post(HistoryDataEvent(false,true,true))
                    binding.recycler.visibility=View.GONE
                    binding.noHistoryLayout.visibility=View.VISIBLE
                }else{
                    Constants.isScanHistoryEmpty=false
                    EventBus.getDefault().post(HistoryDataEvent(false,false,true))
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
        barcodeListAdapter?.enableLongPress(checker)
    }
}




