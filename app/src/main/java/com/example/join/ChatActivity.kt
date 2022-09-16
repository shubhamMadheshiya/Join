package com.example.join

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageList: ArrayList<Message>
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var mDbRef: DatabaseReference

    var receiverRoom:String?=null
    var senderRoom:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat2)

        supportActionBar?.hide()
        val profilImage=findViewById<ImageView>(R.id.profile_image_current_user)
        val profilName=findViewById<TextView>(R.id.profil_name_current)
        val backButton = findViewById<ImageView>(R.id.back_button_current)


        backButton.setOnClickListener{
           val  intent = Intent(this,chatList::class.java)
        startActivity(intent)
        finish()
        }





        mDbRef = FirebaseDatabase.getInstance().getReference()


        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")
        val senderUid= FirebaseAuth.getInstance().currentUser?.uid

        profilName.text = name

        mDbRef.child("user").child(receiverUid!!).get().addOnSuccessListener {
            val profilUrl = it.child("url").value

            Glide.with(this).load(profilUrl).into(profilImage)


        }


        senderRoom=receiverUid +senderUid
        receiverRoom=senderUid+receiverUid

        supportActionBar?.hide()

        chatRecyclerView=findViewById(R.id.chat_recyclerview)
        val messageBox = findViewById<EditText>(R.id.message_box)
        val sentbutton = findViewById<ImageView>(R.id.send_button)
        messageList = ArrayList()
        messageAdapter= MessageAdapter(this, messageList)
        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = messageAdapter



        mDbRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for (postSnapshot in snapshot.children){
                        val message = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        sentbutton.setOnClickListener{
            val message = messageBox.text.toString()
            val messageObject = Message(message,senderUid)
            mDbRef.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener { mDbRef.child("chats").child(receiverRoom!!).child("messages").push()
                    .setValue(messageObject) }
            messageBox.setText("")

        }

    }
}