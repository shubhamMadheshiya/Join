package com.example.join

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

                supportActionBar?.hide()




        val emailText = findViewById<EditText>(R.id.login_email_text)
        val passwordText = findViewById<EditText>(R.id.login_password_text)
        val loginButton = findViewById<Button>(R.id.login_button)
        val signOutButton = findViewById<TextView>(R.id.signOut_login)

        signOutButton.setOnClickListener{
            intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }

        auth = Firebase.auth

        loginButton.setOnClickListener{
            val email = emailText.text.toString()
            val password = passwordText.text.toString()

            if(passwordText.text!!.isEmpty()){
                Toast.makeText(this,"Please enter your password", Toast.LENGTH_SHORT).show()
            }
            else if(emailText.text!!.isEmpty()){
                Toast.makeText(this,"Please enter your email",Toast.LENGTH_SHORT).show()
            }
            else {

                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {

                            Toast.makeText(
                                baseContext, "Login Successful",
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent = Intent(this, chatList::class.java)
                            intent.putExtra("email1",email)
                            startActivity(intent)
                            finish()


                        } else {

                            Toast.makeText(
                                baseContext, "Enter valid user id and password",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    }
            }


        }


    }
}