package com.maze.healthapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.maze.healthapp.R
import com.maze.healthapp.utils.login
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        registerBtn.setOnClickListener {
            startActivity(Intent(this@SplashActivity, RegisterActivity::class.java))
        }

        loginBtn.setOnClickListener {
            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
        }

    }

    override fun onStart() {
        super.onStart()

        if (FirebaseAuth.getInstance().currentUser != null) login()
    }
}
