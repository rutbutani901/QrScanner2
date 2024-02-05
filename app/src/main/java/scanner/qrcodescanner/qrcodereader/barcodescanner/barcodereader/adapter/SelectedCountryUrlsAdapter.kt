package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.databinding.ItemSelectedCountryUrlsBinding
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.modelClass.SearchCountryModel


class ItemSelectedCountry(val binding: ItemSelectedCountryUrlsBinding) : RecyclerView.ViewHolder(binding.root)


class SelectedCountryUrlsAdapter(val context: Context,
                                 val countryList:List<SearchCountryModel>,
                                 val callback:(code:String)->Unit) : RecyclerView.Adapter<ItemSelectedCountry>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemSelectedCountry {
        return ItemSelectedCountry(ItemSelectedCountryUrlsBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    }

    override fun getItemCount(): Int {

        return countryList.size
    }


    override fun onBindViewHolder(holder: ItemSelectedCountry, position: Int) {

        val item = countryList[holder.layoutPosition]


        Glide.with(context)
            .load(item.icon)
            .into(holder.binding.flag)

        holder.binding.title.text=item.title
        holder.binding.url.text=item.url

        holder.itemView.setOnClickListener{

        }

    }



}