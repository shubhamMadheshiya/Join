package com.example.join

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth

class UserAdapter(val context:Context, val userlist:ArrayList<User>): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    class UserViewHolder(iteamView:View):RecyclerView.ViewHolder(iteamView){
        val textName = iteamView.findViewById<TextView>(R.id.user_name_user_layout)
        val imageView = iteamView.findViewById<ImageView>(R.id.profile_image_user)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
       val view = LayoutInflater.from(context).inflate(R.layout.user_layout,parent,false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
       val currentUser= userlist[position]
        holder.textName.text=currentUser.name
        Glide.with(context).load(currentUser.url).into(holder.imageView);
        holder.itemView.setOnClickListener{
            val intent = Intent(context,ChatActivity::class.java)
            intent.putExtra("name",currentUser.name)
            intent.putExtra("uid",currentUser.uid)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
       return  userlist.size
    }
}