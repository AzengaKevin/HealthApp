package com.maze.healthapp.ui.frags


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.maze.healthapp.R
import kotlinx.android.synthetic.main.fragment_bmi.*


/**
 * A simple [Fragment] subclass.
 *
 */
class BMIFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bmi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestBMIRelativeLayout.visibility = View.VISIBLE
        recommendationsRelativeLayout.visibility = View.GONE

        recommendationsBtn.setOnClickListener {

            requestBMIRelativeLayout.visibility = View.GONE
            recommendationsRelativeLayout.visibility = View.VISIBLE

        }

    }


}
