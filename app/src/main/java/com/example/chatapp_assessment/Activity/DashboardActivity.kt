package com.example.chatapp_assessment.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.chatapp_assessment.R
import com.example.chatapp_assessment.databinding.ActivityDashboardBinding
import com.example.chatapp_assessment.databinding.ActivityLoginChatBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        FirebaseApp.initializeApp(this)

        auth = FirebaseAuth.getInstance()

        setSupportActionBar(binding.toolbar1)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId)
        {

            R.id.acntSettings->
            {
                Toast.makeText(applicationContext, "Settings Selected", Toast.LENGTH_SHORT).show()
            }
            R.id.allUsers->
            {
                Toast.makeText(applicationContext, "All Users Selected", Toast.LENGTH_SHORT).show()

            }
            R.id.logout->
            {
                auth.signOut()
                startActivity(Intent(applicationContext,LoginChatActivity::class.java))
            }
        }

        return super.onOptionsItemSelected(item)
    }
}