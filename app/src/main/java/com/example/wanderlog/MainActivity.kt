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
import com.example.wanderlog.database.dto.UserDTO
import com.example.wanderlog.database.models.Trip
import com.example.wanderlog.databinding.ActivityMainBinding
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var userNameTextView: TextView
    private lateinit var userEmailTextView: TextView
    private lateinit var ratingTextView: TextView
    private lateinit var textViewLevel: TextView

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
        ratingTextView = headerView.findViewById(R.id.ratingTextView)
        textViewLevel = headerView.findViewById(R.id.textViewLevel)

        val currentUser: UserDTO? = retrieveCurrentUser()
        if (currentUser != null) {
            updateUI(extractNameFromEmail(currentUser.email), currentUser.email, calculateTripsRating(currentUser.trips), getUserLevel(currentUser.trips.size))
        }

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

    private fun updateUI(name: String, email: String, rating: String, level: String) {
        userNameTextView.text = name
        userEmailTextView.text = email
        ratingTextView.text = rating
        textViewLevel.text = level
    }

    private fun extractNameFromEmail(email: String): String {
        val usernamePart = email.substringBefore("@")
        return usernamePart.uppercase()
    }

    private fun retrieveCurrentUser(): UserDTO? {
        val sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE)
        val userJson = sharedPreferences.getString("USER", null)
        return if (userJson != null) {
            Gson().fromJson(userJson, UserDTO::class.java)
        } else {
            null
        }
    }

    private fun calculateTripsRating(trips: Set<Trip>): String {
        var rating = 0.0
        for (trip in trips) {
            rating += trip.rating
        }
        return (rating / trips.size).toString()
    }

    private fun getUserLevel(tripCount: Int): String {
        return when {
            tripCount >= 15 -> "Platinum Level"
            tripCount >= 10 -> "Gold Level"
            tripCount >= 5-> "Silver Level"
            else -> "Bronze Level"
        }
    }


}