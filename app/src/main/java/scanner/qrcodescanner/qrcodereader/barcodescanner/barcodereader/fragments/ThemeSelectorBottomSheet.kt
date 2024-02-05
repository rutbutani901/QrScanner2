package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ThemeSelectorBottomSheet:BottomSheetDialogFragment() {




    var seletecdTheme=-1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.theme_selector_layout, container, false)

        arguments?.let {
            seletecdTheme=it.getInt("selectedTheme")
        }

        when(seletecdTheme){
            0->{

                changeTickIconPosition(0,view)
//                view.findViewById<ConstraintLayout>(R.id.applyTheme).background=ContextCompat.getDrawable(requireContext(),R.drawable.gradient_green_rounded_corners_8sdp)
            }
            1->{

                changeTickIconPosition(1,view)
//                view.findViewById<ConstraintLayout>(R.id.applyTheme).background=ContextCompat.getDrawable(requireContext(),R.drawable.blue_gradient)

            }
            2->{

                changeTickIconPosition(2,view)
//                view.findViewById<ConstraintLayout>(R.id.applyTheme).background=ContextCompat.getDrawable(requireContext(),R.drawable.purple_gradient)
            }
            3->{

                changeTickIconPosition(3,view)
//                view.findViewById<ConstraintLayout>(R.id.applyTheme).background=ContextCompat.getDrawable(requireContext(),R.drawable.yello_graident)
            }
            4->{

                changeTickIconPosition(4,view)
//                view.findViewById<ConstraintLayout>(R.id.applyTheme).background=ContextCompat.getDrawable(requireContext(),R.drawable.pink_gradient)
            }

        }

        view.findViewById<ImageView>(R.id.back).setOnClickListener {
            dismiss()
        }

        view.findViewById<ImageView>(R.id.green).setOnClickListener {
            view.findViewById<ConstraintLayout>(R.id.applyTheme).background=ContextCompat.getDrawable(requireContext(),R.drawable.gradient_green_rounded_corners_8sdp)

           changeTickIconPosition(0,view)
        }
        view.findViewById<ImageView>(R.id.blue).setOnClickListener {
                view.findViewById<ConstraintLayout>(R.id.applyTheme).background=ContextCompat.getDrawable(requireContext(),R.drawable.blue_gradient)
           changeTickIconPosition(1,view)

        }
        view.findViewById<ImageView>(R.id.purple).setOnClickListener {
                view.findViewById<ConstraintLayout>(R.id.applyTheme).background=ContextCompat.getDrawable(requireContext(),R.drawable.purple_gradient)
           changeTickIconPosition(2,view)
        }
        view.findViewById<ImageView>(R.id.yellow).setOnClickListener {
                view.findViewById<ConstraintLayout>(R.id.applyTheme).background=ContextCompat.getDrawable(requireContext(),R.drawable.yello_graident)

           changeTickIconPosition(3,view)
        }
        view.findViewById<ImageView>(R.id.pink).setOnClickListener {
                view.findViewById<ConstraintLayout>(R.id.applyTheme).background=ContextCompat.getDrawable(requireContext(),R.drawable.pink_gradient)

           changeTickIconPosition(4,view)
        }
        view.findViewById<ConstraintLayout>(R.id.applyTheme).setOnClickListener {

            parentFragmentManager.setFragmentResult("requestKey", bundleOf("bundleKey" to "${seletecdTheme}"))
            dismiss()
        }
        return view
    }

    fun changeTickIconPosition(current:Int,tickView:View){
        when(current){
            0->{
                seletecdTheme=0
                tickView.findViewById<ImageView>(R.id.greenTick).visibility=View.VISIBLE
                tickView.findViewById<ImageView>(R.id.blueTick).visibility=View.GONE
                tickView.findViewById<ImageView>(R.id.purpleTick).visibility=View.GONE
                tickView.findViewById<ImageView>(R.id.yellowTick).visibility=View.GONE
                tickView.findViewById<ImageView>(R.id.pinkTick).visibility=View.GONE
            }
            1->{
                seletecdTheme=1
                tickView.findViewById<ImageView>(R.id.greenTick).visibility=View.GONE
                 tickView.findViewById<ImageView>(R.id.blueTick).visibility=View.VISIBLE
                tickView.findViewById<ImageView>(R.id.purpleTick).visibility=View.GONE
                tickView.findViewById<ImageView>(R.id.yellowTick).visibility=View.GONE
                tickView.findViewById<ImageView>(R.id.pinkTick).visibility=View.GONE
            }
            2->{
                seletecdTheme=2
                tickView.findViewById<ImageView>(R.id.greenTick).visibility=View.GONE
                 tickView.findViewById<ImageView>(R.id.blueTick).visibility=View.GONE
                tickView.findViewById<ImageView>(R.id.purpleTick).visibility=View.VISIBLE
                tickView.findViewById<ImageView>(R.id.yellowTick).visibility=View.GONE
                tickView.findViewById<ImageView>(R.id.pinkTick).visibility=View.GONE
            }
            3->{
                seletecdTheme=3
                tickView.findViewById<ImageView>(R.id.greenTick).visibility=View.GONE
                 tickView.findViewById<ImageView>(R.id.blueTick).visibility=View.GONE
                tickView.findViewById<ImageView>(R.id.purpleTick).visibility=View.GONE
                tickView.findViewById<ImageView>(R.id.yellowTick).visibility=View.VISIBLE
                tickView.findViewById<ImageView>(R.id.pinkTick).visibility=View.GONE
            }
            4->{
                seletecdTheme=4
                tickView.findViewById<ImageView>(R.id.greenTick).visibility=View.GONE
                 tickView.findViewById<ImageView>(R.id.blueTick).visibility=View.GONE
                tickView.findViewById<ImageView>(R.id.purpleTick).visibility=View.GONE
                tickView.findViewById<ImageView>(R.id.yellowTick).visibility=View.GONE
                tickView.findViewById<ImageView>(R.id.pinkTick).visibility=View.VISIBLE
            }
        }

    }
}