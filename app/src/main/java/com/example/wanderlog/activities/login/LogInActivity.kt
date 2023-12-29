package com.example.wanderlog.activities.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import com.example.wanderlog.MainActivity
import com.example.wanderlog.R
import com.example.wanderlog.activities.forgetpassword.ForgetPasswordActivity
import com.example.wanderlog.api.service.UserService
import com.example.wanderlog.database.dto.LoginRequest
import com.example.wanderlog.retrofit.RetrofitInstance
import com.example.wanderlog.utils.EmailUtils.validateEmail
import com.example.wanderlog.utils.PasswordUtils.validatePassword
import com.google.android.material.textfield.TextInputEditText

class LogInActivity : AppCompatActivity() {

    private lateinit var emailInputLogIn: TextInputEditText
    private lateinit var passwordInputLogIn: TextInputEditText
    private lateinit var btnGetStartedLogIn: Button
    private lateinit var tvForgotPasswordLogIn: TextView
    private lateinit var chkTermsConditions: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        setup()
        onBtnGetStartedClicked()
        onTvForgotPasswordClicked()
    }

    private fun setup(){
        emailInputLogIn = findViewById(R.id.email_input_log_in)
        passwordInputLogIn = findViewById(R.id.password_input_log_in)
        btnGetStartedLogIn = findViewById(R.id.btn_get_started_log_in)
        tvForgotPasswordLogIn = findViewById(R.id.tv_forgot_password_log_in)
        chkTermsConditions = findViewById(R.id.chk_terms_conditions)
    }

    private fun onBtnGetStartedClicked(){
        btnGetStartedLogIn.setOnClickListener {

            if(emailInputLogIn.text.toString().isEmpty() && passwordInputLogIn.text.toString().isEmpty()){
                Toast.makeText(this, "Please enter your email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(emailInputLogIn.text.toString().isEmpty()){
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(passwordInputLogIn.text.toString().isEmpty()){
                Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(!validateEmail(emailInputLogIn.text.toString())){
                Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(!validatePassword(passwordInputLogIn.text.toString())){
                Toast.makeText(this, "Invalid password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(!chkTermsConditions.isChecked){
                Toast.makeText(this, "Please accept the terms and conditions", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val loginRequest = LoginRequest(
                emailInputLogIn.text.toString(),
                passwordInputLogIn.text.toString()
            )
            val userService: UserService = RetrofitInstance.getRetrofitInstance().create(
                UserService::class.java
            )
            val call = userService.login(loginRequest)
            call.enqueue(object : retrofit2.Callback<Boolean> {
                override fun onResponse(call: retrofit2.Call<Boolean>, response: retrofit2.Response<Boolean>) {
                    if (response.isSuccessful) {
                        if (response.body() == true) {
                            Toast.makeText(this@LogInActivity, "Login successful", Toast.LENGTH_SHORT).show()
                            saveUserDetails()
                            var intent = Intent(this@LogInActivity, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this@LogInActivity, "Login failed", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@LogInActivity, "Login failed", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: retrofit2.Call<Boolean>, t: Throwable) {
                    Toast.makeText(this@LogInActivity, "Login failed", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun onTvForgotPasswordClicked(){
        tvForgotPasswordLogIn.setOnClickListener {
            var intent = Intent(this, ForgetPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun saveUserDetails(){
        val sharedPreferences = getSharedPreferences("sharedPrefs ", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply{
            putString("EMAIL", emailInputLogIn.text.toString())
            putString("PASSWORD", passwordInputLogIn.text.toString())
        }.apply()
    }
}