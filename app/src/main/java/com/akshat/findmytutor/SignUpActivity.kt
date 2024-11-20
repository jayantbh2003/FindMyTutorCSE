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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {
    private lateinit var edtEmail : EditText
    private lateinit var edtName : EditText
    private lateinit var edtPassword : EditText
    private lateinit var btnSignUp : Button
    private lateinit var mAuth : FirebaseAuth
    private lateinit var mDbRef : DatabaseReference
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)

        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().getReference("Users")

        edtEmail = findViewById(R.id.edtEmail)
        edtPassword = findViewById(R.id.edtPassword)
        edtName = findViewById(R.id.edtName)
        btnSignUp = findViewById(R.id.btnSignUp)

        btnSignUp.setOnClickListener {
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()
            val name  = edtName.text.toString()

            siningAsStudent(name,email,password)
        }
    }

    private fun siningAsStudent(name : String, email:String, password:String){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val userId = mAuth.currentUser?.uid
                if (userId != null) {
                    addStudentDataToDatabase(name, email, userId)
                    val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                    finish()
                    startActivity(intent)
                }
                else {
                    Toast.makeText(this, "User ID is null", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this@SignUpActivity,"Some Error Occurred",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addStudentDataToDatabase(name: String, email: String, uid: String?) {
        val studentData = mapOf("name" to name, "email" to email, "userType" to "student", "uid" to uid)
        if (uid != null) {
            mDbRef.child(uid).setValue(studentData).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Student Registration successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to add student data: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}