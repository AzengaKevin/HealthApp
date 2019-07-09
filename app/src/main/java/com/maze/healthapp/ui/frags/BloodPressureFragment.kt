package com.maze.healthapp.ui.frags


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

import com.maze.healthapp.R
import com.maze.healthapp.adapters.RecommendationAdapter
import com.maze.healthapp.models.Recommendation
import com.maze.healthapp.utils.toast
import kotlinx.android.synthetic.main.fragment_blood_pressure.*

class BloodPressureFragment : Fragment() {

    private val TAG = "BloodPressureFragment"

    var recommendations = mutableListOf<Recommendation>()


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
        checkConditions.visibility = View.GONE

        recommendationsBtn.setOnClickListener {
            val diastole = diastoleET.text.toString().trim().toInt()
            val systole = systoleET.text.toString().trim().toInt()

            ll1.visibility = View.GONE
            recommendationsRelativeLayout.visibility = View.VISIBLE
            checkConditions.visibility = View.GONE


            val query: Query = FirebaseFirestore.getInstance().collection("bp")


            if (diastole in 80..89 && systole in 120..139) {
                query.whereGreaterThanOrEqualTo("diastole", 80)
                    .whereLessThan("diastole", 90)
//                    .whereGreaterThanOrEqualTo("systole", 120)
//                    .whereLessThan("systole", 140)
            } else if (diastole in 60..79 && systole in 100..119) {

                query.whereGreaterThanOrEqualTo("diastole", 60)
                    .whereLessThan("diastole", 80)
//                    .whereGreaterThanOrEqualTo("systole", 100)
//                    .whereLessThan("systole", 120)
            } else if (diastole < 60 && systole < 100) {
                query.whereLessThan("diastole", 60)
//                    .whereLessThan("systole", 100)
            } else if (diastole > 90 && systole < 140) {
                query.whereGreaterThanOrEqualTo("diastole", 90)
//                    .whereGreaterThan("systole", 140)
            }else{

                query.whereEqualTo("diastole", 80)
            }

            query
                .whereGreaterThanOrEqualTo("diastole", 80)
                .whereEqualTo("diastole", diastole)
                .whereEqualTo("systole", systole)
                .addSnapshotListener { querySnapshot, e ->

                    if (e != null) {
                        Log.e(TAG, "onViewCreated", e)
                        return@addSnapshotListener
                    }

                    recommendations.clear()

                    querySnapshot?.let { querySnapshots ->

                        activity?.toast("Found Some Recommendations")

                        recommendationRV.layoutManager = LinearLayoutManager(activity)

                        for (snapshot in querySnapshots) {
                            val rec = snapshot.toObject(Recommendation::class.java)
                            recommendations.add(rec)
                        }

                        val adapter = RecommendationAdapter(recommendations)
                        recommendationRV.adapter = adapter

                    }


                }


        }

        checkActionsBtn.setOnClickListener {

            ll1.visibility = View.GONE
            recommendationsRelativeLayout.visibility = View.GONE
            checkConditions.visibility = View.VISIBLE
        }

        submit.setOnClickListener {
            ll1.visibility = View.GONE
            recommendationsRelativeLayout.visibility = View.VISIBLE
            checkConditions.visibility = View.GONE

            FirebaseFirestore.getInstance().collection("bp")
                .addSnapshotListener { querySnapshot, e ->

                    if (e != null) {
                        Log.e(TAG, "onViewCreated", e)
                        return@addSnapshotListener
                    }

                    recommendations.clear()

                    querySnapshot?.let { querySnapshots ->

                        activity?.toast("Found Some Recommendations")

                        recommendationRV.layoutManager = LinearLayoutManager(activity)

                        for (snapshot in querySnapshots) {
                            val rec = snapshot.toObject(Recommendation::class.java)
                            recommendations.add(rec)
                        }

                        val adapter = RecommendationAdapter(recommendations)
                        recommendationRV.adapter = adapter

                    }


                }


        }

    }

}
