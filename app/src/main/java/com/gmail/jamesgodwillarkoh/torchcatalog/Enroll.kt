package com.gmail.jamesgodwillarkoh.torchcatalog

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.parse.ParseObject
import com.parse.ParseQuery


class Enroll : Fragment(),CourseAdapter.OnItemClickListener {


    private  val CAMERA_REQUEST_CODE=101

    private val ACTIVITY_REQUEST_CODE=1

private lateinit var recyclerView: RecyclerView

    private lateinit var retryImage:ImageView

    private lateinit var retryLoad:TextView

    private lateinit var courseAdapter: CourseAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setPermissions()

        val view=inflater.inflate(R.layout.fragment_enroll, container, false)

        recyclerView=view.findViewById(R.id.v_recycleView)


        retryImage=view.findViewById(R.id.v_retry)

        retryLoad=view.findViewById(R.id.v_retry_view)




        courseAdapter= CourseAdapter(context?.applicationContext)

        courseAdapter.setOnItemClickListener(this@Enroll)


if (courseAdapter.networkerror == true)
{
    retryImage.visibility=View.VISIBLE
    retryLoad.visibility=View.VISIBLE
}
        recyclerView.adapter=courseAdapter


        // Inflate the layout for this fragment
        return view
    }
    private fun setPermissions(){
        val permission= context?.let { ContextCompat.checkSelfPermission(it,android.Manifest.permission.CAMERA) }

        if (permission != PackageManager.PERMISSION_GRANTED){
            makeRequest()
        }
    }

    private fun makeRequest() {
     requestPermissions(arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST_CODE  )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            CAMERA_REQUEST_CODE->{
                if(grantResults.isEmpty()||grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(context,"You need the camera Permission", Toast.LENGTH_LONG).show()
                }


            }
        }
    }

    override fun onItemClick(position: Int,intent: Intent,intent2:Intent) {

 val intentView=intent
        startActivity(intentView)


    }


}