package com.gmail.jamesgodwillarkoh.torchcatalog

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.budiyev.android.codescanner.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.parse.*


class Scanner : AppCompatActivity() {

    private lateinit var codeScanner: CodeScanner

    private lateinit var codeScannerView: CodeScannerView

    private lateinit var lectureCode: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)

        codeScanner()


    }

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
                    lectureCode.text = it.text.toString()

                    val extras = intent.extras

                    var value: String

                    if (extras != null) {
                        value = extras.getString("courseCode").toString()

                        if (it.text.toString() == value) {
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

                                        val obj = ParseObject.create(value)

                                        val query = ParseQuery.getQuery<ParseObject>(value)

                                        query.whereEqualTo("name", currentUser.username.toString())
                                        query.getFirstInBackground { `object`, e ->

                                            if (e == null) {
                                                `object`.increment("classAttended")
                                                `object`.saveEventually()
                                            }else{
                                                if(e.code==ParseException.OBJECT_NOT_FOUND){
                                                    obj.put("name", currentUser.username.toString())
                                                    obj.put("classAttended", 1)
                                                    obj.saveEventually()
                                                }
                                            }

                                        }


                                        dialog.dismiss()


                                    }
                                    .show()
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