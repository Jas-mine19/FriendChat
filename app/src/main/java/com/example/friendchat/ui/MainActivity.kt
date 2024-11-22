package com.example.friendchat.ui

import com.example.friendchat.ui.chat.ChatFragment
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.friendchat.R
import com.example.friendchat.databinding.ActivityMainBinding
import com.example.friendchat.ui.user.UserFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
        }


        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        // Load default fragment
        loadFragment(ChatFragment())

        // Handle navigation selection
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.profile -> loadFragment(ChatFragment())
                R.id.nav_users -> loadFragment(UserFragment())
                else -> false
            }
            true
        }

        binding.toolbar.setOnMenuItemClickListener { item ->

            true

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.profile_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}



