package com.maze.healthapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.maze.healthapp.R
import com.maze.healthapp.models.Recommendation
import kotlinx.android.synthetic.main.single_recommendation_view.view.*

class RecommendationAdapter(private val recommendations: MutableList<Recommendation>) :
    RecyclerView.Adapter<RecommendationAdapter.RecommendationHolder>() {

    private lateinit var ctx: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationHolder {
        ctx = parent.context

        val view = LayoutInflater.from(ctx).inflate(R.layout.single_recommendation_view, parent, false)

        return RecommendationHolder(view)

    }

    override fun getItemCount(): Int = recommendations.size

    override fun onBindViewHolder(holder: RecommendationHolder, position: Int) {
        val recommendation = recommendations[position]

        Glide.with(ctx)
            .load(recommendation.photoUrl)
            .into(holder.image)

        holder.recText.text = recommendation.content

    }


    inner class RecommendationHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val image = itemView.recImage
        val recText = itemView.recContent

    }
}