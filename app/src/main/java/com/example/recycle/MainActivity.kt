package com.example.recycle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.recycle.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNavigationView.itemIconTintList=null
        binding.bottomNavigationView.background=null
    }
}