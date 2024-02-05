package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.R
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.adapter.CountryListAdapter
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.modelClass.CountryModel
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.modelClass.SearchUrlsCountryModelClass
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.ArrayList

class CountryListBotomSheet : BottomSheetDialogFragment() {

    var urlList= arrayListOf(
        CountryModel(R.drawable.flag_default,
            "*",
            "*",

            ),
        CountryModel(R.drawable.flag_australia,
            "au",
            "Australia",

            ),
        CountryModel(R.drawable.flag_austria,"at","Austria",

            ),
        CountryModel(R.drawable.flag_belgium,"be","Belgium",
        ),
        CountryModel(R.drawable.flag_brazil,"br","Brazil",

            ),
        CountryModel(R.drawable.flag_canada,"ca","Canada",
        ),
        CountryModel(R.drawable.flag_china,"cn","China mainland",
        ),
        CountryModel(R.drawable.flag_czechia,"cz","Czechia",
        ),
        CountryModel(R.drawable.flag_denmark,"dk","Denmark",
        ),
        CountryModel(R.drawable.flag_finland,"fi","Finland",
        ),
        CountryModel(R.drawable.flag_france,"fr","France",
        ),//france
        CountryModel(R.drawable.flag_germany,"de","Germany",
        ),
        CountryModel(R.drawable.flag_hong_kong,"hk","Hong Kong",
        ),
        CountryModel(R.drawable.flag_india,"in","India",
        ),
        CountryModel(R.drawable.flag_ireland,"ie","Ireland",
        ),
        CountryModel(R.drawable.flag_italy,"it","Italy",
        ),
        CountryModel(R.drawable.flag_japan,"jp","Japan",
        ),
        CountryModel(R.drawable.flag_liechtenstein,"li","Liechtenstein",
        ),
        CountryModel(R.drawable.flag_luxembourg,"lu","Luxembourg",
        ),
        CountryModel(R.drawable.flag_malaysia,"my","Malaysia",
        ),
        CountryModel(R.drawable.flag_mexico,"mx","Mexico",
        ),
        CountryModel(R.drawable.flag_monaco,"mc","Monaco",
        ),
        CountryModel(R.drawable.flag_netherlands,"nl","Netherlands",
        ),
        CountryModel(R.drawable.flag_new_zealand,"nz","New Zealand",
        ),
        CountryModel(R.drawable.flag_norway,"no","Norway",
        ),
        CountryModel(R.drawable.flag_philippines,"ph","Philippines",
        ),
        CountryModel(R.drawable.flag_poland,"pl","Poland",
        ),
        CountryModel(R.drawable.flag_portugal,"pt","Portugal",
        ),
        CountryModel(R.drawable.flag_russia,"ru","Russia",
        ),
        CountryModel(R.drawable.flag_san_marino,"sm","San Marino",
        ),
        CountryModel(R.drawable.flag_singapore,"sg","Singapore",
        ),
        CountryModel(R.drawable.flag_south_korea,"kr","Sotuh Korea",
        ),
        CountryModel(R.drawable.flag_spain,"es","Spain",
        ),
        CountryModel(R.drawable.flag_sweden,"se","Sweden",
        ),
        CountryModel(R.drawable.flag_switzerland,"ch","Switzerland",
        ),
        CountryModel(R.drawable.flag_turkey,"tr","Turkey",
        ),
        CountryModel(R.drawable.flag_united_kingdom,"gb","United Kingdom",
        ),
        CountryModel(R.drawable.flag_us,"us","United States",
        )
    )
    var selectedCountry: SearchUrlsCountryModelClass?=null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomSheetBehavior = BottomSheetBehavior.from(view.parent as View)
        bottomSheetBehavior.peekHeight = resources.getDimensionPixelSize(R.dimen.peek_height)
    }

    override fun onStart() {
        super.onStart()

        val dialog = dialog
        if (dialog != null) {
            val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            val behavior = BottomSheetBehavior.from(bottomSheet)

            behavior.peekHeight = resources.getDimensionPixelSize(R.dimen.peek_height)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.botom_sheet_country, container, false)

        arguments?.let {
            selectedCountry=it.getSerializable("selectedCountry") as SearchUrlsCountryModelClass
        }




        view.findViewById<ImageView>(R.id.cross).setOnClickListener {
            dismiss()
        }

        setWebsiteAdapter(view,urlList)

        return view
    }

    lateinit var countryListAdapter: CountryListAdapter
    fun setWebsiteAdapter(view: View,website: ArrayList<CountryModel>) {

        countryListAdapter = CountryListAdapter(requireContext(), website) {code->

        parentFragmentManager.setFragmentResult("requestKey", bundleOf("bundleKey" to code))
        dismiss()

        }
        view.findViewById<RecyclerView>(R.id.recycler).layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        view.findViewById<RecyclerView>(R.id.recycler).adapter = countryListAdapter

    }

}






