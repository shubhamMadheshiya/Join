package com.example.join

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.join.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.net.URI
import java.util.*

class SignUp : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth;
    private lateinit var mdbRef: DatabaseReference
    private  lateinit var storage: FirebaseStorage
    private  lateinit var selectedImg: Uri
    private lateinit var dialog: AlertDialog.Builder
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        supportActionBar?.hide()
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        dialog = AlertDialog.Builder(this).setMessage("Updating Profil..")

        storage=FirebaseStorage.getInstance()

        binding.userImage.setOnClickListener{
            val intent = Intent()
            intent.action=Intent.ACTION_GET_CONTENT
            intent.type="image/*"
            startActivityForResult(intent,1)
        }


        val name = findViewById<EditText>(R.id.name_text)
        val email = findViewById<EditText>(R.id.email_text)
        val password = findViewById<EditText>(R.id.password_text)
        val rePassword = findViewById<EditText>(R.id.re_password_text)
        val signupButton = findViewById<Button>(R.id.signup_signup_button)
        val checkBox = findViewById<CheckBox>(R.id.checkBox)

        auth = Firebase.auth



        signupButton.setOnClickListener{
            if(name.text!!.isEmpty()){
                Toast.makeText(this,"Please enter your name",Toast.LENGTH_SHORT).show()
            }
            else if(email.text!!.isEmpty()){
                Toast.makeText(this,"Please enter your email",Toast.LENGTH_SHORT).show()
            }
            else if(password.text!!.isEmpty()){
                Toast.makeText(this,"Please enter your Password",Toast.LENGTH_SHORT).show()
            }
            else if(password.text.toString()!=rePassword.text.toString()){
                Toast.makeText(this,"Password not match",Toast.LENGTH_SHORT).show()

            }

            else if(checkBox.isChecked){
                val reference = storage

                val email = email.text.toString()
                val password = password.text.toString()
                val name= name.text.toString()

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            addUserToDatabase(name,email, auth.currentUser?.uid!!)
                            Toast.makeText(baseContext, "User registers.",
                                Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, Login::class.java)
                            startActivity(intent)
                            finish()
                        } else {

                            Toast.makeText(baseContext, "Please type valid email.",
                                Toast.LENGTH_SHORT).show()

                        }
                    }

            }
            else {



                Toast.makeText(this,"Please agree Terms and Privacy Policy",Toast.LENGTH_SHORT).show()

            }
            }

        }
    private fun addUserToDatabase(name: String?, email: String, uid: String?) {
        mdbRef=FirebaseDatabase.getInstance().getReference()
        val reference = storage.reference.child("Profil").child(Date().time.toString())
        reference.putFile(selectedImg).addOnCompleteListener{
            if (it.isSuccessful){
                reference.downloadUrl.addOnSuccessListener { task->
                    if (uid != null) {
                        
                        mdbRef.child("user").child(uid).setValue(User(name,email,uid,task.toString()))
                    }
                }
            }
        }


    }






    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data!=null){
            if(data.data!=null){
                selectedImg = data.data!!
                binding.userImage.setImageURI(selectedImg)
            }
        }
    }
}