package com.example.recycle.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.recycle.R
import com.example.recycle.databinding.LeaderboardItemListBinding
import com.example.recycle.model.LeaderboardUser

class LeaderboardAdapter : RecyclerView.Adapter<LeaderboardAdapter.ViewHolder>() {

    var userList = mutableListOf<LeaderboardUser>()
    fun updateLeaderboard(list: List<LeaderboardUser>) {
        userList = list.toMutableList()
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: LeaderboardItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: LeaderboardUser) {
            binding.user = user
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: LeaderboardItemListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.leaderboard_item_list, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}