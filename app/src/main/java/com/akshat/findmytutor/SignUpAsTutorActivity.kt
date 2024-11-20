package com.akshat.findmytutor

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpAsTutorActivity : AppCompatActivity() {
    private lateinit var edtEmail : EditText
    private lateinit var edtName : EditText
    private lateinit var edtPassword : EditText
    private lateinit var edtSubject : EditText
    private lateinit var btnSignUp : Button
    private lateinit var mAuth : FirebaseAuth
    private lateinit var mDbRef : DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up_as_tutor)

        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()

        edtEmail = findViewById(R.id.edtEmail)
        edtPassword = findViewById(R.id.edtPassword)
        edtName = findViewById(R.id.edtName)
        btnSignUp = findViewById(R.id.btnSignUp)
        edtSubject = findViewById(R.id.edtSubject)

        btnSignUp.setOnClickListener {
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()
            val name  = edtName.text.toString()
            val subject = edtSubject.text.toString()

            siningAsTutor(name,email,password,subject)
        }
    }
    private fun siningAsTutor(name : String, email:String, password:String,subject:String){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                addTutorDataToTutorDatabase(name,email,mAuth.currentUser?.uid,subject)
                val intent = Intent(this@SignUpAsTutorActivity, LoginActivity::class.java)
                finish()
                startActivity(intent)
            }
            else{
                Toast.makeText(this@SignUpAsTutorActivity,"Some Error Occurred", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addTutorDataToTutorDatabase(name:String,email:String,uid:String?,subject:String){
        mDbRef = FirebaseDatabase.getInstance().getReference("Users")
        val tutorData = mapOf("name" to name, "email" to email, "userType" to "tutor", "uid" to uid,"subject" to subject)
        if (uid != null) {
            mDbRef.child(uid).setValue(tutorData).addOnCompleteListener { saveTask ->
                if (saveTask.isSuccessful) {
                    Toast.makeText(this, "Tutor Registered Successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to save tutor data", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}