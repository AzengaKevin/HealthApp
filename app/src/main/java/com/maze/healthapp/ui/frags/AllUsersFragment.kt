package com.maze.healthapp.ui.frags


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

import com.maze.healthapp.R
import com.maze.healthapp.adapters.UsersAdapter
import com.maze.healthapp.models.User
import com.maze.healthapp.utils.toast
import kotlinx.android.synthetic.main.fragment_all_users.*

class AllUsersFragment : Fragment() {
    val TAG = "AllUserFragment"

    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_users, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        usersRV.layoutManager = LinearLayoutManager(activity)

        db.collection("users")
            .addSnapshotListener { snapshots, e ->

                if (e != null) {
                    Log.e(TAG, "onViewCreated<Getting Users>", e)
                    return@addSnapshotListener
                }

                snapshots?.let {
                    activity?.toast("Seen User")

                    if (snapshots.isEmpty) {
                        msgTV.visibility = View.VISIBLE
                        usersRV.visibility = View.GONE
                    } else {

                        var users = mutableListOf<User>()

                        msgTV.visibility = View.GONE
                        usersRV.visibility = View.VISIBLE

                        users.clear()

                        for (snapshot in it) {

                            val user = snapshot.toObject(User::class.java)

                            if (!FirebaseAuth.getInstance().uid?.equals(user.uid)!!)
                                users.add(user)

                            Log.d(TAG, "I've got some users for you")
                        }

                        val adapter = UsersAdapter(users)

                        usersRV.adapter = adapter
                    }

                }

            }
    }

}
