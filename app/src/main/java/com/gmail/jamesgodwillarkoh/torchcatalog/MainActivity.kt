package com.gmail.jamesgodwillarkoh.torchcatalog


import android.annotation.SuppressLint
import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.parse.ParseException
import com.parse.ParseObject
import com.parse.ParseUser
import com.parse.SignUpCallback

class MainActivity : AppCompatActivity() {

    private lateinit var shimmer: ShimmerFrameLayout

    private lateinit var loginTextView:TextView

    private lateinit var password: TextInputLayout

    private lateinit var textPassword: TextInputEditText

    private lateinit var passwordConfirm: TextInputLayout

    private lateinit var textPasswordConfirm: TextInputEditText

    private lateinit var username: TextInputLayout

    private lateinit var textUsername: TextInputEditText

    private lateinit var course: TextInputLayout

    private lateinit var textCourse: TextInputEditText


    private lateinit var email: TextInputLayout

    private lateinit var textEmail: TextInputEditText



    private lateinit var signUp: MaterialButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

     ParseUser.logOut()



        password = findViewById(R.id.v_password)
        textPassword = findViewById(R.id.v_password_text)
        passwordConfirm = findViewById(R.id.v_password_confirm)
        textPasswordConfirm = findViewById(R.id.v_password_text_confirm)
        email = findViewById(R.id.v_student_email)
        textEmail = findViewById(R.id.v_email_text)
        username=findViewById(R.id.v_username)
        textUsername=findViewById(R.id.v_username_text)
        textCourse=findViewById(R.id.v_student_course_text)
        shimmer=findViewById(R.id.v_shimmer_load)
        course=findViewById(R.id.v_student_course)
        loginTextView=findViewById(R.id.v_login_view)

        signUp = findViewById(R.id.v_sign_up)



        loginTextView.setOnClickListener {

            val intent= Intent(this@MainActivity,Login::class.java)
            startActivity(intent)

        }

        shimmer.hideShimmer()
        signUp.setOnClickListener()
        {


            if (!isUsernameEmpty())
            {

               return@setOnClickListener
            }
            if (!isCourseEmpty())
            {

               return@setOnClickListener
            }
            if (!isEmailValid()) {

                return@setOnClickListener
            }
            if (!isPasswordValid()) {

                return@setOnClickListener
            }
            if (!isConfirmPasswordValid()) {

                return@setOnClickListener
            }
            shimmer.showShimmer(true)



            val user=ParseUser()
            user.setUsername(textUsername.text.toString())
            user.put("Course",textCourse.text.toString())
            user.email=textEmail.text.toString()
            user.setPassword(textPasswordConfirm.text.toString())
            user.put("verified",ParseObject.createWithoutData("Enrolled","JJpOvVSDvl"))



      user.signUpInBackground {

          e ->

          if(e==null) {

              Toast.makeText(this@MainActivity,"Signed Up",Toast.LENGTH_LONG).show()
              val intent=Intent(this@MainActivity,Login::class.java)
              startActivity(intent)

              ParseUser.logOut()

          }else {
              Toast.makeText(this@MainActivity,e.message,Toast.LENGTH_LONG).show()
              shimmer.hideShimmer()

          }
      }

        }





        textUsername.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                isUsernameEmpty()
            }
        })
        textCourse.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                isCourseEmpty()
            }
        })

        textEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                isEmailValid()
            }
        })
        textPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                isPasswordValid()
            }
        })

        textPasswordConfirm.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                isConfirmPasswordValid()
            }
        })


    }


    private fun requestFocus(view: View) {
        if (view.requestFocus()) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }
    }





    private fun isUsernameEmpty(): Boolean {

        if (textUsername.text.toString().trim().isEmpty())
        {
            username.error="Username is required"
            requestFocus(textUsername)
            return false
        }
        else{
            username.isErrorEnabled=false
        }
        return true

    }
    private fun isCourseEmpty(): Boolean {
        if (textCourse.text.toString().trim().isEmpty())
        {
            course.error="Course is required"
            requestFocus(textCourse)
            return false
        }
        else{
            course.isErrorEnabled=false
        }

        return true

    }

    fun isEmailValid(): Boolean {

        when{
            textEmail.text.toString().trim().isEmpty()-> {
                email.error="Email is required"
                requestFocus(textEmail)
                return false
            } else ->{
            val emailId = textEmail.text.toString()

            val isValid = android.util.Patterns.EMAIL_ADDRESS.matcher(emailId).matches()

            if (!isValid) {
                email.error = "Invalid Email Address,ex: abc@example.come"
                requestFocus(textEmail)
                return false
            } else {
                email.isErrorEnabled = false

            }
        }
        }

        return true
    }

    fun isPasswordValid(): Boolean {

        when {
            textPassword.text.toString().trim().isEmpty() -> {
                password.error = "Password is required"
                requestFocus(textPassword)
                return false
            }
            textPassword.text.toString().length < 8 -> {
                password.error = "Password must contain 8 or more characters"
                requestFocus(textPassword)
                return false
            }

            else -> {
                password.isErrorEnabled = false
            }
        }
        return true
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun isConfirmPasswordValid(): Boolean {

        val pword= textPassword.text?.trim().toString()
        val confirmPword=textPasswordConfirm.text?.trim().toString()

        when {
            textPasswordConfirm.text.toString().trim().isEmpty() -> {
                passwordConfirm.error = "Password confirmation required"
                requestFocus(textPasswordConfirm)
                return false
            }
            pword != confirmPword -> {
                passwordConfirm.error = "Password does not match"
                requestFocus(textPasswordConfirm)
                return false

            }


            else -> {

                passwordConfirm.endIconDrawable=resources.getDrawable(R.drawable.baseline_check_circle_green_700_24dp)
                passwordConfirm.isErrorEnabled = false



            }
        }
        return true
    }



}
