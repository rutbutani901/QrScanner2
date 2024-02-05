package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.databinding.ItemCountryBinding
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.extension.sharedPref
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.modelClass.CountryModel


class ItemCountryView(val binding: ItemCountryBinding) : RecyclerView.ViewHolder(binding.root)


class CountryListAdapter(val context: Context,
                         val countryList:ArrayList<CountryModel>,
                         val callback:(code:String)->Unit) : RecyclerView.Adapter<ItemCountryView>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemCountryView {
        return ItemCountryView(ItemCountryBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    }

    override fun getItemCount(): Int {

        return countryList.size
    }


    override fun onBindViewHolder(holder: ItemCountryView, position: Int) {

        val item = countryList[holder.layoutPosition]


        Glide.with(context)
            .load(item.icon)
            .into(holder.binding.flag)

        holder.binding.countryName.text=item.title

        if(item.code==context.sharedPref.countryCodeSearchOption){
            holder.binding.tick.visibility= View.VISIBLE
        }else{
            holder.binding.tick.visibility= View.GONE
        }

        holder.itemView.setOnClickListener{
            callback.invoke(item.code)
        }

    }



}