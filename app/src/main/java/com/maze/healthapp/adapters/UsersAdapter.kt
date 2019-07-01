package com.maze.healthapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.maze.healthapp.R
import com.maze.healthapp.models.User
import kotlinx.android.synthetic.main.single_user_view.view.*

class UsersAdapter(val users: List<User>) : RecyclerView.Adapter<UsersAdapter.UserHolder>() {

    private lateinit var ctx: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {

        ctx = parent.context

        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_user_view, parent, false)
        return UserHolder(view)
    }

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: UserHolder, position: Int) {

        val user = users[position]

        user.photoUrl.let {
            Glide.with(ctx)
                .load(user.photoUrl)
                .into(holder.avatarCIV)
        }

        holder.usernameTV.setText(user.name)


    }


    inner class UserHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mView = itemView
        val avatarCIV = itemView.avatar
        val usernameTV = itemView.username

    }
}