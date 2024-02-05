package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.NetworkChangeReceiver
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.R
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.activity.InAppPurchaseActivity
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.adapter.ShoppingAdapter
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.ads.AdEventListener
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.ads.AdHandler
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.databinding.FragmentShoppingBinding
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.sharedPref
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.modelClass.ShoppingModelClass
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.util.Constants
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.util.InternetChecker
import com.google.android.gms.ads.nativead.NativeAd


class ShoppingFragment : Fragment() {

    lateinit var fragmentActivity: FragmentActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentActivity=context as FragmentActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(activity!=null) fragmentActivity=activity as FragmentActivity
    }

    lateinit var binding:FragmentShoppingBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentShoppingBinding.inflate(layoutInflater)

        if(fragmentActivity.sharedPref.isPremiumPurchased){
            binding.pro.visibility=View.GONE
        }else{
            binding.pro.visibility=View.VISIBLE
        }

        val categoryList= arrayListOf<ShoppingModelClass>(
            ShoppingModelClass(R.drawable.sh_music,getString(R.string.music)),
            ShoppingModelClass(R.drawable.sh_electronics,getString(R.string.elec)),
            ShoppingModelClass(R.drawable.sh_toys,getString(R.string.toys)),
            ShoppingModelClass(R.drawable.sh_cosmetic,getString(R.string.cosmetic)),
            ShoppingModelClass(R.drawable.sh_clothing,getString(R.string.women)),
            ShoppingModelClass(R.drawable.sh_food,getString(R.string.food)),
            ShoppingModelClass(R.drawable.sh_grocery,getString(R.string.grocery)),
            ShoppingModelClass(R.drawable.sh_foods,getString(R.string.food2))
        )
        val recommendList= arrayListOf<ShoppingModelClass>(
            ShoppingModelClass(R.drawable.sh_chips,getString(R.string.argente)),
            ShoppingModelClass(R.drawable.sh_water,getString(R.string.evian)),
            ShoppingModelClass(R.drawable.sh_buscuit_stick,getString(R.string.biscuit)),
            ShoppingModelClass(R.drawable.sh_wallmart,getString(R.string.salted)),
            ShoppingModelClass(R.drawable.sh_full_cream_milk,getString(R.string.full)),
            ShoppingModelClass(R.drawable.sh_mandolarian,getString(R.string.mando)),
            ShoppingModelClass(R.drawable.sh_spiderman,getString(R.string.spider)),
            ShoppingModelClass(R.drawable.sh_mario_kart,getString(R.string.mario))
        )
        val storeList= arrayListOf<ShoppingModelClass>(
            ShoppingModelClass(R.drawable.sh_amazon,getString(R.string.amazon)),
            ShoppingModelClass(R.drawable.sh_target,getString(R.string.target)),
            ShoppingModelClass(R.drawable.sh_flipcart,getString(R.string.flipcart)),
            ShoppingModelClass(R.drawable.sh_walmart,getString(R.string.walmart)),
            ShoppingModelClass(R.drawable.sh_ebay,getString(R.string.ebay)),
            ShoppingModelClass(R.drawable.sh_shein,getString(R.string.shein)),
            ShoppingModelClass(R.drawable.sh_best_buy,getString(R.string.bestbuy)),
            ShoppingModelClass(R.drawable.sh_new_egg,getString(R.string.newegg))
        )

        binding.pro.setOnClickListener{
            startActivity(Intent(fragmentActivity, InAppPurchaseActivity::class.java))
        }
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
                if(isConnected){
                    binding.checkInternetLayout.visibility=View.GONE
                    binding.contentInScroll.visibility=View.VISIBLE

                    loadNewNativeAd()

                }else{
                    binding.contentInScroll.visibility=View.GONE
                    binding.checkInternetLayout.visibility=View.VISIBLE
                }
            }
        })

        setCategoryAdapter(categoryList)
        setRecommendAdapter(recommendList)
        setStoresAdapter(storeList)


        return binding.root
    }
    var networkCheckerLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result->
        Constants.openOtherScreenFromApp=false
        isNetworkAvailable()
    }

    override fun onDestroy() {
        super.onDestroy()
        Constants.openOtherScreenFromApp=false
    }
    fun isNetworkAvailable(){
        if(this::binding.isInitialized){
            if (InternetChecker.isConnectedToInternet(fragmentActivity) || InternetChecker.isConnectedToWifi(fragmentActivity)) {

                binding.checkInternetLayout.visibility=View.GONE
                binding.contentInScroll.visibility=View.VISIBLE

                loadNewNativeAd()

            } else {
                binding.checkInternetLayout.visibility=View.VISIBLE

            }
        }
    }


    lateinit var categoryAdapter: ShoppingAdapter
    fun setCategoryAdapter( categoryList: ArrayList<ShoppingModelClass>) {

        categoryAdapter = ShoppingAdapter(fragmentActivity, categoryList) { itemName ->

            searchForProduct(itemName)
        }
        binding.categoryRecycler.layoutManager = GridLayoutManager(fragmentActivity,4, GridLayoutManager.VERTICAL, false)
        binding.categoryRecycler.adapter = categoryAdapter

    }
    lateinit var recommendAdapter: ShoppingAdapter
    fun setRecommendAdapter( recommendList: ArrayList<ShoppingModelClass>) {

        recommendAdapter = ShoppingAdapter(fragmentActivity, recommendList) { itemName ->

            searchForProduct(itemName)
        }
        binding.recommendRecycler.layoutManager = GridLayoutManager(fragmentActivity,4, GridLayoutManager.VERTICAL, false)
        binding.recommendRecycler.adapter = recommendAdapter

    }
    lateinit var storesAdapter: ShoppingAdapter
    fun setStoresAdapter( storeList: ArrayList<ShoppingModelClass>) {

        storesAdapter = ShoppingAdapter(fragmentActivity, storeList) { itemName ->

            searchForProduct(itemName)
        }
        binding.onlineStoreRecycler.layoutManager = GridLayoutManager(fragmentActivity,4, GridLayoutManager.VERTICAL, false)
        binding.onlineStoreRecycler.adapter = storesAdapter

    }

    fun searchForProduct(itemName:String){
        Constants.openOtherScreenFromApp=true
        when(itemName){
            "Music" -> {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.ebay.com/b/Music/bn_7000710860"));
                startActivity(intent)
            }
            "Electronics" -> {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.bestbuy.com"));
                startActivity(intent)
            }
            "Toys" -> {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.ebay.com/b/Toys-Hobbies/220/bn_1865497"));
                startActivity(intent)
            }
            "Cosmetics & Skin care" -> {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.skinstore.com"));
                startActivity(intent)
            }
            "Woman's Clothing" -> {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.amazon.com/gp/browse.html?node=7147440011&linkCode=sl2&tag=sinpeak-20&linkId=59c3af6f154ece6193bc04894c1db6e8&language=en_US&ref_=as_li_ss_tl"));
                startActivity(intent)
            }
            "Food Gifts & candy" -> {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.walmart.com/cp/food-gifts-flowers-shop/1089004"));
                startActivity(intent)
            }
            "Grocery & Gourmet food" -> {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.amazon.com/Snacks-Chips-Cookies-Gum-Gluten-Free/b?ie=UTF8&node=16322721&linkCode=sl2&tag=sinpeak-20&linkId=8674382e3fc790e0fd0fa3b0d799fb75&language=en_US&ref_=as_li_ss_tl"));
                startActivity(intent)
            }
            "Food's" -> {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.amazon.com/alm/storefront?almBrandId=VUZHIFdob2xlIEZvb2Rz&linkCode=sl2&tag=sinpeak-20&linkId=8d9a6237585303d54419f695439ca6d8&language=en_US&ref_=as_li_ss_tl"));
                startActivity(intent)
            }


            "Argente Chips" -> {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.amazon.sg/gp/product/B07V6STW3V/ref=as_li_tl?ie=UTF8&camp=247&creative=1211&creativeASIN=B07V6STW3V&linkCode=as2&tag=sinpeak-22&linkId=7e6456176ca34a26b418abe4b8281ab6"));
                startActivity(intent)
            }

            "Evian Water" -> {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.amazon.sg/gp/product/B09BP6FTHN/ref=as_li_tl?ie=UTF8&camp=247&creative=1211&creativeASIN=B09BP6FTHN&linkCode=as2&tag=sinpeak-22&linkId=18f64d7ea74b92661fc490b2e16fb1c0"));
                startActivity(intent)
            }
            "Biscuit Stick" -> {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.amazon.sg/gp/product/B013RZGD5U/ref=as_li_tl?ie=UTF8&camp=247&creative=1211&creativeASIN=B013RZGD5U&linkCode=as2&tag=sinpeak-22&linkId=1b32c62d19b672bcc2621ddc9224ad5a"));
                startActivity(intent)
            }
            "Salted Egg Fish Skin" -> {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.amazon.sg/gp/product/B07BVB41CB/ref=as_li_tl?ie=UTF8&camp=247&creative=1211&creativeASIN=B07BVB41CB&linkCode=as2&tag=sinpeak-22&linkId=1159ef2ed5e03056290d8fb5c63d04b9"));
                startActivity(intent)
            }
            "Full Cream Milk" -> {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.amazon.sg/gp/product/B07LG7VYFV/ref=as_li_tl?ie=UTF8&camp=247&creative=1211&creativeASIN=B07LG7VYFV&linkCode=as2&tag=sinpeak-22&linkId=77905a82d55995a7640b32363e6cf292"));
                startActivity(intent)
            }
            "Mandalorian" -> {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.amazon.sg/gp/product/B09H1C5QS4/ref=as_li_tl?ie=UTF8&camp=247&creative=1211&creativeASIN=B09H1C5QS4&linkCode=as2&tag=sinpeak-22&linkId=72ec7fa71bb503e77f530874c3fc32a4"));
                startActivity(intent)
            }
            "Spider-Man Figure" -> {
                 val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.amazon.sg/gp/product/B09V6GB65W/ref=as_li_tl?ie=UTF8&camp=247&creative=1211&creativeASIN=B09V6GB65W&linkCode=as2&tag=sinpeak-22&linkId=35a080418aa5765f7bf9def751c1d9a9"));
                startActivity(intent)

            }
            "Mario Kart" -> {
                 val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.amazon.sg/gp/product/B01N1037CV/ref=as_li_tl?ie=UTF8&camp=247&creative=1211&creativeASIN=B01N1037CV&linkCode=as2&tag=sinpeak-22&linkId=0bf5dcd56324a412fc747b584d22a9c4"));
                startActivity(intent)

            }
            "Amazon" -> {
                 val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.amazon.com/"));
                startActivity(intent)

            }
            "Ebay" -> {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.ebay.com/"));
                startActivity(intent)

            }
            "BestBuy" -> {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.bestbuy.com/"));
                startActivity(intent)

            }
            "Wallmart" -> {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.walmart.com/"));
                startActivity(intent)

            }
            // mssing target icon
            "Target" -> {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.target.com/"));
                startActivity(intent)

            }
            "Shein" -> {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://m.shein.in/?ref=www&rep=dir&ret=min"));
                startActivity(intent)

            }
            // mssing cdiscount

            "Neweg" -> {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.newegg.com/areyouahuman?referer=https%3A%2F%2Fwww.newegg.com%2F"));
                startActivity(intent)

            }


            "Flipkart" -> {
                 val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.flipkart.com/"));
                startActivity(intent)

            }





        }

    }

    override fun onResume() {
        super.onResume()
        Constants.openOtherScreenFromApp=false
    }

    fun loadNewNativeAd() {

        if (!fragmentActivity.sharedPref.isPremiumPurchased) {
            if (scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.ads.AdHandler.getInstance(fragmentActivity).isNetworkAvailable(fragmentActivity)) {

                binding.adContainer.visibility = View.GONE
                binding.shimmerLayout.root.visibility = View.VISIBLE

                scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.ads.AdHandler.getInstance(fragmentActivity).loadNativeAd(
                    fragmentActivity,
                    getString(R.string.nativeAdvanced),
                    object :
                        scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.ads.AdEventListener {
                        override fun onAdLoaded(adObject: Any?) {
                            if (adObject != null) {

                                binding.adContainer.visibility = View.VISIBLE
                                binding.shimmerLayout.root.visibility = View.GONE

                                scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.ads.AdHandler.getInstance(fragmentActivity).showNativeAd(
                                    fragmentActivity,
                                    binding.adContainer,
                                    adObject as NativeAd, false
                                )
                            } else {
                                binding.adContainer.visibility = View.GONE
                                binding.shimmerLayout.root.visibility = View.GONE
                            }
                        }

                        override fun onAdClosed() {}
                        override fun onLoadError(errorCode: String?) {

                            binding.adContainer.visibility = View.GONE
                            binding.shimmerLayout.root.visibility = View.GONE
                        }
                    })

            } else {

                binding.adLinearLayout.visibility = View.GONE

            }
        }else{
            binding.adLinearLayout.visibility = View.GONE

        }


    }


}