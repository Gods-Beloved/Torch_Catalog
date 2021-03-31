package com.gmail.jamesgodwillarkoh.torchcatalog

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.react.modules.core.PermissionListener
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseQuery
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import org.jitsi.meet.sdk.*
import timber.log.Timber
import java.net.MalformedURLException
import java.net.URL


class Enroll : Fragment(),CourseAdapter.OnItemClickListener {



    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            onBroadcastReceived(intent)
        }
    }




    private  val CAMERA_REQUEST_CODE=101


    private val ACTIVITY_REQUEST_CODE=1
    private lateinit var shimmer:ShimmerFrameLayout

private lateinit var recyclerView: RecyclerView

    private lateinit var nestedDetails:NestedScrollView

private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>



    private lateinit var displayPic:CircleImageView

    private lateinit var Lecturerposition: TextView
    private lateinit var behaveDown: Button

    private lateinit var fullName: TextView

    private lateinit var department: TextView

    private lateinit var researcg: TextView

    private lateinit var courseAdapter: CourseAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setPermissions()

        val view=inflater.inflate(R.layout.fragment_enroll, container, false)

       // fusedLocationClient=LocationServices.getFusedLocationProviderClient(context)

        val serverURL: URL
        serverURL = try {
            // When using JaaS, replace "https://meet.jit.si" with the proper serverURL
            URL("https://meet.jit.si")
        } catch (e: MalformedURLException) {
            e.printStackTrace()
            throw RuntimeException("Invalid server URL!")
        }
        val defaultOptions = JitsiMeetConferenceOptions.Builder()
            .setServerURL(serverURL)

            // When using JaaS, set the obtained JWT here
            //.setToken("MyJWT")
            // Different features flags can be set
            //.setFeatureFlag("toolbox.enabled", false)
            //.setFeatureFlag("filmstrip.enabled", false)
            .setWelcomePageEnabled(false)
            .build()
        JitsiMeet.setDefaultConferenceOptions(defaultOptions)

        registerForBroadcastMessages()

        recyclerView=view.findViewById(R.id.v_recycleView)



        nestedDetails=view.findViewById(R.id.v_bottom_sheet)

        bottomSheetBehavior=BottomSheetBehavior.from(nestedDetails)

        displayPic=view.findViewById(R.id.lecture_image)
        Lecturerposition=view.findViewById(R.id.v_lecturer_position)
        fullName=view.findViewById(R.id.v_lecturer_name)
       researcg=view.findViewById(R.id.v_lecturer_interest)
       department=view.findViewById(R.id.v_lecturer_department)
        shimmer=view.findViewById(R.id.v_shimmer)

        behaveDown=view.findViewById(R.id.v_down_btn)


behaveDown.setOnClickListener {
    bottomSheetBehavior.state=BottomSheetBehavior.STATE_COLLAPSED
}


        courseAdapter= CourseAdapter(context?.applicationContext)

        courseAdapter.setOnItemClickListener(this@Enroll)



        recyclerView.adapter=courseAdapter


        // Inflate the layout for this fragment
        return view
    }
    private fun setPermissions(){
        val permission= context?.let {
            ContextCompat.checkSelfPermission(it,android.Manifest.permission.CAMERA)

        }
          val permission2= context?.let {
            ContextCompat.checkSelfPermission(it,android.Manifest.permission.ACCESS_COARSE_LOCATION)

        }
          val permission3= context?.let {
            ContextCompat.checkSelfPermission(it,android.Manifest.permission.ACCESS_FINE_LOCATION)

        }



        if (permission != PackageManager.PERMISSION_GRANTED || permission2 != PackageManager.PERMISSION_GRANTED||permission3 != PackageManager.PERMISSION_GRANTED){
            makeRequest()
        }
    }

    override fun onDestroy() {

        LocalBroadcastManager.getInstance(requireContext().applicationContext).unregisterReceiver(broadcastReceiver)
        super.onDestroy()
    }



    private fun registerForBroadcastMessages() {
        val intentFilter = IntentFilter()

        /* This registers for every possible event sent from JitsiMeetSDK
           If only some of the events are needed, the for loop can be replaced
           with individual statements:
           ex:  intentFilter.addAction(BroadcastEvent.Type.AUDIO_MUTED_CHANGED.action);
                intentFilter.addAction(BroadcastEvent.Type.CONFERENCE_TERMINATED.action);
                ... other events
         */
        for (type in BroadcastEvent.Type.values()) {
            intentFilter.addAction(type.action)
        }
LocalBroadcastManager.getInstance(requireContext().applicationContext).registerReceiver(broadcastReceiver,intentFilter)
        //LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter)
    }

    private fun onBroadcastReceived(intent: Intent?) {
        if (intent != null) {
            val event = BroadcastEvent(intent)
            when (event.getType()) {
                BroadcastEvent.Type.CONFERENCE_JOINED -> Timber.i("Conference Joined with url%s", event.getData().get("url"))
                BroadcastEvent.Type.PARTICIPANT_JOINED -> Timber.i("Participant joined%s", event.getData().get("name"))
            }
        }
    }

    private fun hangUp() {
        val hangupBroadcastIntent: Intent = BroadcastIntentHelper.buildHangUpIntent()
        LocalBroadcastManager.getInstance(org.webrtc.ContextUtils.getApplicationContext()).sendBroadcast(hangupBroadcastIntent)
    }


    private fun makeRequest() {
     requestPermissions(arrayOf(android.Manifest.permission.CAMERA,android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION), CAMERA_REQUEST_CODE  )
    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            CAMERA_REQUEST_CODE->{
                if(grantResults.isEmpty()||grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(context,"You need the camera and location permission ", Toast.LENGTH_LONG).show()
                }
                if(grantResults[1] != PackageManager.PERMISSION_GRANTED||grantResults[1] != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(context,"You need the location permission", Toast.LENGTH_LONG).show()
                }



            }
        }
    }





    override fun onItemClick(position: Int,intent: Intent) {

 val intentView=intent
        startActivity(intentView)


    }

    override fun onItemClick(position: Int) {
        shimmer.showShimmer(true)
shimmer.startShimmer()

        bottomSheetBehavior.state=BottomSheetBehavior.STATE_EXPANDED

        val parse = ParseQuery.getQuery<ParseObject>("Course")

        parse.findInBackground { objects, e ->

            if (e==null){

               val file=objects[position].getParseFile("image")?.url

                Picasso.get()
                        .load(file)
                        .resize(96,96)
                        .centerCrop()
                        .error(R.drawable.baseline_update_disabled_black_24dp)
                        .placeholder(R.drawable.baseline_person_black_24dp)
                        .into(displayPic)

                fullName.text=objects[position].get("lecturersFullName").toString()
                researcg.text="Research/Interest:"+objects[position].get("research_areas").toString()
                department.text="Dept: "+objects[position].get("department").toString()
                Lecturerposition.text=objects[position].get("position").toString()


shimmer.stopShimmer()
                shimmer.hideShimmer()

            }else{
                Toast.makeText(context,e.message,Toast.LENGTH_LONG).show()
                shimmer.stopShimmer()
                shimmer.hideShimmer()

            }

        }


    }




}