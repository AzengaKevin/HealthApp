package com.maze.healthapp.ui.frags

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation

import com.maze.healthapp.R
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        bmiCard.setOnClickListener {
            val action = HomeFragmentDirections.actionCheckBMI()
            Navigation.findNavController(it).navigate(action)
        }

        bpCard.setOnClickListener {
            val action = HomeFragmentDirections.actionCheckBP()
            Navigation.findNavController(it).navigate(action)
        }

        bslCard.setOnClickListener {
            val action = HomeFragmentDirections.actionCheckBSL()
            Navigation.findNavController(it).navigate(action)
        }

        usersCard.setOnClickListener {
            val action = HomeFragmentDirections.actionViewAllUsers()
            Navigation.findNavController(it).navigate(action)

        }

    }

}
