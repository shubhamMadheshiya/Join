package com.example.join

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(val context: Context, val messageList:ArrayList<Message>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {



    val ITEM_RECEIVE=1
    val ITEM_SENT=2

    class SentViewHolder(iteamView: View):RecyclerView.ViewHolder(iteamView){
        val sentMessage: TextView = iteamView.findViewById(R.id.sent_message_text)
    }

    class ReceiveViewHolder(iteamView:View):RecyclerView.ViewHolder(iteamView){
        val receiveMessage: TextView = iteamView.findViewById(R.id.receive_message_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType==1){
            val view = LayoutInflater.from(context).inflate(R.layout.receive_layout,parent,false)
            return ReceiveViewHolder(view)

        }else{
            val view = LayoutInflater.from(context).inflate(R.layout.sent_layout,parent,false)
            return SentViewHolder(view)

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messageList[position]




        if (holder.javaClass== SentViewHolder::class.java){
            val  viewHolder = holder as SentViewHolder
            holder.sentMessage.text= currentMessage.message
        }else{

            val  viewHolder = holder as ReceiveViewHolder
            holder.receiveMessage.text= currentMessage.message


        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]
        if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)) {
            return ITEM_SENT

        }
        else{
            return ITEM_RECEIVE
        }

    }

    override fun getItemCount(): Int {
        return messageList.size
    }
}