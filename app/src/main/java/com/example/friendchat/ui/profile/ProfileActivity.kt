package com.example.friendchat.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.friendchat.data.preferences.AppPreferences
import com.example.friendchat.databinding.ActivityProfileBinding
import com.example.friendchat.ui.SplashActivity
import com.example.friendchat.ui.auth.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {

    private var _binding: ActivityProfileBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var appPreferences: AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.let {
            it.setNavigationOnClickListener { finish() }
        }
        binding.llLogOut.setOnClickListener {
            logOutUser()
        }

    }

    private fun logOutUser() {
        lifecycleScope.launch {
            appPreferences.setIsAuthenticated(false) // Clear authenticated state
        }

        FirebaseAuth.getInstance().signOut() // Sign out from FirebaseAuth

        val intent = Intent(this, SplashActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}