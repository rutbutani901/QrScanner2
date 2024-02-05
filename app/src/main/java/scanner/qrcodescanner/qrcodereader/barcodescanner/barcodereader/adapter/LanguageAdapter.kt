package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.R
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.databinding.ItemLanguageBinding
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.sharedPref
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.modelClass.LanguageModelClass


class ItemLanguageView(val itemBinding: ItemLanguageBinding) :
    RecyclerView.ViewHolder(itemBinding.root)

class LanguageAdapter(val context: Context,
                      var langList:ArrayList<LanguageModelClass>,
                      val callback:(lanCode:String)->Unit
) : RecyclerView.Adapter<ItemLanguageView>() {

//    @ColorInt var greenAppColor:Int?=null
//    init {
//        val typedValue = TypedValue()
//        val theme: Resources.Theme = context.getTheme()
//        theme.resolveAttribute(R.attr.greenAppColor, typedValue, true)
////        @ColorInt val greenAppColor: Int = typedValue.data
//         greenAppColor = typedValue.data
//    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemLanguageView {

        return ItemLanguageView(ItemLanguageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }


    override fun onBindViewHolder(holder: ItemLanguageView, position: Int) {

        val listData= langList[holder.layoutPosition]

        Glide.with(context)
            .load(listData.lanIcon)
            .into(holder.itemBinding.lanuageIcon)



        if(listData.isSelected){

            holder.itemBinding.mainCardView.background=ContextCompat.getDrawable(context, R.drawable.rounded_corners_lang_stroke)
            holder.itemBinding.languageTickIcon.setImageResource(R.drawable.ic_on_radio)

            when(context.sharedPref.appTheme){
                0->{
                      holder.itemBinding.languageTickIcon.imageTintList=ContextCompat.getColorStateList(context,R.color.greenAppColor)

                }
                1->{

                      holder.itemBinding.languageTickIcon.imageTintList=ContextCompat.getColorStateList(context,R.color.blueThemeColor)
                }
                2->{

                      holder.itemBinding.languageTickIcon.imageTintList=ContextCompat.getColorStateList(context,R.color.purpleThemeColor)
                }
                3->{

                      holder.itemBinding.languageTickIcon.imageTintList=ContextCompat.getColorStateList(context,R.color.yellowThemeColor)
                }
                4->{

                      holder.itemBinding.languageTickIcon.imageTintList=ContextCompat.getColorStateList(context,R.color.pinkThemeColor)
                }
            }


//            if(greenAppColor!=null) {
//
//                holder.itemBinding.languageTickIcon.imageTintList=ContextCompat.getColorStateList(context,greenAppColor!!)
//            }else{
//                holder.itemBinding.languageTickIcon.imageTintList=ContextCompat.getColorStateList(context,R.color.greenAppColor)
//            }

            holder.itemBinding.languageTickIcon.setPadding(15,15,15,15)
        }
        else{
            holder.itemBinding.mainCardView.background=ContextCompat.getDrawable(context, R.drawable.shape_language_holder)

            holder.itemBinding.languageTickIcon.setImageResource(R.drawable.ic_off_radio)
            holder.itemBinding.languageTickIcon.setPadding(15,15,15,15)
            holder.itemBinding.languageTickIcon.imageTintList=ContextCompat.getColorStateList(context,R.color.unselectedLangOffRadioIconColor)
        }

        holder.itemBinding.lanuageName.text=listData.lanName

        holder.itemView.setOnClickListener{

            val prevSelectedIndex=langList.indexOfFirst { it.isSelected==true }
            if(prevSelectedIndex!=-1){

                langList[prevSelectedIndex].isSelected=false
                listData.isSelected=true

                callback.invoke(listData.lanCode)

                notifyItemChanged(holder.layoutPosition)
                notifyItemChanged(prevSelectedIndex)
            }
        }
    }




    override fun getItemCount(): Int {
        return langList.size
    }
}
