package com.maze.healthapp.ui.frags


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation

import com.maze.healthapp.R
import kotlinx.android.synthetic.main.fragment_chats.*

class ChatsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chats, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        chatsRV.visibility = View.GONE

        chatFAB.setOnClickListener {
            val action = ChatsFragmentDirections.actionGetUser()
            Navigation.findNavController(it).navigate(action)
        }
    }


}
