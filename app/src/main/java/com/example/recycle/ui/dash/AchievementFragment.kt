package com.example.recycle.ui.dash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.recycle.R
import com.example.recycle.adapter.LeaderboardAdapter
import com.example.recycle.databinding.FragmentAchievementBinding
import com.example.recycle.model.LeaderboardUser

class AchievementFragment : Fragment() {

    lateinit var binding: FragmentAchievementBinding
    private val leaderboardAdapter = LeaderboardAdapter()
    private val userList = mutableListOf<LeaderboardUser>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userList.add(LeaderboardUser("Sandeep", 100, resources.getDrawable(R.drawable.avtaar),1))
        userList.add(LeaderboardUser("Ananya", 50, resources.getDrawable(R.drawable.avtaar),2))
        userList.add(LeaderboardUser("Suyash", 40, resources.getDrawable(R.drawable.avtaar),3))
        userList.add(LeaderboardUser("Ayush", 10, resources.getDrawable(R.drawable.avtaar),4))
        userList.add(LeaderboardUser("Kunal", 1, resources.getDrawable(R.drawable.avtaar),5))

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_achievement,container,false)


        leaderboardAdapter.updateLeaderboard(userList)
        binding.leaderboardRv.adapter =  leaderboardAdapter

        return binding.root
    }

}