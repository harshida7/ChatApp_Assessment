package com.example.chatapp_assessment.Activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.example.chatapp_assessment.Model.RegisterUserModel
import com.example.chatapp_assessment.R
import com.example.chatapp_assessment.databinding.ActivityLoginChatBinding
import com.example.chatapp_assessment.databinding.ActivityRegisterChatBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class RegisterChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterChatBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference
    companion object {
        private const val IMAGE_PICK_REQUEST = 123
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityRegisterChatBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        FirebaseApp.initializeApp(this)

        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference

        binding.profileImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, IMAGE_PICK_REQUEST)
        }

        binding.btnCreateAcnt.setOnClickListener {

            var unm = binding.edtUserName.text.toString()
            var email = binding.edtEmail.text.toString()
            var password = binding.edtPassword.text.toString()


            registerUser(unm, email, password)
            Toast.makeText(applicationContext, "User Registered", Toast.LENGTH_SHORT).show()

        }
    }

    private fun registerUser(username: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val firebaseUser = auth.currentUser
                    val userId = firebaseUser?.uid ?: ""

                    // Save user details to Firebase Realtime Database
                    saveUserToDatabase(userId, username, email,password)

                    Toast.makeText(applicationContext, "Registration successful", Toast.LENGTH_SHORT).show()
                } else {
                    val exception = task.exception
                    Toast.makeText(applicationContext, "Registration failed: ${exception?.message}", Toast.LENGTH_SHORT).show()
                    Log.e("RegistrationError", "Registration failed", exception)
                }
            }
    }

    private fun saveUserToDatabase(userId: String, username: String, email: String,password: String) {
        val database = FirebaseDatabase.getInstance().reference
        val user = RegisterUserModel(username, email, password)

        // Save user details to the "users" node with the user's ID
        database.child("Register User").child(userId).setValue(user)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_PICK_REQUEST && resultCode == Activity.RESULT_OK) {
            val selectedImageUri: Uri? = data?.data
            if (selectedImageUri != null) {
                // Upload the selected image to Firebase Storage
                uploadImage(selectedImageUri)
            }
        }
    }
    private fun uploadImage(imageUri: Uri) {
        val userId = auth.currentUser?.uid ?: ""

        // Create a reference to the location in Firebase Storage where the image will be stored
        val imageRef: StorageReference = storageReference.child("images/$userId/image.jpg")

        // Upload the image to Firebase Storage
        imageRef.putFile(imageUri)
            .addOnSuccessListener {
                // Image uploaded successfully
                Toast.makeText(this, "Image uploaded", Toast.LENGTH_SHORT).show()

                // Get the download URL of the uploaded image
                imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    // Save the download URL to Firebase Realtime Database
                    saveImageUrlToDatabase(userId, downloadUri.toString())
                }
            }
            .addOnFailureListener { e ->
                // Handle the failure
                Toast.makeText(this, "Image upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
    private fun saveImageUrlToDatabase(userId: String, imageUrl: String) {
        // Save the image URL to the "users" node in Firebase Realtime Database
        val database = FirebaseDatabase.getInstance().reference
        database.child("Register User Profile").child(userId).child("image_url").setValue(imageUrl)
    }
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}