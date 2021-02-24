package com.gmail.jamesgodwillarkoh.torchcatalog

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import gr.net.maroulis.library.EasySplashScreen

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

val config=EasySplashScreen(this@SplashScreenActivity)
        .withFullScreen()
        .withTargetActivity(Login::class.java)
        .withSplashTimeOut(5000)
        .withBackgroundColor(Color.parseColor("#ffffffff"))
//        .withBeforeLogoText("Before Logo Text")
//        .withAfterLogoText("After Logo Text")
        .withLogo(R.drawable.torch)

        val splashScreen=config.create()

        setContentView(splashScreen)


    }
}