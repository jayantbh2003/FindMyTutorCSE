package com.akshat.findmytutor

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TutorActivity : AppCompatActivity() {
    private lateinit var tutorRecyclerView: RecyclerView
    private lateinit var tutorArrayList: ArrayList<Tutor>
    private lateinit var myAdapter: TutorAdapter
    private lateinit var mAuth : FirebaseAuth
    private lateinit var mDef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutor)

        mAuth = FirebaseAuth.getInstance()

        tutorArrayList = ArrayList()
        myAdapter = TutorAdapter(this, tutorArrayList)

        tutorRecyclerView = findViewById(R.id.rvTutor)
        tutorRecyclerView.layoutManager = LinearLayoutManager(this)
        tutorRecyclerView.adapter = myAdapter

        mDef = FirebaseDatabase.getInstance().getReference()
        mDef.child("Users").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                tutorArrayList.clear()
                for (postSnapshot in snapshot.children) {
                    val currentUser = postSnapshot.getValue(Tutor::class.java)
                    if (currentUser != null) {
                        if (currentUser.userType == "tutor") {
                            tutorArrayList.add(currentUser)
                        }
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
            val intent = Intent(this@TutorActivity,LoginActivity::class.java)
            finish()
            startActivity(intent)
            return true
        }
        return true
    }
}