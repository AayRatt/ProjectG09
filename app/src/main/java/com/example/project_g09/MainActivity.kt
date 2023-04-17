package com.example.project_g09

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = Firebase.firestore

        // setup the bottom navigation menu so it responds to clicks
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.container1) as NavHostFragment
        val navController = navHostFragment.navController
        setupWithNavController(findViewById<BottomNavigationView>(R.id.bottomNavigationView), navController)

    }


//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        val inflater: MenuInflater = menuInflater
//        inflater.inflate(R.menu.options_menu, menu)
//        return true
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle item selection
//        when (item.itemId) {
//            R.id.mi_settings -> {
//                val toast = Toast.makeText(this, "Settings Menu clicked!", Toast.LENGTH_SHORT)
//                toast.show()
//                return true
//            }
//            R.id.mi_profile -> {
//                val toast = Toast.makeText(this, "Profile Menu clicked!", Toast.LENGTH_SHORT)
//                toast.show()
//                return true
//            }
//            R.id.mi_logout -> {
//                val toast = Toast.makeText(this, "Logout Menu clicked!", Toast.LENGTH_SHORT)
//                toast.show()
//                return true
//            }
//            else -> {
//                return super.onOptionsItemSelected(item)
//            }
//        }
//    }



}