package com.example.friendchat.ui.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.friendchat.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private var _binding: ActivityProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.let {
            it.setNavigationOnClickListener { finish() }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}