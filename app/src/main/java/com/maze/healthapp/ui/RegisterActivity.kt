package com.maze.healthapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.maze.healthapp.R
import com.maze.healthapp.utils.login
import com.maze.healthapp.utils.toast
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mAuth = FirebaseAuth.getInstance()

        loginTV.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }


        registerBtn.setOnClickListener {
            val email = emailET.text.toString().trim()
            val pwd = pwdET.text.toString().trim()

            if (email.isEmpty()) {
                emailET.error = "Email Required"
                emailET.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                emailET.error = "Invalid Email"
                emailET.requestFocus()
                return@setOnClickListener
            }

            if (pwd.isEmpty() || pwd.length < 6) {
                pwdET.error = "Atleast 6 chars password required"
                pwdET.requestFocus()
                return@setOnClickListener
            }

            registerUser(email, pwd)

        }

    }

    private fun registerUser(email: String, pwd: String) {
        registerProgress.visibility = View.VISIBLE
        mAuth.createUserWithEmailAndPassword(email, pwd)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    login()
                } else {
                    task.exception?.message?.let {
                        toast(it)
                    }
                }
            }
    }

    override fun onStart() {
        super.onStart()
        if (mAuth.currentUser != null) {
            login()
        }
    }
}
