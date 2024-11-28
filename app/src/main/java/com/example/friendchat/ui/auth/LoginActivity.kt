package com.example.friendchat.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.friendchat.R
import com.example.friendchat.data.preferences.AppPreferences
import com.example.friendchat.databinding.ActivityLoginBinding
import com.example.friendchat.ui.MainActivity
import com.example.friendchat.util.setupUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var appPreferences: AppPreferences



    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        setupUI(findViewById(R.id.root), this)

        binding.btnContinue.setOnClickListener { signInUser() }
        binding.tvSignUp.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
            finish()
        }
    }

    private fun signInUser() {
        val email = binding.editTextEmail.text.toString().trim()
        val pass = binding.editTextPassword.text.toString()

        if (email.isBlank() || pass.isBlank()) {
            Toast.makeText(this, "Email and Password cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        binding.progressBar.visibility = View.VISIBLE
        binding.btnContinue.visibility = View.GONE

        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this) { task ->
            binding.progressBar.visibility = View.GONE
            binding.btnContinue.visibility = View.VISIBLE
            if (task.isSuccessful) {
                val userId = auth.currentUser?.uid
                if (userId != null) {
                    lifecycleScope.launch {
                        appPreferences.saveUserId(userId)
                        appPreferences.setIsAuthenticated(true)
                    }

                    val intent = Intent(this, MainActivity::class.java).apply {
                        putExtra("userId", userId)
                    }
                    startActivity(intent)
                    finish()
                }
            } else {
                binding.progressBar.visibility = View.GONE
                binding.btnContinue.visibility = View.VISIBLE
                when (val error = task.exception) {
                    is FirebaseAuthException -> {
                        when (error.errorCode) {
                            "ERROR_NETWORK_REQUEST_FAILED" -> {
                                Toast.makeText(this, "Network error. Please try again.", Toast.LENGTH_SHORT).show()
                            }
                            else -> {
                                Toast.makeText(this, "Login failed: ${error.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    else -> {
                        Toast.makeText(this, "Unexpected error: ${error?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}