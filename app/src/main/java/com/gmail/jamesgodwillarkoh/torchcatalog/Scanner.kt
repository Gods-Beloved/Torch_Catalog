package com.gmail.jamesgodwillarkoh.torchcatalog

import android.Manifest
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.*
import android.provider.Settings
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.getSystemService
import com.budiyev.android.codescanner.*
import com.gmail.jamesgodwillarkoh.torchcatalog.util.PermissionUtil
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.parse.*
import java.util.*


class Scanner : AppCompatActivity() {

    private lateinit var codeScanner: CodeScanner

    private lateinit var codeScannerView: CodeScannerView


    private val LOCATION_PERMISSION_REQUEST_CODE = 102

    private lateinit var lectureCode: TextView


    override fun onStart() {
        super.onStart()
        when {
            PermissionUtil.isAccessFineLocationGranted(this) -> {
                when {
                    PermissionUtil.isLocationEnabled(this) -> {
                        setUpLocationListener()
                    }
                    else -> {
                        PermissionUtil.showGPSNotEnabledDialog(this)
                    }
                }
            }
            else -> {
                PermissionUtil.requestAccessFineLocationPermission(
                        this,
                        LOCATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun setUpLocationListener() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        val locationRequest = LocationRequest().setInterval(2000).setFastestInterval(2000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)


        fusedLocationProviderClient.requestLocationUpdates(locationRequest, object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
//                for (location in p0.locations) {
//                  //  lectureCode.text = location.latitude.toString()
//                   // lngTextView.text = location.longitude.toString()
//                }
            }
        }, Looper.myLooper())
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    when {
                        PermissionUtil.isLocationEnabled(this) -> {
                            setUpLocationListener()
                        }
                        else -> {
                            PermissionUtil.showGPSNotEnabledDialog(this)
                        }
                    }
                } else {
                    Toast.makeText(
                            this,
                            "Permission not granted",
                            Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)


        lectureCode = findViewById(R.id.v_display_view)

        // locationManager= getSystemService(Context.LOCATION_SERVICE) as LocationManager


        codeScanner()


    }


    @SuppressLint("MissingPermission")
    private fun codeScanner() {


        codeScannerView = findViewById(R.id.v_code_scanner)

        lectureCode = findViewById(R.id.v_display_view)

        codeScanner = CodeScanner(this, codeScannerView)

        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS
            autoFocusMode = AutoFocusMode.SAFE
            isAutoFocusEnabled = true
            isFlashEnabled = false
            scanMode = ScanMode.SINGLE

            decodeCallback = DecodeCallback {
                runOnUiThread {

//lectureCode.text=it.text.toString().substring(14,it.text.length)
                    val extras = intent.extras

                    var value: String

                    if (extras != null) {
                        value = extras.getString("courseCode").toString()

                        val resultText=it.text.toString()

                        if (resultText.startsWith(value) && resultText.contains("TORCH",false) ) {
                            MaterialAlertDialogBuilder(this@Scanner)
                                    .setCancelable(false)
                                    .setMessage("Do you want to Enroll for ${value}")
                                    .setNeutralButton("CANCEL") { dialog, which ->
                                        val intent = Intent(applicationContext, StudentPortal::class.java)
                                        startActivity(intent)
                                    }
                                    .setNegativeButton("DECLINE") { dialog, which ->
                                        dialog.cancel()

                                    }
                                    .setPositiveButton("ACCEPT") { dialog, which ->


                                        val currentUser = ParseUser.getCurrentUser()


                                        value = value.replace("\\s".toRegex(), "")

                                        lectureCode.text=value
                                        var latitude:Double

                                        var longitude: Double
                                        val obj = ParseObject.create(value)

                                        val query = ParseQuery.getQuery<ParseObject>(value)

                                        val queryUser = ParseUser.getQuery()
                                        queryUser.whereEqualTo("objectId",currentUser.objectId)

                                        query.whereEqualTo("name", currentUser.username.toString())
                                        query.getFirstInBackground { `object`, e ->


                                            if (e == null) {

                                                val validUpdated=`object`.updatedAt.day
                                                if(validUpdated == currentUser.updatedAt.day){
                                                    Toast.makeText(applicationContext,"You have already entered lecture",Toast.LENGTH_LONG).show()
                                                }else{ val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(applicationContext)

                                                    val locationRequest = LocationRequest().setInterval(2000).setFastestInterval(2000)
                                                            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)


                                                    fusedLocationProviderClient.requestLocationUpdates(locationRequest, object : LocationCallback() {
                                                        override fun onLocationResult(p0: LocationResult) {
                                                            super.onLocationResult(p0)
                                                            for (location in p0.locations) {


                                                                longitude = location.longitude
                                                                latitude = location.latitude
                                                                //lectureCode.text = "longitude =${longitude} latitude=${latitude}"
                                                                `object`.put("longitude", longitude)
                                                                `object`.put("latitude", latitude)
                                                                `object`.saveEventually()

                                                            }
                                                        }
                                                    }, Looper.myLooper())

                                                    `object`.increment("classAttended")
                                                    `object`.saveEventually()

                                                    queryUser.getFirstInBackground { object3, e3 ->
                                                        if(e3 == null){
                                                            object3.put("latest",`object`.updatedAt)
                                                            object3.saveEventually()
                                                        }else{
                                                            Toast.makeText(applicationContext,e3.message,Toast.LENGTH_LONG).show()
                                                        }

                                                    }
                                                }

                                            } else {
                                                if (e.code == ParseException.OBJECT_NOT_FOUND) {

                                                    val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(applicationContext)

                                                    val locationRequest = LocationRequest().setInterval(2000).setFastestInterval(2000)
                                                            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)


                                                    fusedLocationProviderClient.requestLocationUpdates(locationRequest, object : LocationCallback() {
                                                        override fun onLocationResult(p0: LocationResult) {
                                                            super.onLocationResult(p0)
                                                            for (location in p0.locations) {
                                                                var latitude2:Double

                                                                var longitude2: Double = location.longitude
                                                                latitude2 = location.latitude

                                                                obj.put("longitude", longitude2)
                                                                obj.put("latitude", latitude2)
                                                                obj.saveEventually()

                                                            }
                                                        }

                                                    }, Looper.myLooper())


                                                    obj.put("name", currentUser.username.toString())
                                                    obj.put("classAttended", 1)
                                                    
                                                    obj.saveEventually()

                                                    val c = Calendar.getInstance().time

                                                    queryUser.getFirstInBackground { object3, e3 ->
                                                        if(e3 == null){
                                                           object3.put("latest",c)
                                                            object3.saveEventually()
                                                        }else{
                                                            Toast.makeText(applicationContext,e3.message,Toast.LENGTH_LONG).show()
                                                        }

                                                    }



                                                }
                                            }

                                        }


                                        dialog.dismiss()


                                    }
                                    .show()
                        }else{
                            Toast.makeText(applicationContext,"Wrong Code Detected",Toast.LENGTH_SHORT).show()
                        }

                    }


                    val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        //deprecated in API 26
                        vibrator.vibrate(100);
                    }


                }
            }

            errorCallback = ErrorCallback {
                runOnUiThread {
                    Toast.makeText(this@Scanner, "ERROR:${it.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

        codeScannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }


}