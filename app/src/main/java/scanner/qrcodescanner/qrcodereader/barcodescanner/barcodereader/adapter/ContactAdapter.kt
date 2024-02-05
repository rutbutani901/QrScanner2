package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.databinding.ItemContactFieldsBinding
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.modelClass.ContactFieldsModelClass
import java.util.ArrayList


class ItemContactView(val binding: ItemContactFieldsBinding) :
    RecyclerView.ViewHolder(binding.root)

class ContactAdapter(val context: Context,
                     var contactFieldsList: ArrayList<ContactFieldsModelClass>,
                     val callback:(lanCode:String)->Unit
) : RecyclerView.Adapter<ItemContactView>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemContactView {

        return ItemContactView(ItemContactFieldsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }


    override fun onBindViewHolder(holder: ItemContactView, position: Int) {

        val item= contactFieldsList[holder.layoutPosition]

        holder.binding.value.text=item.fieldValue
        holder.binding.logo.setImageResource(item.fieldIcon)


    }




    override fun getItemCount(): Int {
        return contactFieldsList.size
    }
}
