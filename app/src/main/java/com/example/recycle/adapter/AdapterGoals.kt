package com.example.recycle.adapter

import android.media.ThumbnailUtils
import android.provider.MediaStore
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.recycle.R
import com.example.recycle.databinding.FragmentCardViewGoalsBinding
import com.example.recycle.model.GoalsModel
import com.example.recycle.model.LeaderboardUser

class AdapterGoals(var Goals:ArrayList<GoalsModel>): RecyclerView.Adapter<AdapterGoals.ViewHolder>()  {
    var clickListener:ClickListener?=null

    fun onClickListeer( clickListener:ClickListener)
    {
        this.clickListener=clickListener
    }
    interface ClickListener{
        fun OnClick(position:Int)
    }
    inner class ViewHolder(val binding:FragmentCardViewGoalsBinding):RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                clickListener?.OnClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater=LayoutInflater.from(parent.context)
        val cardViewLecturesBinding:FragmentCardViewGoalsBinding=DataBindingUtil.inflate(layoutInflater,R.layout.fragment_card_view_goals,parent,false)
        return ViewHolder(cardViewLecturesBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.GoalsDescription.text=Goals[position].content.toString()
        holder.binding.TitleGoals.text=Goals[position].title.toString()
        holder.binding.CardView.setOnClickListener {
            var goalsdescription=if(holder.binding.GoalsDescription.visibility==View.GONE)

            {
                View.VISIBLE
            }
            else
            {
                View.GONE
            }
            TransitionManager.beginDelayedTransition(holder.binding.CardView,AutoTransition())
            holder.binding.GoalsDescription.visibility=goalsdescription
            holder.binding.button.visibility=goalsdescription
        }
    }

    override fun getItemCount(): Int {
        return Goals.size
    }

}