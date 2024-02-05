package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.databinding.ItemTextBinding


class ItemTextView(val binding: ItemTextBinding) :
    RecyclerView.ViewHolder(binding.root)

class TextAdapter(val context: Context,
                     var textList:ArrayList<String>,
                     val callback:(lanCode:String)->Unit
) : RecyclerView.Adapter<ItemTextView>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemTextView {

        return ItemTextView(ItemTextBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ItemTextView, position: Int) {

        val item= textList[holder.layoutPosition]
        holder.binding.inputText.text=item
    }

    override fun getItemCount(): Int {
        return textList.size
    }
}
