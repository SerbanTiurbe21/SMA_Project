package com.example.wanderlog

import android.os.Bundle
import android.view.Menu
import android.widget.TextView
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.wanderlog.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var userNameTextView: TextView
    private lateinit var userEmailTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_aboutUs, R.id.nav_contact, R.id.nav_share
            ), drawerLayout
        )

        val headerView = navView.getHeaderView(0)
        userNameTextView = headerView.findViewById(R.id.textViewName)
        userEmailTextView = headerView.findViewById(R.id.textViewEmail)
        val sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE)
        val email: String? = sharedPreferences.getString("EMAIL", null)
        val name = extractNameFromEmail(email!!)
        updateUI(name, email)

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun updateUI(name: String, email: String) {
        userNameTextView.text = name
        userEmailTextView.text = email
    }

    private fun extractNameFromEmail(email: String): String {
        val usernamePart = email.substringBefore("@")
        return usernamePart.uppercase()
    }
}