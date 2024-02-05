package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.viewPager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.fragments.AppIntroFirstFragment
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.fragments.AppIntroFourthFragment
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.fragments.AppIntroSecondFragment
import scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.fragments.AppIntroThirdFragment

class AppIntroViewPager(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {

        var frag: Fragment?=null

        when(position)
        {
            0-> frag= AppIntroFirstFragment()
            1-> frag= AppIntroSecondFragment()
            2-> frag= AppIntroThirdFragment()
            3-> frag= AppIntroFourthFragment()
        }
        return frag!!
    }

}