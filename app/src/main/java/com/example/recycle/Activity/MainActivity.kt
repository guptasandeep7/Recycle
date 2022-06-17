package com.example.recycle.Activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.recycle.R
import com.example.recycle.Ui.dash.ObjectDetection
import com.example.recycle.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNavigationView.itemIconTintList = null
        binding.bottomNavigationView.background = null

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_dash_container) as NavHostFragment
        val navController: NavController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
        binding.floatingButtonTeachersSide.setOnClickListener {
            var intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }
    }
}