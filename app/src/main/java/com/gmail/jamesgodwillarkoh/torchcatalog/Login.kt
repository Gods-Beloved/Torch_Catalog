package com.gmail.jamesgodwillarkoh.torchcatalog

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.parse.LogInCallback
import com.parse.ParseException
import com.parse.ParseObject
import com.parse.ParseUser

class Login : AppCompatActivity() {

    private lateinit var shimmer:ShimmerFrameLayout

    private lateinit var login: Button

    private lateinit var register: TextView

    private lateinit var textUsername: TextInputEditText

    private lateinit var username: TextInputLayout

    private lateinit var textPassword: TextInputEditText

    private lateinit var password: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        register = findViewById(R.id.v_register_view)
        textUsername = findViewById(R.id.v_login_usrname)
        username = findViewById(R.id.v_textInputLayout_user)
        login=findViewById(R.id.v_login_btn)
        shimmer=findViewById(R.id.v_shimmer_load)
  textPassword = findViewById(R.id.v_login_password)

        password = findViewById(R.id.v_textInputLayout_password)

shimmer.hideShimmer()
        textUsername.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                isUsernameEmpty()
            }
        })

        textPassword.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                isPasswordEmpty()
            }
        })


        register.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        login.setOnClickListener {
            if(!isUsernameEmpty())
            {
                return@setOnClickListener
            }
            if (!isPasswordEmpty())
            {
                return@setOnClickListener
            }



shimmer.showShimmer(true)

val username=textUsername.text.toString()
            val password=textPassword.text.toString()

            ParseUser.logInInBackground(username,password,object:LogInCallback{
                override fun done(user: ParseUser?, e: ParseException?) {
                    if (e==null)
                    {
                        val intent=Intent(this@Login,StudentPortal::class.java)
                        startActivity(intent)


                        shimmer.hideShimmer()
                    }
                    else{
                        Toast.makeText(this@Login,e.message,Toast.LENGTH_SHORT).show()
                        shimmer.hideShimmer()
                    }

                }
            })



        }
    }

    private fun requestFocus(view: View) {
        if (view.requestFocus()) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }
    }


    private fun isUsernameEmpty(): Boolean {

        if (textUsername.text.toString().trim().isEmpty()) {
            username.error = "Username is required"
            requestFocus(textUsername)
            return false
        }else{
            username.isErrorEnabled = false
        }

        return true

    }
    private fun isPasswordEmpty():Boolean{
        if(textPassword.text.toString().trim().toString().isEmpty()){
            password.error="Password is required"
            requestFocus(textPassword)
            return false
        }
        else {

            password.isErrorEnabled=false
        }

        return true
    }

}