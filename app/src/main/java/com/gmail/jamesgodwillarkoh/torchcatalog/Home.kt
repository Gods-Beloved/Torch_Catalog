package com.gmail.jamesgodwillarkoh.torchcatalog

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView


class Home : Fragment(),CourseAdapter.OnItemClickListener {

private lateinit var recyclerView:RecyclerView

    private lateinit var courseAdapter:CourseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view=inflater.inflate(R.layout.fragment_home, container, false)
        // Inflate the layout for this fragment
        recyclerView=view.findViewById(R.id.v_recycleView_home)



        courseAdapter= CourseAdapter(context?.applicationContext)


        courseAdapter.setOnItemClickListener(this@Home)
        recyclerView.adapter=courseAdapter
        return view
    }

    override fun onItemClick(position: Int,intent: Intent,intentLecturer:Intent) {
        val intent2=intentLecturer
        startActivity(intent2)
        Toast.makeText(context,"Lecturers view",Toast.LENGTH_LONG).show()
    }


}