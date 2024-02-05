package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.R
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.databinding.ItemHistoryBinding
import qrcodescanner.qrcodereader.barcodescanner.barcodereader.dataBase.DataModel
import ezvcard.Ezvcard
import ezvcard.VCard
import java.util.HashMap


class ItemBarcodeDataView(val binding: ItemHistoryBinding) :
    RecyclerView.ViewHolder(binding.root)

class ScanInHistoryAdapter(val context: Context,
                    var barcodeList:ArrayList<DataModel>,
                    val callback:(longPressed:Boolean,isDeleteClicked:Boolean,holderPosition:Int)->Unit
                    ) : RecyclerView.Adapter<ItemBarcodeDataView>() {//  val callback:(holderPosition:Int,longPressed:Boolean,holderView:View)->Unit

    val barcodeIconMap: HashMap<Int, Int> = hashMapOf(
        11 to R.drawable.ic_event,
        1 to R.drawable.ic_contact,
        2 to R.drawable.ic_mail,
        10 to R.drawable.ic_location,//location
        4 to R.drawable.ic_phone,
        5 to R.drawable.ic_product,//product
        6 to R.drawable.ic_message,
        7 to R.drawable.ic_text,
        8 to R.drawable.ic_website,
        9 to R.drawable.ic_wifi,
        15 to R.drawable.ic_facebook,
        16 to R.drawable.ic_instagram,
        17 to R.drawable.ic_twitter,
        18 to R.drawable.ic_whatsapp,
        19 to R.drawable.ic_viber,
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemBarcodeDataView {
        return ItemBarcodeDataView(ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }
    var longPressed=false
    var count=0
    override fun onBindViewHolder(holder: ItemBarcodeDataView, position: Int) {

        var item= barcodeList[holder.layoutPosition]

        Glide.with(context)
            .load(barcodeIconMap.get(item.barcodeType))
            .into(holder.binding.barcodeIcon)

        if(item.barcodeType==1)
        {
            holder.binding.barcodeValue.text=getContactName(item.barValue)
        }
        else{
            holder.binding.barcodeValue.text=item.barValue
        }

        holder.binding.createdDate.text=item.creationTime

        if(!item.note.equals(""))
        {
            holder.binding.barcodeNote.visibility=View.VISIBLE
            holder.binding.barcodeNote.text=item.note
        }
        else{
            holder.binding.barcodeNote.visibility=View.GONE
        }

        if(item.isFav) {
            holder.binding.favIcon.visibility=View.VISIBLE
        }
        else{
            holder.binding.favIcon.visibility=View.GONE
        }

        if(item.isSelected){
            holder.binding.favIcon.visibility=View.GONE
            holder.binding.delete.visibility=View.VISIBLE
        }else{
            holder.binding.delete.visibility=View.GONE
            if(item.isFav){

                holder.binding.favIcon.visibility=View.VISIBLE
            }else{
                holder.binding.favIcon.visibility=View.GONE
            }
        }

        holder.itemView.setOnClickListener{

            if(!longPressed)
            {
                callback.invoke(false,false,holder.layoutPosition)
            }

        }

        holder.itemView.setOnLongClickListener(OnLongClickListener { v ->

            if(!longPressed){

                longPressed=true
                barcodeList.forEach { it.isSelected=true }
                notifyDataSetChanged()
                callback.invoke(true,false,holder.layoutPosition)
            }

            true
        })
        holder.binding.delete.setOnClickListener{
            Log.d("porisitonAdapter",holder.layoutPosition.toString())
            callback.invoke(true,true,holder.layoutPosition)
        }
    }
    override fun getItemCount(): Int {
        return barcodeList.size
    }

    fun getContactName(barcodeValue:String):String
    {
        val vcard: VCard? = Ezvcard.parse(barcodeValue).first()
        var formattedBValue=""

        var fullNameFound=false
        vcard?.formattedName?.let {
            fullNameFound=true
            formattedBValue= vcard.formattedName.value// first name and last name
        }
       if(!fullNameFound){

           vcard?.structuredName?.family?.let {
               formattedBValue= vcard.structuredName.family// first name and last name
           }
       }
        return formattedBValue
    }

    fun removeAtSpecific(position: Int){
        Log.d("porisitonadapter2",position.toString())
        barcodeList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun enableLongPress(checker:Boolean){
        if(checker){
            longPressed=true
            barcodeList.forEach { it.isSelected=true }

        }else{
            longPressed=false
            barcodeList.forEach { it.isSelected=false }
        }
        notifyDataSetChanged()

    }

}
