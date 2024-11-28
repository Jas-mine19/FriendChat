package com.example.friendchat.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.friendchat.ui.auth.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.isAuthenticated.collect { isAuthenticated ->
                val nextIntent = if (FirebaseAuth.getInstance().currentUser != null && isAuthenticated) {
                    Intent(this@SplashActivity, MainActivity::class.java)
                } else {
                    Intent(this@SplashActivity, LoginActivity::class.java)
                }
                startActivity(nextIntent)
                finish()
            }
        }
    }
}