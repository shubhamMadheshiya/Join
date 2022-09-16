package com.example.join

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        val signupButton = findViewById<Button>(R.id.signup_main_button)
        val loginButtton = findViewById<Button>(R.id.login_main_button)

        signupButton.setOnClickListener{
            val intent = Intent(this , SignUp::class.java)
            startActivity(intent)
            finish()
        }
        loginButtton.setOnClickListener{
            val intent = Intent(this , Login::class.java)
            startActivity(intent)
            finish()
        }
    }
}