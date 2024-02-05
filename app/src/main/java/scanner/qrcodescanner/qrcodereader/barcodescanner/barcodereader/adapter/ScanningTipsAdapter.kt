package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.R
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.databinding.ItemSupportedCodesBinding
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.sharedPref
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.modelClass.SupportedCodesModelClass

//import com.google.android.gms.ads.nativead.NativeAd
//import qrcodescanner.qrcodereader.barcodescanner.barcodereader.R
//import qrcodescanner.qrcodereader.barcodescanner.barcodereader.ads.AdEventListener
//import qrcodescanner.qrcodereader.barcodescanner.barcodereader.ads.AdHandler
//import qrcodescanner.qrcodereader.barcodescanner.barcodereader.databinding.ItemOtherBarcodesBinding
//import qrcodescanner.qrcodereader.barcodescanner.barcodereader.databinding.ItemSupportedCodeNativeHolderBinding
//import qrcodescanner.qrcodereader.barcodescanner.barcodereader.databinding.ItemSupportedCodesBinding
//import qrcodescanner.qrcodereader.barcodescanner.barcodereader.modelClass.SupportedCodesModelClass
//import qrcodescanner.qrcodereader.barcodescanner.barcodereader.util.Constant


//class ItemScanningFormatNativeHolder(val scanningFormatNativeAdHolderBinding: ItemSupportedCodeNativeHolderBinding) :
//    RecyclerView.ViewHolder(scanningFormatNativeAdHolderBinding.root)

class ItemScanningFormat(val bindingScan: ItemSupportedCodesBinding) :
    RecyclerView.ViewHolder(bindingScan.root)


class ScanningTipsAdapter(val context: Context, val codeList:ArrayList<SupportedCodesModelClass>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemScanningFormat(ItemSupportedCodesBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    }

    override fun getItemCount(): Int {

        return codeList.size
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val item = codeList[holder.layoutPosition]

        (holder as ItemScanningFormat).bindingScan.supportedCodesTitle.text= (item as SupportedCodesModelClass).codeTitle

        Glide.with(context)
            .load(item.codeIcon)
            .into(holder.bindingScan.icon)

        holder.bindingScan.isSupportedIcon.imageTintList= ContextCompat.getColorStateList(context, R.color.white)

        if(item.isSupported){

            holder.bindingScan.isSupportedIcon.background= ContextCompat.getDrawable(context, R.drawable.shape_circle)
            when(context.sharedPref.appTheme){
                0->{
                      holder.bindingScan.isSupportedIcon.backgroundTintList=ContextCompat.getColorStateList(context,R.color.greenAppColor)

                }
                1->{

                      holder.bindingScan.isSupportedIcon.backgroundTintList=ContextCompat.getColorStateList(context,R.color.blueThemeColor)
                }
                2->{

                      holder.bindingScan.isSupportedIcon.backgroundTintList=ContextCompat.getColorStateList(context,R.color.purpleThemeColor)
                }
                3->{

                      holder.bindingScan.isSupportedIcon.backgroundTintList=ContextCompat.getColorStateList(context,R.color.yellowThemeColor)
                }
                4->{

                      holder.bindingScan.isSupportedIcon.backgroundTintList=ContextCompat.getColorStateList(context,R.color.pinkThemeColor)
                }
            }

//            holder.bindingScan.isSupportedIcon.backgroundTintList= ContextCompat.getColorStateList(context, R.color.greenAppColor)
            holder.bindingScan.isSupportedIcon.setImageResource(R.drawable.ic_theme_tick_icon)

            holder.bindingScan.isSupportedIcon.setPadding(15)
        }else{

            holder.bindingScan.isSupportedIcon.background= ContextCompat.getDrawable(context, R.drawable.shape_circle)
            holder.bindingScan.isSupportedIcon.backgroundTintList= ContextCompat.getColorStateList(context, R.color.red)
            holder.bindingScan.isSupportedIcon.setImageResource(R.drawable.cross_icon)
            holder.bindingScan.isSupportedIcon.setPadding(12)
        }
    }



}