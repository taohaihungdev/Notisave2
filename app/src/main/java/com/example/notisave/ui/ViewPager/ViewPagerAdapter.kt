package com.example.notisave.ui.ViewPager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.notisave.ui.Message.GroupMessageFragment
import com.example.notisave.ui.More.MoreFragment
import com.example.notisave.ui.home.HomeFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 3 // Number of fragments
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> GroupMessageFragment()
            2->MoreFragment()
            else -> HomeFragment()
        }
    }
}