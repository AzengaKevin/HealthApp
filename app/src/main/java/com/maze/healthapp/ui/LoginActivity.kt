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
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        registerTV.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }

        resetPwdTV.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ResetPasswordActivity::class.java))
        }

        loginBtn.setOnClickListener {
            val email: String = emailET.text.toString().trim()
            val pwd: String = pwdET.text.toString().trim()

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

            loginUser(email, pwd)
        }
    }

    private fun loginUser(email: String, pwd: String) {
        loginProgress.visibility = View.VISIBLE
        mAuth.signInWithEmailAndPassword(email, pwd)
            .addOnCompleteListener { task ->
                loginProgress.visibility = View.GONE
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
