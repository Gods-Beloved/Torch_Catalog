package com.gmail.jamesgodwillarkoh.torchcatalog

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.parse.GetCallback
import com.parse.ParseObject
import com.parse.ParseQuery
import java.util.*


class Info : Fragment() {


    private lateinit var display:MaterialButton

    private lateinit var viewResults:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {





//        parseScore.addAll("lecturer",Arrays.asList("H.M YUSIF","A. SARDIYA","K.A PABBI","E.AHENE","E.0 ANSONG","J.K PANFORD","M. ASANTE"))
//        parseScore.addAll("code",Arrays.asList("CSM 497","MAS 261","CSM 481","CSM 495","CSM 483","CSM 491","CSM 477"))
//        parseScore.saveInBackground()

        // Inflate the layout for this fragment



        val view =inflater.inflate(R.layout.fragment_info, container, false)

         display=view.findViewById(R.id.v_display)


         viewResults=view.findViewById(R.id.v_view)

        display.setOnClickListener {


//        parseScore.addAll("code",Arrays.asList("CSM 497","MAS 261","CSM 481","CSM 495","CSM 483","CSM 491","CSM 477"))


            // Inflate the layout for this fragment

            Toast.makeText(context,"clicked",Toast.LENGTH_SHORT).show()



        }

        return view
    }


}