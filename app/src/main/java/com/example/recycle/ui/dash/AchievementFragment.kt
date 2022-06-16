package com.example.recycle.ui.dash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.recycle.R
import com.example.recycle.databinding.FragmentAchievementBinding
import com.example.recycle.model.LeaderboardUser

class AchievementFragment : Fragment() {

    lateinit var binding: FragmentAchievementBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userList = mutableListOf<LeaderboardUser>()

        userList.add(LeaderboardUser("Sandeep",100,R.drawable.avtaar))
        userList.add(LeaderboardUser("Ananya",50,R.drawable.avtaar))
        userList.add(LeaderboardUser("Suyash",40,R.drawable.avtaar))
        userList.add(LeaderboardUser("Ayush",10,R.drawable.avtaar))
        userList.add(LeaderboardUser("Kunal",1,R.drawable.avtaar))



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_achievement,container,false)

        return binding.root
    }

}