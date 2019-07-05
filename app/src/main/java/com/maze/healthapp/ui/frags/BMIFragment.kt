package com.maze.healthapp.ui.frags


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore

import com.maze.healthapp.R
import com.maze.healthapp.adapters.RecommendationAdapter
import com.maze.healthapp.models.Recommendation
import kotlinx.android.synthetic.main.fragment_bmi.*


class BMIFragment : Fragment() {

    private val TAG: String = "BMIFragment"
    private var height = 0.0
    private var weight = 0.0

    private var reccomendations = mutableListOf<Recommendation>()


    private val heightWatcher = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            //Relax
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            //Relax
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            if (s.isNullOrEmpty()) return
            s?.let {
                height = it.toString().trim().toDouble()

                calculateBMI()
            }

        }

    }

    private val weightWatcher = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {

        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, end: Int, count: Int) {
            if (s.isNullOrEmpty()) return

            s?.let {
                weight = it.toString().trim().toDouble()

                calculateBMI()
            }
        }
    }

    private fun calculateBMI() {
        var bmi = (weight / (height * height))

        var sBmi = "Your BMI: $bmi"

        bmiDisplay.text = sBmi
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bmi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        heightET.addTextChangedListener(heightWatcher)
        weightET.addTextChangedListener(weightWatcher)

        requestBMIRelativeLayout.visibility = View.VISIBLE
        recommendationsRelativeLayout.visibility = View.GONE

        recommendationsBtn.setOnClickListener {

            requestBMIRelativeLayout.visibility = View.GONE
            recommendationsRelativeLayout.visibility = View.VISIBLE

            FirebaseFirestore.getInstance().collection("bmi")
                .addSnapshotListener { querySnapshots, exception ->
                    if (exception != null) {
                        Log.e(TAG, "onViewCreated", exception)

                        return@addSnapshotListener
                    }

                    querySnapshots?.let {

                        bmiRV.layoutManager = LinearLayoutManager(activity)

                        for (snapshot in querySnapshots) {
                            val rec = snapshot.toObject(Recommendation::class.java)
                            reccomendations.add(rec)
                        }

                        val adapter = RecommendationAdapter(reccomendations)

                        bmiRV.adapter = adapter
                    }
                }


        }

    }


}
