package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.databinding.ItemWifiBinding
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.modelClass.WifiModelClass
import java.util.ArrayList


class ItemWifiView(val binding: ItemWifiBinding) :
    RecyclerView.ViewHolder(binding.root)

class WifiAdapter(val context: Context,
                  var wifiFieldsList: ArrayList<WifiModelClass>,
                  val callback:(lanCode:String)->Unit
) : RecyclerView.Adapter<ItemWifiView>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemWifiView {

        return ItemWifiView(ItemWifiBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }


    override fun onBindViewHolder(holder: ItemWifiView, position: Int) {

        val item= wifiFieldsList[holder.layoutPosition]

        holder.binding.field.text=item.field
        holder.binding.value.text=item.value



    }




    override fun getItemCount(): Int {
        return wifiFieldsList.size
    }
}
