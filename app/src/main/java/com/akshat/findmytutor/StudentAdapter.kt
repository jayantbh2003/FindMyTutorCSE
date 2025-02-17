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

class StudentAdapter(val context: Context, val userList:ArrayList<Student>):RecyclerView.Adapter<StudentAdapter.UserViewHolder> (){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view : View = LayoutInflater.from(context).inflate(R.layout.student_layout,parent,false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]
        Log.d("StudentAdapter", "Binding user: ${currentUser.name}")
        holder.textName.text=currentUser.name

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("name",currentUser.name)
            intent.putExtra("uid",currentUser.uid)
            context.startActivity(intent)
        }
    }

    class UserViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val textName = itemView.findViewById<TextView>(R.id.tvStudent)
    }
}