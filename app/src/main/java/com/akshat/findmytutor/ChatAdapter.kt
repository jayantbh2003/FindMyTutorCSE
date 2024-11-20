package com.akshat.findmytutor

import android.app.Notification.MessagingStyle.Message
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class ChatAdapter(val context : Context, val chatList:ArrayList<Chat>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val ITEM_RECIEVE = 1
    val ITEM_SENT = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1){
            //inflate recieve
            val view : View = LayoutInflater.from(context).inflate(R.layout.recieve,parent, false)
            return RecieveViewHolder(view)
        }
        else{
            //inflate sent
            val view : View = LayoutInflater.from(context).inflate(R.layout.sent,parent, false)
            return SentViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = chatList[position]

        if (holder.javaClass == SentViewHolder::class.java){
            //do the stuff for sent view holder
            val viewHolder = holder as SentViewHolder
            holder.sentMessage.text = currentMessage.message
        }
        else{
            //do the stuff for recieve view holder
            val viewHolder = holder as RecieveViewHolder
            holder.recieveMessage.text = currentMessage.message
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = chatList[position]
        if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)){
            return ITEM_SENT
        }
        else return ITEM_RECIEVE
    }

    class SentViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val sentMessage = itemView.findViewById<TextView>(R.id.txt_sent_message)
    }

    class RecieveViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val recieveMessage = itemView.findViewById<TextView>(R.id.txt_recieve_message)
    }
}