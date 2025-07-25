package com.dapoer.rangoe.ui.linkyi

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dapoer.rangoe.ui.linkyi.tabs.HeadlineFragment
import com.dapoer.rangoe.ui.linkyi.tabs.LinkFragment

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = LinkFragment()
            1 -> fragment = HeadlineFragment()
        }
        return fragment as Fragment
    }

    override fun getItemCount(): Int {
        return 2
    }

}