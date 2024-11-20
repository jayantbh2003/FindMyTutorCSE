package com.akshat.findmytutor

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class StudentActivity : AppCompatActivity() {
    private lateinit var StudentRecyclerView: RecyclerView
    private lateinit var studentArrayList: ArrayList<Student>
    private lateinit var myAdapter: StudentAdapter
    private lateinit var mAuth : FirebaseAuth
    private lateinit var mDef : DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student)

        mAuth = FirebaseAuth.getInstance()

        studentArrayList = ArrayList()
        myAdapter = StudentAdapter(this, studentArrayList)

        StudentRecyclerView = findViewById(R.id.StudentRecyclerView)
        StudentRecyclerView.layoutManager = LinearLayoutManager(this)
        StudentRecyclerView.adapter = myAdapter

        mDef = FirebaseDatabase.getInstance().getReference()
        mDef.child("Users").addValueEventListener(object :ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                studentArrayList.clear()
                for (postSnapshot in snapshot.children){
                    val currentUser = postSnapshot.getValue(Student::class.java)
                    if (currentUser != null && currentUser.userType == "student") {
                        studentArrayList.add(currentUser)
                    }
                }
                myAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {}
        })

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout){
            //we have to write the logic for logout
            mAuth.signOut()
            val intent = Intent(this@StudentActivity,LoginActivity::class.java)
            finish()
            startActivity(intent)
            return true
        }
        return true
    }
}
