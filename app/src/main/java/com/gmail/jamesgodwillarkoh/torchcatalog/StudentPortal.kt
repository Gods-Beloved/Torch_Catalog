package com.gmail.jamesgodwillarkoh.torchcatalog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.parse.ParseObject
import java.util.*

class StudentPortal : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout

    private lateinit var pager: ViewPager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_portal)

        tabLayout=findViewById(R.id.v_tab)
        pager=findViewById(R.id.v_pager)


       val adapter=PageAdapter(supportFragmentManager,FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)

        adapter.addFragment(Home(),"Home")
        adapter.addFragment(Enroll(),"Enroll")
        adapter.addFragment(Info(),"Info")


        pager.adapter=adapter

        tabLayout.setupWithViewPager(pager)






        tabLayout.addOnTabSelectedListener(object:TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {

            }
        })

    }
}