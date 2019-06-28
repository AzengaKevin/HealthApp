package com.maze.healthapp.ui.frags


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.maze.healthapp.R
import kotlinx.android.synthetic.main.fragment_blood_pressure.*

/**
 * A simple [Fragment] subclass.
 *
 */
class BloodPressureFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blood_pressure, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        ll1.visibility = View.VISIBLE
        recommendationsRelativeLayout.visibility = View.GONE

        recommendationsBtn.setOnClickListener {
            ll1.visibility = View.GONE
            recommendationsRelativeLayout.visibility = View.VISIBLE
        }


    }


}
