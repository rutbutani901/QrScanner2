package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.databinding.ItemShoppingBinding

import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.modelClass.ShoppingModelClass


class ItemShoppingView(val itemBinding: ItemShoppingBinding) :
    RecyclerView.ViewHolder(itemBinding.root)

class ShoppingAdapter(val context: Context,
                      var dataList: ArrayList<ShoppingModelClass>,
                      val callback:(name:String)->Unit
) : RecyclerView.Adapter<ItemShoppingView>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemShoppingView {

        return ItemShoppingView(ItemShoppingBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }


    override fun onBindViewHolder(holder: ItemShoppingView, position: Int) {

        val item = dataList[holder.layoutPosition]


        Glide.with(context)
            .load(item.icon)
            .into(holder.itemBinding.imageView)

        val text=item.title
        holder.itemBinding.textView.text=text

        holder.itemView.setOnClickListener{
            callback.invoke(text)
        }


    }




    override fun getItemCount(): Int {
        return dataList.size
    }
}
