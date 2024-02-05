package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.databinding.ItemWebisteBinding


class ItemWebsiteView(val binding: ItemWebisteBinding) :
    RecyclerView.ViewHolder(binding.root)

class WebsiteAdapter(val context: Context,
                      var websiteList:ArrayList<String>,
                      val callback:(website:String)->Unit
) : RecyclerView.Adapter<ItemWebsiteView>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemWebsiteView {

        return ItemWebsiteView(ItemWebisteBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }


    override fun onBindViewHolder(holder: ItemWebsiteView, position: Int) {

        val item= websiteList[holder.layoutPosition]

        holder.binding.webiteText.text=item

        holder.itemView.setOnClickListener{
            callback.invoke(item)
        }

    }




    override fun getItemCount(): Int {
        return websiteList.size
    }
}
