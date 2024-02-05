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


class ItemSupportedCodes(val binding: ItemSupportedCodesBinding) :
    RecyclerView.ViewHolder(binding.root)


class SupportedCodesAdapter(val context: Context, val codeList: ArrayList<SupportedCodesModelClass>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemSupportedCodes(
            ItemSupportedCodesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {


        val item = codeList[holder.layoutPosition]

        (holder as ItemSupportedCodes).binding.supportedCodesTitle.text =
            item.codeTitle
        Glide.with(context)
            .load(item.codeIcon)
            .into(holder.binding.icon)

        holder.binding.isSupportedIcon.imageTintList =
            ContextCompat.getColorStateList(context, R.color.white)

        if (item.isSupported) {

            holder.binding.isSupportedIcon.background = ContextCompat.getDrawable(context, R.drawable.shape_circle)
            when(context.sharedPref.appTheme){
                0->{
                   holder.binding.isSupportedIcon.backgroundTintList=ContextCompat.getColorStateList(context,R.color.greenAppColor)

                }
                1->{

                   holder.binding.isSupportedIcon.backgroundTintList=ContextCompat.getColorStateList(context,R.color.blueThemeColor)
                }
                2->{

                   holder.binding.isSupportedIcon.backgroundTintList=ContextCompat.getColorStateList(context,R.color.purpleThemeColor)
                }
                3->{

                   holder.binding.isSupportedIcon.backgroundTintList=ContextCompat.getColorStateList(context,R.color.yellowThemeColor)
                }
                4->{

                   holder.binding.isSupportedIcon.backgroundTintList=ContextCompat.getColorStateList(context,R.color.pinkThemeColor)
                }
            }
//            holder.binding.isSupportedIcon.backgroundTintList = ContextCompat.getColorStateList(context, R.color.greenAppColor)
            holder.binding.isSupportedIcon.setImageResource(R.drawable.ic_theme_tick_icon)

            holder.binding.isSupportedIcon.setPadding(15)
        } else {

            holder.binding.isSupportedIcon.background =
                ContextCompat.getDrawable(context, R.drawable.shape_circle)
            holder.binding.isSupportedIcon.backgroundTintList =
                ContextCompat.getColorStateList(context, R.color.red)
            holder.binding.isSupportedIcon.setImageResource(R.drawable.cross_icon)
            holder.binding.isSupportedIcon.setPadding(12)
        }
    }

    override fun getItemCount(): Int {

        return codeList.size
    }


}