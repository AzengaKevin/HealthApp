package com.maze.healthapp.ui.frags


import android.os.Bundle
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
import kotlinx.android.synthetic.main.fragment_bsl.*

/**
 * A simple [Fragment] subclass.
 *
 */
class BSLFragment : Fragment() {
    private val TAG = "BSLFragment"

    var recommendations = mutableListOf<Recommendation>()

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
        checkBSLConditionsLayout.visibility = View.GONE

        bslRecBtn.setOnClickListener {
            ll1.visibility = View.GONE
            recommendationsRelativeLayout.visibility = View.VISIBLE
            checkBSLConditionsLayout.visibility = View.GONE

            FirebaseFirestore.getInstance().collection("bsl")
                .addSnapshotListener { querySnapshots, exception ->

                    if (exception != null) {
                        Log.e(TAG, "onViewCreated", exception)
                        return@addSnapshotListener
                    }

                    querySnapshots?.let { snapshots ->
                        bslRecRV.layoutManager = LinearLayoutManager(activity)
                        bslRecRV.setHasFixedSize(true)

                        recommendations.clear()
                        snapshots.forEach { snapshot ->
                            val rec = snapshot.toObject(Recommendation::class.java)
                            recommendations.add(rec)
                        }

                        val adapter = RecommendationAdapter(recommendations)
                        bslRecRV.adapter = adapter
                    }

                }
        }

        checkBSLSymptomsBtn.setOnClickListener {
            ll1.visibility = View.GONE
            recommendationsRelativeLayout.visibility = View.GONE
            checkBSLConditionsLayout.visibility = View.VISIBLE

        }

        getSuggestionsBtn.setOnClickListener {
            ll1.visibility = View.GONE
            recommendationsRelativeLayout.visibility = View.VISIBLE
            checkBSLConditionsLayout.visibility = View.GONE

            FirebaseFirestore.getInstance().collection("bsl")
                .addSnapshotListener { querySnapshots, exception ->

                    if (exception != null) {
                        Log.e(TAG, "onViewCreated", exception)
                        return@addSnapshotListener
                    }

                    querySnapshots?.let { snapshots ->
                        bslRecRV.layoutManager = LinearLayoutManager(activity)
                        bslRecRV.setHasFixedSize(true)

                        recommendations.clear()
                        snapshots.forEach { snapshot ->
                            val rec = snapshot.toObject(Recommendation::class.java)
                            recommendations.add(rec)
                        }

                        val adapter = RecommendationAdapter(recommendations)
                        bslRecRV.adapter = adapter
                    }

                }

        }
    }


}
