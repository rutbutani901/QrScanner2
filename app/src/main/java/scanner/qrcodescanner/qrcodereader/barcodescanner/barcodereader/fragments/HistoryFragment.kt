package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.fragments


import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.ColorInt
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.NetworkChangeReceiver
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.R
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.databinding.FragmentHistoryBinding
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.sharedPref
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.modelClass.HistoryDataEvent
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.util.Constants
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.util.InternetChecker
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.viewModel.BarcodeListViewModel
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.viewPager.HomeViewPager
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class HistoryFragment : Fragment() {

    lateinit var binding:FragmentHistoryBinding
    lateinit var fragmentActivity: FragmentActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentActivity=context as FragmentActivity
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        Constants.openOtherScreenFromApp=false
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: HistoryDataEvent) {

        if(event.isDeleteEnabled){
            binding.delete.visibility=View.VISIBLE
           openDelete(event.fromScan)


        }else{

            if(event.isListEmpty){
                if(Constants.isScanHistoryEmpty && Constants.isCreateHistoryEmpty){
                    binding.delete.visibility=View.GONE
                }
            }else{
                binding.delete.visibility=View.VISIBLE
            }
            closeDelete()
        }
    }
    var deleteOpen=false
    fun openDelete(fromScan:Boolean) {
        deleteOpen=true
        binding.title.text=getString(R.string.deleteAll)
        binding.fav.visibility=View.GONE
        binding.filter.visibility=View.GONE
        binding.back.visibility=View.VISIBLE
        binding.delete.setImageResource(R.drawable.ic_delete_all)

        if(fromScan){
            createFrag.enebleLongPress(true)
        }else{
            scanFrag.enebleLongPress(true)
        }
    }

    fun closeDelete(){
        deleteOpen=false
        binding.title.text=getString(R.string.history)
        binding.fav.visibility=View.VISIBLE
        binding.filter.visibility=View.VISIBLE
        binding.back.visibility=View.GONE
        binding.delete.setImageResource(R.drawable.ic_delete)

        createFrag.enebleLongPress(false)
        scanFrag.enebleLongPress(false)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(activity!=null) fragmentActivity=activity as FragmentActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentHistoryBinding.inflate(layoutInflater)

//        isNetworkAvailable()

        binding.tapToRetry.setOnClickListener{
            binding.tapToRetry.text=getString(R.string.loading)
            Handler(Looper.getMainLooper()).postDelayed({
                binding.tapToRetry.text=getString(R.string.tapToRetry)
            },1000)
        }
        binding.networkSetting.setOnClickListener{

            Constants.openOtherScreenFromApp=true
            val intent = Intent("android.settings.SETTINGS")
            networkCheckerLauncher.launch(intent)
        }

        val connectionLiveData = NetworkChangeReceiver(fragmentActivity)
        connectionLiveData.observe(fragmentActivity, Observer { isConnected ->
            isConnected?.let {
                Log.d("dfdfdfd","fdfdf")
                if(isConnected){
                    if( binding.viewPager2.visibility!=View.VISIBLE){
                        binding.checkInternetLayout.visibility=View.GONE
                        binding.viewPager2.visibility=View.VISIBLE

                        binding.fav.visibility=View.VISIBLE
                        binding.delete.visibility=View.VISIBLE
                        binding.filter.visibility=View.VISIBLE

                        createFragments()
                    }

                }else{
                    binding.viewPager2.visibility=View.GONE
                    binding.checkInternetLayout.visibility=View.VISIBLE

                    binding.fav.visibility=View.GONE

                    binding.filter.visibility=View.GONE

                    //closing filter
                    if(binding.filterLayout.visibility==View.VISIBLE){

                        binding.touchLayout.visibility=View.GONE
                        binding.dimBackground.visibility=View.GONE
                        binding.filter.setImageResource(R.drawable.ic_filter)
                        binding.viewPager2.backgroundTintList=ContextCompat.getColorStateList(fragmentActivity,R.color.lightWhiteMainBackground)

                        val slideDownAnimation: Animation = AnimationUtils.loadAnimation(fragmentActivity, R.anim.history_filter_reverse_animation)
                        binding.filterLayout.startAnimation(slideDownAnimation)
                        binding.filterLayout.visibility=View.GONE
                    }
                    if(binding.back.visibility==View.VISIBLE){

                        deleteOpen=false
                        binding.title.text=getString(R.string.history)
                        binding.back.visibility=View.GONE
                        binding.delete.setImageResource(R.drawable.ic_delete)

                        createFrag.enebleLongPress(false)
                        scanFrag.enebleLongPress(false)

                        scanFrag.disableDelete()
                        createFrag.disableDelete()
                    }
                    binding.delete.visibility=View.GONE
                    if(isDeleteAllDialogOpen){
                        dialog?.dismiss()
                    }


                }
            }
        })

        setClickListners()

        return binding.root
    }

    var networkCheckerLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result->
        Constants.openOtherScreenFromApp=false
        isNetworkAvailable()
    }

    fun isNetworkAvailable(){
        if(this::binding.isInitialized){
            if (InternetChecker.isConnectedToInternet(fragmentActivity) || InternetChecker.isConnectedToWifi(fragmentActivity)) {

                binding.checkInternetLayout.visibility=View.GONE

                createFragments()

            } else {
                binding.checkInternetLayout.visibility=View.VISIBLE

            }
        }
    }



    var canOpenFilterOption=true

    var previousType=-1
    var newType=-1
    fun setClickListners(){



        binding.back.setOnClickListener{
            disableDelete()
        }
        binding.product.setOnClickListener{
            newType=5
            setResetSelectedFilters()
            canOpenFilterOption=true
            closeFilters()
            Constants.selectedFilterType=5

//            if(isFavEnable){
//                scanFrag.getFavBarcodesInSpecificFilter(5)
//                createFrag.getFavBarcodesInSpecificFilter(5)
//            }else{
//                scanFrag.getFilterWiseBarcodes(5)
//                createFrag.getFilterWiseBarcodes(5)
//            }
            scanFrag.getFilterWiseBarcodes(5)
            createFrag.getFilterWiseBarcodes(5)

        }
        binding.website.setOnClickListener{
            newType=8
            setResetSelectedFilters()
            Constants.selectedFilterType=8
            binding.website.isSelected=true
            canOpenFilterOption=true
            closeFilters()
            scanFrag.getFilterWiseBarcodes(8)
            createFrag.getFilterWiseBarcodes(8)
        }
        binding.text.setOnClickListener{
            newType=7
            setResetSelectedFilters()
            Constants.selectedFilterType=7
            binding.text.isSelected=true
            canOpenFilterOption=true
            closeFilters()
            scanFrag.getFilterWiseBarcodes(7)
            createFrag.getFilterWiseBarcodes(7)
        }
        binding.phone.setOnClickListener{
            newType=4
            setResetSelectedFilters()
            Constants.selectedFilterType=4
            binding.phone.isSelected=true
            canOpenFilterOption=true
            closeFilters()
            scanFrag.getFilterWiseBarcodes(4)
            createFrag.getFilterWiseBarcodes(4)
        }
        binding.contact.setOnClickListener{
            newType=1
            setResetSelectedFilters()
            Constants.selectedFilterType=1
            binding.contact.isSelected=true
            canOpenFilterOption=true
            closeFilters()
            scanFrag.getFilterWiseBarcodes(1)
            createFrag.getFilterWiseBarcodes(1)
        }
        binding.email.setOnClickListener{
            newType=2
            setResetSelectedFilters()
            Constants.selectedFilterType=2
            binding.email.isSelected=true
            canOpenFilterOption=true
            closeFilters()
            scanFrag.getFilterWiseBarcodes(2)
            createFrag.getFilterWiseBarcodes(2)
        }
        binding.wifi.setOnClickListener{
            newType=9
            setResetSelectedFilters()
            Constants.selectedFilterType=9
            binding.wifi.isSelected=true
            canOpenFilterOption=true
            closeFilters()
            scanFrag.getFilterWiseBarcodes(9)
            createFrag.getFilterWiseBarcodes(9)
        }
        binding.message.setOnClickListener{
            newType=6
            setResetSelectedFilters()
            Constants.selectedFilterType=6
            binding.message.isSelected=true
            canOpenFilterOption=true
            closeFilters()
            scanFrag.getFilterWiseBarcodes(6)
            createFrag.getFilterWiseBarcodes(6)
        }
        binding.event.setOnClickListener{
            newType=11
            setResetSelectedFilters()
            Constants.selectedFilterType=11
            binding.event.isSelected=true
            canOpenFilterOption=true
            closeFilters()
            scanFrag.getFilterWiseBarcodes(11)
            createFrag.getFilterWiseBarcodes(11)
        }
        binding.facebook.setOnClickListener{
            newType=15
            setResetSelectedFilters()
            Constants.selectedFilterType=15
            binding.facebook.isSelected=true
            canOpenFilterOption=true
            closeFilters()
            scanFrag.getFilterWiseBarcodes(15)
            createFrag.getFilterWiseBarcodes(15)
        }
        binding.instagram.setOnClickListener{
            newType=16
            setResetSelectedFilters()
            Constants.selectedFilterType=16
            binding.instagram.isSelected=true
            canOpenFilterOption=true
            closeFilters()
            scanFrag.getFilterWiseBarcodes(16)
            createFrag.getFilterWiseBarcodes(16)
        }
        binding.twitter.setOnClickListener{
            newType=17
            setResetSelectedFilters()
            Constants.selectedFilterType=17
            binding.twitter.isSelected=true
            canOpenFilterOption=true
            closeFilters()
            scanFrag.getFilterWiseBarcodes(17)
            createFrag.getFilterWiseBarcodes(17)
        }
        binding.whatsapp.setOnClickListener{
            newType=18
            setResetSelectedFilters()
            Constants.selectedFilterType=18
            binding.whatsapp.isSelected=true
            canOpenFilterOption=true
            closeFilters()
            scanFrag.getFilterWiseBarcodes(18)
            createFrag.getFilterWiseBarcodes(18)
        }
        binding.viber.setOnClickListener{
            newType=19
            setResetSelectedFilters()
            Constants.selectedFilterType=18
            binding.viber.isSelected=true
            canOpenFilterOption=true
            closeFilters()
            scanFrag.getFilterWiseBarcodes(19)
            createFrag.getFilterWiseBarcodes(19)
        }
        binding.reset.setOnClickListener{
            newType=-1
            setResetSelectedFilters()

            Constants.selectedFilterType=-1
            canOpenFilterOption=true
            closeFilters()
            if(Constants.isFavEnabledInHistory){
                scanFrag.getFavBarcodes()
                createFrag.getFavBarcodes()
            }else{
                scanFrag.getAllBarcodes()
                createFrag.getAllBarcodes()
            }

        }

        binding.networkSetting.setOnClickListener{

            val intent = Intent("android.settings.SETTINGS")
            networkCheckerLauncher.launch(intent)
        }

        binding.touchLayout.setOnClickListener{
            if(!canOpenFilterOption){
                canOpenFilterOption=true
                closeFilters()
            }
        }
        binding.dimBackground.setOnClickListener{

        }
        binding.filter.setOnClickListener{

            if(canOpenFilterOption){
                canOpenFilterOption=false
               openFilters()
            }else{
                canOpenFilterOption=true
               closeFilters()
            }
        }

        binding.fav.setOnClickListener{
//            newType=0
//            setResetSelectedFilters()

//            if(!canOpenFilterOption){
//                newType=0
//                setResetSelectedFilters()
//                canOpenFilterOption=true
//                closeFilters()
//            }

            if(!isFavEnable){
                Constants.isFavEnabledInHistory=true
                isFavEnable=true
                binding.fav.setImageResource(R.drawable.ic_fav_fill_history)
                scanFrag.getFavBarcodes()
                createFrag.getFavBarcodes()
            }else{
                Constants.isFavEnabledInHistory=false
                isFavEnable=false
                binding.fav.setImageResource(R.drawable.ic_fav_history)

                if(Constants.selectedFilterType!=-1){//filter is selected
                    Log.d("newType",newType.toString())
                    scanFrag.getFilterWiseBarcodes(newType)
                    createFrag.getFilterWiseBarcodes(newType)
                }else{
                    scanFrag.getAllBarcodes()
                    createFrag.getAllBarcodes()
                }

            }


//            if(canOpenFilterOption){
//
//                if(!isFavEnable){
//                    isFavEnable=true
//                    binding.fav.setImageResource(R.drawable.ic_fav_fill_history)
//                }else{
//                    isFavEnable=false
//                    binding.fav.setImageResource(R.drawable.ic_fav_history)
//                }
//
//            }else{
//
//            }
        }
        binding.delete.setOnClickListener{
            if(deleteOpen){
                showDeleteAllDialog()
            }else{
                deleteOpen=true
                binding.title.text=getString(R.string.deleteAll)
                binding.fav.visibility=View.GONE
                binding.filter.visibility=View.GONE
                binding.back.visibility=View.VISIBLE
                binding.delete.setImageResource(R.drawable.ic_delete_all)

                createFrag.enebleLongPress(true)
                scanFrag.enebleLongPress(true)
            }
        }
    }

    var isDeleteAllDialogOpen=false

    var dialog:AlertDialog?=null
    private fun showDeleteAllDialog() {


        val dialogView: View = this.layoutInflater.inflate(R.layout.dialog_home_camera_permission, null)

        val dialogBuilder : AlertDialog.Builder = AlertDialog.Builder(fragmentActivity, R.style.MyDialogStyle)
        dialogBuilder.setView(dialogView)
        dialogBuilder.setCancelable(false)
        dialogBuilder.create()
         dialog = dialogBuilder.show()

        isDeleteAllDialogOpen=true
        dialogView.findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.ic_delete_dialog)
        dialogView.findViewById<TextView>(R.id.title).setText(getString(R.string.deleteFiles))
        dialogView.findViewById<TextView>(R.id.description).setText(getString(R.string.deleteFilesMessage))
        dialogView.findViewById<TextView>(R.id.allow).setText(getString(R.string.yes))
        dialogView.findViewById<LinearLayoutCompat>(R.id.hand).visibility=View.GONE

        dialogView.findViewById<TextView>(R.id.allow).setOnClickListener{
            deleteAll()
            isDeleteAllDialogOpen=false
            dialog?.dismiss()
        }

        dialogView.findViewById<TextView>(R.id.no).setOnClickListener {
            isDeleteAllDialogOpen=false
            dialog?.dismiss()
        }
    }

    fun deleteAll(){
        disableDelete()
        BarcodeListViewModel().deleteAll(fragmentActivity)
    }


    var isFavEnable=false
    fun setResetSelectedFilters(){
        if(previousType!=newType){
//            if(isFavEnable){
//
//                isFavEnable=false
//                binding.fav.setImageResource(R.drawable.ic_fav_history)
//            }

            when(previousType){
                8->{
                    binding.website.isSelected=false
                }
                4->{
                    binding.phone.isSelected=false
                }
                1->{
                    binding.contact.isSelected=false
                }
                9->{
                    binding.wifi.isSelected=false
                }
                11->{
                    binding.event.isSelected=false
                }
                7->{
                    binding.text.isSelected=false
                }
                2->{
                    binding.email.isSelected=false
                }
                6->{
                    binding.message.isSelected=false
                }
                5->{
                    binding.product.isSelected=false
                }
                15->{
                    binding.facebook.isSelected=false
                }
                16->{
                    binding.instagram.isSelected=false
                }
                17->{
                    binding.twitter.isSelected=false
                }
                18->{
                    binding.whatsapp.isSelected=false
                }
                19->{
                    binding.viber.isSelected=false
                }
                else->{

                }
            }
            when(newType){
                8->{
                    binding.website.isSelected=true
                }
                4->{
                    binding.phone.isSelected=true
                }
                1->{
                    binding.contact.isSelected=true
                }
                9->{
                    binding.wifi.isSelected=true
                }
                11->{
                    binding.event.isSelected=true
                }
                7->{
                    binding.text.isSelected=true
                }
                2->{
                    binding.email.isSelected=true
                }
                6->{
                    binding.message.isSelected=true
                }
                5->{
                    binding.product.isSelected=true
                }
                15->{
                    binding.facebook.isSelected=true
                }
                16->{
                    binding.instagram.isSelected=true
                }
                17->{
                    binding.twitter.isSelected=true
                }
                18->{
                    binding.whatsapp.isSelected=true
                }
                19->{
                    binding.viber.isSelected=true
                }
                else->{

                }
            }
            previousType=newType
        }

    }
    fun openFilters(){
        binding.fav.visibility=View.GONE
        binding.delete.visibility=View.GONE
        binding.touchLayout.visibility=View.VISIBLE
        binding.dimBackground.visibility=View.VISIBLE
        binding.viewPager2.backgroundTintList=ContextCompat.getColorStateList(fragmentActivity,R.color.dimColor)
        binding.filter.setImageResource(R.drawable.ic_filter_selected)
        val slideDownAnimation: Animation = AnimationUtils.loadAnimation(fragmentActivity, R.anim.history_filter_animation)
        binding.filterLayout.visibility=View.VISIBLE
        binding.filterLayout.startAnimation(slideDownAnimation)
    }
    fun closeFilters(){
        binding.fav.visibility=View.VISIBLE
        binding.delete.visibility=View.VISIBLE

        binding.touchLayout.visibility=View.GONE
        binding.dimBackground.visibility=View.GONE
        binding.filter.setImageResource(R.drawable.ic_filter)
        binding.viewPager2.backgroundTintList=ContextCompat.getColorStateList(fragmentActivity,R.color.lightWhiteMainBackground)

        val slideDownAnimation: Animation = AnimationUtils.loadAnimation(fragmentActivity, R.anim.history_filter_reverse_animation)
        binding.filterLayout.startAnimation(slideDownAnimation)
        binding.filterLayout.visibility=View.GONE
    }


    var scanFrag= ScanFragmentInHistory()
    var createFrag= CreateFragmentInHistory()

    lateinit var homeViewPager: HomeViewPager
    fun createFragments(){
        val fragList= arrayListOf(
            scanFrag,
            createFrag,
        )

        homeViewPager= HomeViewPager(fragmentActivity,fragList)
        binding.viewPager2.adapter= homeViewPager
        binding.viewPager2.offscreenPageLimit= homeViewPager.itemCount


        val typedValue = TypedValue()
        val theme: Resources.Theme = fragmentActivity.getTheme()
        theme.resolveAttribute(R.attr.greenAppColor, typedValue, true)
        @ColorInt val greenAppColor: Int = typedValue.data

        binding.viewPager2.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback(){

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    when(position) {
                        0->{
                            when(fragmentActivity.sharedPref.appTheme){
                                0->{
                            binding.scanTabText.setTextColor(ContextCompat.getColorStateList(fragmentActivity,R.color.greenAppColor))
                            binding.scanTabSelector.backgroundTintList= ContextCompat.getColorStateList(fragmentActivity,R.color.greenAppColor)


                                }
                                1->{
                            binding.scanTabText.setTextColor(ContextCompat.getColorStateList(fragmentActivity,R.color.blueThemeColor))
                            binding.scanTabSelector.backgroundTintList= ContextCompat.getColorStateList(fragmentActivity,R.color.blueThemeColor)


                                }
                                2->{
                            binding.scanTabText.setTextColor(ContextCompat.getColorStateList(fragmentActivity,R.color.purpleThemeColor))
                            binding.scanTabSelector.backgroundTintList= ContextCompat.getColorStateList(fragmentActivity,R.color.purpleThemeColor)


                                }
                                3->{
                            binding.scanTabText.setTextColor(ContextCompat.getColorStateList(fragmentActivity,R.color.yellowThemeColor))
                            binding.scanTabSelector.backgroundTintList= ContextCompat.getColorStateList(fragmentActivity,R.color.yellowThemeColor)


                                }
                                4->{
                            binding.scanTabText.setTextColor(ContextCompat.getColorStateList(fragmentActivity,R.color.pinkThemeColor))
                            binding.scanTabSelector.backgroundTintList= ContextCompat.getColorStateList(fragmentActivity,R.color.pinkThemeColor)


                                }
                            }

//                            binding.scanTabText.setTextColor(ContextCompat.getColorStateList(fragmentActivity,greenAppColor))
//                            binding.scanTabSelector.backgroundTintList= ContextCompat.getColorStateList(fragmentActivity,greenAppColor)
                            binding.createTabText.setTextColor(ContextCompat.getColorStateList(fragmentActivity,R.color.greyTextColorSplash))
                            binding.createTabSelector.backgroundTintList= ContextCompat.getColorStateList(fragmentActivity,R.color.greyTextColorSplash)
                            binding.createTabSelector.visibility=View.GONE
                            binding.scanTabSelector.visibility=View.VISIBLE
                        }
                        1->{

                            when(fragmentActivity.sharedPref.appTheme){
                                0->{
                                    binding.createTabText.setTextColor(ContextCompat.getColorStateList(fragmentActivity,R.color.greenAppColor))
                                    binding.createTabSelector.backgroundTintList= ContextCompat.getColorStateList(fragmentActivity,R.color.greenAppColor)

                                }
                                1->{
                                    binding.createTabText.setTextColor(ContextCompat.getColorStateList(fragmentActivity,R.color.blueThemeColor))
                                    binding.createTabSelector.backgroundTintList= ContextCompat.getColorStateList(fragmentActivity,R.color.blueThemeColor)

                                }
                                2->{
                                    binding.createTabText.setTextColor(ContextCompat.getColorStateList(fragmentActivity,R.color.purpleThemeColor))
                                    binding.createTabSelector.backgroundTintList= ContextCompat.getColorStateList(fragmentActivity,R.color.purpleThemeColor)

                                }
                                3->{

                                    binding.createTabText.setTextColor(ContextCompat.getColorStateList(fragmentActivity,R.color.yellowThemeColor))
                                    binding.createTabSelector.backgroundTintList= ContextCompat.getColorStateList(fragmentActivity,R.color.yellowThemeColor)

                                }
                                4->{
                                    binding.createTabText.setTextColor(ContextCompat.getColorStateList(fragmentActivity,R.color.pinkThemeColor))
                                    binding.createTabSelector.backgroundTintList= ContextCompat.getColorStateList(fragmentActivity,R.color.pinkThemeColor)

                                }
                            }

                            binding.scanTabText.setTextColor(ContextCompat.getColorStateList(fragmentActivity,R.color.greyTextColorSplash))
                            binding.scanTabSelector.backgroundTintList= ContextCompat.getColorStateList(fragmentActivity,R.color.greyTextColorSplash)
                            binding.createTabSelector.visibility=View.VISIBLE
                            binding.scanTabSelector.visibility=View.GONE
//                            binding.createTabText.setTextColor(ContextCompat.getColorStateList(fragmentActivity,greenAppColor))
//                            binding.createTabSelector.backgroundTintList= ContextCompat.getColorStateList(fragmentActivity,greenAppColor)
                        }

                    }
                }
            })

        binding.scanTab.setOnClickListener{
            binding.viewPager2.currentItem=0
        }
        binding.createTab.setOnClickListener{
            binding.viewPager2.currentItem=1
        }
    }

    fun disableDelete(){
        if(this::binding.isInitialized){

            closeDelete()
            scanFrag.disableDelete()
            createFrag.disableDelete()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if(this::binding.isInitialized){

            isDeleteAllDialogOpen=false
            closeDelete()
            scanFrag.disableDelete()
            createFrag.disableDelete()
        }
    }

}