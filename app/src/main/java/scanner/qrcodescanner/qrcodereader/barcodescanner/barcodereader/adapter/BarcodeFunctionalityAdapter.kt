package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.adapter



import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.databinding.ItemBarcodeFunctionalityBinding
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.modelClass.BarcodeFunctionalityModelClass
import java.util.ArrayList


class ItemGoogleView(val binding: ItemBarcodeFunctionalityBinding) :
    RecyclerView.ViewHolder(binding.root)

class BarcodeFunctionalityAdapter(val context: Context,
                                  var barcodeFunList: ArrayList<BarcodeFunctionalityModelClass>,
                                  val callback:(holderTitle:String,holderValue:String)->Unit
) : RecyclerView.Adapter<ItemGoogleView>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemGoogleView {

        return ItemGoogleView(ItemBarcodeFunctionalityBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }


    override fun onBindViewHolder(holder: ItemGoogleView, position: Int) {

        val item= barcodeFunList[holder.layoutPosition]

        holder.binding.siteText.text=item.title
        holder.binding.logo.setImageResource(item.icon)

        holder.itemView.setOnClickListener{
            callback.invoke(item.title,item.value.toString())
        }

    }

    override fun getItemCount(): Int {
        return barcodeFunList.size
    }
}
