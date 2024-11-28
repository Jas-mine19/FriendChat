package com.example.friendchat.ui

import android.content.Intent
import com.example.friendchat.ui.chat.ChatFragment
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.friendchat.R
import com.example.friendchat.data.preferences.AppPreferences
import com.example.friendchat.databinding.ActivityMainBinding
import com.example.friendchat.ui.profile.ProfileActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var dataStoreManager: AppPreferences

    private lateinit var navController: NavController
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        lifecycleScope.launch {
            dataStoreManager.userId.collect { id ->
                userId = id

                if (id == null) {
                    Toast.makeText(this@MainActivity, "User ID not found!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        setupBottomNavigation()

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.chatFragment, R.id.userFragment -> binding.bottomNav.visibility = View.VISIBLE
                else -> binding.bottomNav.visibility = View.GONE
            }
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNav.setupWithNavController(navController)

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_chat -> navigateToChatFragment()
                R.id.nav_users -> navigateToUserFragment()
            }
            true
        }
    }

    private fun navigateToChatFragment() {
        userId?.let {
            val bundle = Bundle().apply { putString("userId", it) }
            navController.navigate(R.id.chatFragment, bundle)
        } ?: Toast.makeText(this, "User ID is not available", Toast.LENGTH_SHORT).show()
    }

    private fun navigateToUserFragment() {
        userId?.let {
            val bundle = Bundle().apply { putString("userId", it) }
            navController.navigate(R.id.userFragment, bundle)
        } ?: Toast.makeText(this, "User ID is not available", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.profile_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}



