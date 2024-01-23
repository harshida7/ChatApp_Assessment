package com.example.chatapp_assessment.Activity

import com.example.chatapp_assessment.databinding.ActivityLoginChatBinding

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp

import com.google.firebase.auth.FirebaseAuth

class LoginChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginChatBinding
    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginChatBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        FirebaseApp.initializeApp(this)
        mAuth = FirebaseAuth.getInstance()

        val cuser = mAuth.currentUser

        if (cuser != null) {
            // User is signed in
            Toast.makeText(applicationContext, "Yayy!!... Enjoy...", Toast.LENGTH_SHORT).show()
            startActivity(Intent(applicationContext,DashboardActivity::class.java))
        } else {
            // No user is signed in
        }
        binding.btnRegister.setOnClickListener {
            var i = Intent(applicationContext,RegisterChatActivity::class.java)
            startActivity(i)
        }

        binding.btnLogin.setOnClickListener {

            var email = binding.edtEmail.text.toString()
            var pass = binding.edtPassword.text.toString()

            login(email,pass)

        }
    }

    private fun login(email: String, pass: String) {
        mAuth.signInWithEmailAndPassword(email,pass)
            .addOnCompleteListener(this) { task->
                if(task.isSuccessful)
                {
                    //code for logging in user
                    var i = Intent(applicationContext, DashboardActivity::class.java)
                    finish()
                    startActivity(i)
                }
                else
                {
                    Toast.makeText(applicationContext, "user does not exist", Toast.LENGTH_SHORT).show()
                }
            }
    }


    override fun onBackPressed()
    {
        super.onBackPressed()
        var builder = AlertDialog.Builder(this)
        builder.setTitle("Are you sure you want to exit?")
        builder.setPositiveButton("YES",{ dialogInterface: DialogInterface, i: Int -> finishAffinity()})
        builder.setNegativeButton("NO",{ dialogInterface: DialogInterface, i: Int ->
            dialogInterface.cancel()
        })
        builder.show()
        }
}