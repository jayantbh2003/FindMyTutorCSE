package com.akshat.findmytutor

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class TutorAdapter(private val context:Context, private val userList:ArrayList<Tutor>):RecyclerView.Adapter<TutorAdapter.TutorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TutorViewHolder {
        val view : View = LayoutInflater.from(context).inflate(R.layout.tutor_layout,parent,false)
        return TutorViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: TutorViewHolder, position: Int) {
        val currentTutor = userList[position]
        Log.d("TutorAdapter", "Binding user: ${currentTutor.name}, ${currentTutor.subject}")
        holder.textName.text = currentTutor.name
        holder.textSubject.text = currentTutor.subject

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("name",currentTutor.name)
            intent.putExtra("uid", currentTutor.uid)
            context.startActivity(intent)
        }
    }

    class TutorViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val textName: TextView = itemView.findViewById(R.id.tvTutorName)
        val textSubject: TextView = itemView.findViewById(R.id.tvTutorSubject)
    }
}

