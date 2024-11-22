package com.example.friendchat.ui.auth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.friendchat.R
import com.example.friendchat.data.preferences.AppPreferences
import com.example.friendchat.databinding.ActivityRegistrationBinding
import com.example.friendchat.ui.MainActivity
import com.example.friendchat.util.setupUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RegistrationActivity : AppCompatActivity() {

    private var _binding: ActivityRegistrationBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var appPreferences: AppPreferences

    private lateinit var auth: FirebaseAuth
    private var selectedImageUri: Uri? = null

    private val pickImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            selectedImageUri = uri
            binding.userPhoto.setImageURI(uri)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUI(findViewById(R.id.root), this)

        auth = Firebase.auth

        binding.btnContinue.setOnClickListener { signUpUser() }
        binding.userPhoto.setOnClickListener { pickImage.launch("image/*") }
        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun signUpUser() {
        val name = binding.editTextUserName.text.toString().trim()
        val email = binding.editTextEmail.text.toString().trim()
        val pass = binding.editTextPassword.text.toString()
        val confirmPass = binding.editTextConfirmPassword.text.toString()

        if (name.isBlank() || email.isBlank() || pass.isBlank() || confirmPass.isBlank()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show()
            return
        }

        if (pass != confirmPass) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                auth.currentUser?.let {
                    saveUserData(it.uid, name, email)
                }
            } else {
                Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveUserData(userId: String, name: String, email: String) {
        val db = Firebase.firestore
        val user = hashMapOf("id" to userId, "name" to name, "email" to email)
        db.collection("users").document(userId).set(user)
            .addOnSuccessListener {
                lifecycleScope.launch { appPreferences.setIsAuthenticated(true) }
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to save user data: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}