package com.maze.healthapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.maze.healthapp.R
import com.maze.healthapp.models.User
import com.maze.healthapp.utils.logout
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_bmi.view.*
import kotlinx.android.synthetic.main.nav_header.view.*

class HomeActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    val TAG = "HomeActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setSupportActionBar(toolbar)

        navController = Navigation.findNavController(this, R.id.fragment)
        NavigationUI.setupWithNavController(navView, navController)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        addRecTV.setOnClickListener {
            startActivity(Intent(this@HomeActivity, AddRecommendationsActivity::class.java))
        }

        mAuth.currentUser?.photoUrl?.let {

            Glide.with(this)
                .load(it)
                .into(navView.getHeaderView(0).avatar)

        }
        mAuth.currentUser?.displayName?.let {

            navView.getHeaderView(0).username.text = it

        }

    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, drawerLayout)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.main, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.nav_logout) {
            AlertDialog.Builder(this).apply {
                setTitle("Are you sure")

                setPositiveButton("Yes") { _, _ ->
                    FirebaseAuth.getInstance().signOut()
                    logout()
                }

                setNegativeButton("Cancel") { _, _ -> }
            }.create().show()

            return true
        }


        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()

        if (FirebaseAuth.getInstance().currentUser == null) {
            logout()
        } else {

            val docRef = db.collection("users").document("${FirebaseAuth.getInstance().currentUser?.uid}")

            docRef.get()
                .addOnSuccessListener {
                    if (it.exists()) {
                        val user = it.toObject(User::class.java)

                        user?.let {
                            if (it.role.equals("Expert")) {
                                addRecTV.visibility = View.VISIBLE
                            } else {
                                addRecTV.visibility = View.GONE
                            }
                        }
                    }
                }
                .addOnFailureListener {
                    Log.e(TAG, "onStart", it)
                }
        }
    }

}
