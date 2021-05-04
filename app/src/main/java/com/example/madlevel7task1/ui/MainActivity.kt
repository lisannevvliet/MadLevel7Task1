package com.example.madlevel7task1.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import com.example.madlevel7task1.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Set the app name as the toolbar title (instead of MainActivity).
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.title = getString(R.string.app_name)
        setSupportActionBar(toolbar)

        // Initialize Firestore.
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            FirebaseFirestore.setLoggingEnabled(true)
            FirebaseApp.initializeApp(this)
            findNavController(R.id.nav_host_fragment).navigate(R.id.CreateProfileFragment)
        }

        // Hide the floating action button.
        findViewById<FloatingActionButton>(R.id.fab).hide()
    }
}