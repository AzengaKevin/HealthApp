package com.maze.healthapp.utils

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.maze.healthapp.ui.HomeActivity
import com.maze.healthapp.ui.SplashActivity

fun Context.toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

fun Context.login() {

    val intent = Intent(this, HomeActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    startActivity(intent)
}

fun Context.logout() {
    val intent = Intent(this, SplashActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    startActivity(intent)
}