package com.gmail.jamesgodwillarkoh.torchcatalog

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class PageAdapter(fm:FragmentManager,behavior:Int): FragmentStatePagerAdapter(fm,behavior) {


    private val fragmentList= ArrayList<Fragment>()

    private val fragmentTitles=ArrayList<CharSequence>()

    override fun getItem(position: Int): Fragment {

        return fragmentList[position]
    }

    override fun getCount(): Int {
       return fragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTitles[position]
    }

    fun addFragment(fragment: Fragment,title:CharSequence){

        fragmentList.add(fragment)
        fragmentTitles.add(title)
    }


}