package com.akshat.findmytutor

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatActivity : AppCompatActivity() {
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageBox : EditText
    private lateinit var sentButton : ImageView
    private lateinit var chatAdapter : ChatAdapter
    private lateinit var chatList : ArrayList<Chat>
    private lateinit var mDef : DatabaseReference

    var receiverRoom : String? = null
    var senderRoom : String? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")

        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        mDef = FirebaseDatabase.getInstance().getReference()

        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid

        supportActionBar?.title = name

        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        messageBox = findViewById(R.id.messageBox)
        sentButton = findViewById(R.id.sentBtn)
        chatList = ArrayList()
        chatAdapter = ChatAdapter(this, chatList)

        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = chatAdapter

        //logic for adding data to recyclerView
        mDef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    chatList.clear()
                    for (postsnapShot in snapshot.children){
                        val message = postsnapShot.getValue(Chat::class.java)
                        chatList.add(message!!)
                    }
                    chatAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        //adding the messages to database
        sentButton.setOnClickListener {
            val message = messageBox.text.toString()
            val messageObject = Chat(message,senderUid)
            mDef.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    Log.d("ChatActivity", "Message sent to senderRoom")
                    mDef.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObject).addOnSuccessListener {
                            Log.d("ChatActivity", "Message sent to receiverRoom")
                        }
                }
            messageBox.setText("")
        }
    }
}