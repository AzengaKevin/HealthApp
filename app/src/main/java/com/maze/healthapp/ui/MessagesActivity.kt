package com.maze.healthapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.maze.healthapp.R
import com.maze.healthapp.adapters.ChatAdapter
import com.maze.healthapp.models.Chat
import com.maze.healthapp.models.User
import com.maze.healthapp.utils.toast
import kotlinx.android.synthetic.main.activity_messages.*

class MessagesActivity : AppCompatActivity() {

    private val TAG = "MessagesActivity"

    private var uid: String? = null
    private val db = FirebaseFirestore.getInstance()
    private val messages: MutableList<Chat> = mutableListOf()

    override

    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)

        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            title = ""
            setDisplayHomeAsUpEnabled(true)
        }

        messagesRV.layoutManager = LinearLayoutManager(this@MessagesActivity)!!

        uid = intent?.extras!!.getString("uid")

        uid?.let {

            db.document("users/$uid")
                .addSnapshotListener { documentSnapshot, e ->
                    if (e != null) {
                        Log.e(TAG, "onCreate", e)
                        return@addSnapshotListener
                    }

                    documentSnapshot?.let { snapshot ->
                        val user = snapshot.toObject(User::class.java)

                        user?.let {
                            recipientUsernameTV.text = it.name
                            Glide.with(this)
                                .load(it.photoUrl)
                                .into(recipientCIV)
                        }
                    }
                }

        }

        sendIB.setOnClickListener {
            val msg = messageET.text.toString().trim()

            if (msg.isNullOrEmpty()) {
                messageET.error = "Please set message"
                messageET.requestFocus()
                return@setOnClickListener
            }

            val sender = FirebaseAuth.getInstance().uid

            val data = hashMapOf(
                "sender" to sender,
                "receiver" to uid,
                "msg" to msg,
                "timestamp" to FieldValue.serverTimestamp()
            )


            db.collection("chats")
                .add(data)
                .addOnSuccessListener {

                    toast("Message Sent")
                    messageET.setText("")

                }
                .addOnFailureListener { e ->

                    Log.e(TAG, "onCreate.onClickListener", e)

                }

        }


        readusers()

    }

    private fun readusers() {

        db.collection("chats").orderBy("timestamp")
            .addSnapshotListener { querySnapshot, exception ->
                if (exception != null) {
                    Log.e(TAG, "readUsers", exception)
                    return@addSnapshotListener
                }
                querySnapshot?.let {

                    messages.clear()
                    for (snapshot in it) {

                        val chat = snapshot.toObject(Chat::class.java)

                        if (chat.sender == FirebaseAuth.getInstance().uid || chat.reciver == FirebaseAuth.getInstance().uid)
                            messages.add(chat)
                    }

                    val adapter = ChatAdapter(messages)

                    messagesRV.adapter = adapter

                }
            }
    }
}
