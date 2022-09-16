package com.example.join

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class chatList : AppCompatActivity() {
    private lateinit var userRecyclerView:RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var adapter: UserAdapter
    private lateinit var auth:FirebaseAuth
    private lateinit var mDbRef:DatabaseReference
    lateinit var toggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_list)
        supportActionBar?.hide()

        val logoutButton = findViewById<ImageView>(R.id.logout)


        val sharedPref =this?.getPreferences(Context.MODE_PRIVATE)?:return
        val logIn = sharedPref.getString("email","1")
        val email = intent.getStringExtra("email1")




        logoutButton.setOnClickListener{
            sharedPref.edit().remove("email").apply()
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        if(logIn=="1"){

            if (email!=null){
                val editor = sharedPref.edit()
                editor.putString("email",email)
                editor.apply()
                setText()
            }
            else{

                intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()
            }

        }
        else{
            setText()

        }




    }

    private fun setText() {

        val myProfile = findViewById<ImageView>(R.id.profile_image_my)
        val myName = findViewById<TextView>(R.id.my_name)


        userList= ArrayList()
        adapter= UserAdapter(this,userList)
        auth=FirebaseAuth.getInstance()
        mDbRef=FirebaseDatabase.getInstance().getReference()

        userRecyclerView=findViewById(R.id.recycler_view)
        userRecyclerView.layoutManager=LinearLayoutManager(this)
        userRecyclerView.adapter=adapter

        val uid = FirebaseAuth.getInstance().currentUser?.uid


        mDbRef.child("user").child(uid!!).get().addOnSuccessListener {
            val profilName = it.child("name").value
            val profilUrl = it.child("url").value

            Glide.with(this).load(profilUrl).into(myProfile)
            myName.text = profilName.toString()




            mDbRef.child("user").addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {


                    userList.clear()
                    for (postSnapshot in snapshot.children){
                        val currentUser = postSnapshot.getValue(User::class.java)
                        if (auth.currentUser?.uid!=currentUser?.uid){
                            userList.add(currentUser!!)

                        }

                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

        }





    }
}