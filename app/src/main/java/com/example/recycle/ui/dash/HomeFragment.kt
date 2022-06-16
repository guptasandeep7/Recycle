package com.example.recycle.ui.dash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.recycle.R
import com.example.recycle.adapter.AdapterGoals
import com.example.recycle.databinding.FragmentHomeBinding
import com.example.recycle.model.GoalsModel
import com.example.recycle.model.LeaderboardUser


class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    private var layoutManager: RecyclerView.LayoutManager?=null
    lateinit var adapter: AdapterGoals
     var goalsList = ArrayList<GoalsModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       goalsList.add(GoalsModel("All Clean","We usually use a non-sustainable product for cleaning the house. Try making this: Half cup of white vinegar, 2 table spoons of baking soda and essential oils. Mix them and you haave a sustainable cleaning product!",R.drawable.ic_leafgoals))
        goalsList.add(GoalsModel("All Clean","We usually use a non-sustainable product for cleaning the house. Try making this: Half cup of white vinegar, 2 table spoons of baking soda and essential oils. Mix them and you haave a sustainable cleaning product!",R.drawable.ic_leafgoals))
        goalsList.add(GoalsModel("All Clean","We usually use a non-sustainable product for cleaning the house. Try making this: Half cup of white vinegar, 2 table spoons of baking soda and essential oils. Mix them and you haave a sustainable cleaning product!",R.drawable.ic_leafgoals))
        goalsList.add(GoalsModel("All Clean","We usually use a non-sustainable product for cleaning the house. Try making this: Half cup of white vinegar, 2 table spoons of baking soda and essential oils. Mix them and you haave a sustainable cleaning product!",R.drawable.ic_leafgoals))
        goalsList.add(GoalsModel("All Clean","We usually use a non-sustainable product for cleaning the house. Try making this: Half cup of white vinegar, 2 table spoons of baking soda and essential oils. Mix them and you haave a sustainable cleaning product!",R.drawable.ic_leafgoals))

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_home, container, false)
        val imageList = ArrayList<SlideModel>() // Create image list
        imageList.add(SlideModel(R.drawable.slider,ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.slider4,ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.slider_three,ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.sliderone,ScaleTypes.FIT))
        adapter= AdapterGoals(goalsList)
        binding.HomePageSlider.setImageList(imageList)
        layoutManager=LinearLayoutManager(container?.context)
        binding.RecyclerViewHomePage.layoutManager=layoutManager
        binding.RecyclerViewHomePage.adapter=adapter
        adapter.onClickListeer(object : AdapterGoals.ClickListener {
            override fun OnClick(position: Int) {

            }
        })
        return binding.root
    }

}