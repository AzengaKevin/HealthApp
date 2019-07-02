package com.maze.healthapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.maze.healthapp.models.Chat
import kotlinx.android.synthetic.main.chat_left.view.msgTV
import com.maze.healthapp.R

class ChatAdapter(val chats: MutableList<Chat>) : RecyclerView.Adapter<ChatAdapter.ChatHolder>() {

    val CHATLEFT = 22
    val CHATRIGHT = 7

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatHolder {
        var view: View?

        when (viewType) {
            CHATRIGHT -> view = LayoutInflater.from(parent.context).inflate(R.layout.chat_right, parent, false)
            CHATLEFT -> view = LayoutInflater.from(parent.context).inflate(R.layout.chat_left, parent, false)
            else -> view = LayoutInflater.from(parent.context).inflate(R.layout.chat_left, parent, false)
        }

        return ChatHolder(view)
    }

    override fun getItemCount(): Int = chats.size

    override fun onBindViewHolder(holder: ChatHolder, position: Int) {
        holder.msgTV.text = chats[position].msg
    }

    override fun getItemViewType(position: Int): Int {
        val chat = chats[position]

        if (chat.sender == FirebaseAuth.getInstance().uid) return CHATRIGHT
        else return CHATLEFT
    }

    inner class ChatHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val msgTV = itemView.msgTV
    }
}