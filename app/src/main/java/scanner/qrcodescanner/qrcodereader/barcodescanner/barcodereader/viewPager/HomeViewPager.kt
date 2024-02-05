package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.viewPager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class HomeViewPager(fragmentActivity: FragmentActivity,val fragList:ArrayList<Fragment>): FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return fragList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragList[position]
    }

}