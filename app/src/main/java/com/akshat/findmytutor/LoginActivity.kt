package com.akshat.findmytutor

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {
    private lateinit var edtEmail : EditText
    private lateinit var edtPassword : EditText
    private lateinit var btnSignUpAsStudent : Button
    private lateinit var btnSignUpAsTutor : Button
    private lateinit var btnLogin : Button
    private lateinit var mAuth : FirebaseAuth


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()

        edtEmail = findViewById(R.id.edtEmail)
        edtPassword = findViewById(R.id.edtPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnSignUpAsStudent = findViewById(R.id.btnSignUpAsStudent)
        btnSignUpAsTutor = findViewById(R.id.btnSignUpAsTutor)


        btnSignUpAsStudent.setOnClickListener {
            val intent = Intent(this,SignUpActivity::class.java)
            startActivity(intent)
        }
        btnSignUpAsTutor.setOnClickListener {
            val intent = Intent(this,SignUpAsTutorActivity::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()

            Login(email,password)
        }

    }

    private fun Login(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val uid = mAuth.currentUser?.uid
                if (uid != null) {
                    val databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                    databaseReference.child(uid).child("userType").get().addOnSuccessListener { snapshot ->
                        val userType = snapshot.value as? String
                        when (userType) {
                            "student" -> {
                                // Student navigates to TutorActivity
                                val intent = Intent(this@LoginActivity, TutorActivity::class.java)
                                finish()
                                startActivity(intent)
                            }
                            "tutor" -> {
                                // Tutor navigates to StudentActivity
                                val intent = Intent(this@LoginActivity, StudentActivity::class.java)
                                finish()
                                startActivity(intent)
                            }
                            else -> {
                                Toast.makeText(this, "User type not found", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }.addOnFailureListener {
                        Toast.makeText(this, "Failed to retrieve user type", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else {
                Toast.makeText(this, "User Does Not Exist", Toast.LENGTH_SHORT).show()
            }
        }
    }
}