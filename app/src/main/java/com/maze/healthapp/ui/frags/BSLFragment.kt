package com.maze.healthapp.ui.frags


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.maze.healthapp.R
import kotlinx.android.synthetic.main.fragment_bsl.*

/**
 * A simple [Fragment] subclass.
 *
 */
class BSLFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bsl, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        ll1.visibility = View.VISIBLE
        recommendationsRelativeLayout.visibility = View.GONE
        allBSLRecommendations.visibility = View.GONE
        checkBSLConditionsLayout.visibility = View.GONE

        bslRecBtn.setOnClickListener {
            ll1.visibility = View.GONE
            recommendationsRelativeLayout.visibility = View.VISIBLE
            allBSLRecommendations.visibility = View.GONE
            checkBSLConditionsLayout.visibility = View.GONE
        }

        checkBSLSymptomsBtn.setOnClickListener {
            ll1.visibility = View.GONE
            recommendationsRelativeLayout.visibility = View.GONE
            allBSLRecommendations.visibility = View.GONE
            checkBSLConditionsLayout.visibility = View.VISIBLE

        }

        getSuggestionsBtn.setOnClickListener {
            ll1.visibility = View.GONE
            recommendationsRelativeLayout.visibility = View.GONE
            allBSLRecommendations.visibility = View.VISIBLE
            checkBSLConditionsLayout.visibility = View.GONE

        }
    }


}
