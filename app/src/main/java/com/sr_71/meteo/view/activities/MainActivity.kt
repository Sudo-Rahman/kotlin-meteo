package com.sr_71.meteo.view.activities

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sr_71.meteo.R


class MainActivity : AppCompatActivity() {


    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController

        isDay.observe(this){
//            println("isDay: $it")
            val color = if (it) getDrawable(R.drawable.gradient_main_page_day) else getDrawable(R.drawable.gradient_main_page_night)
            findViewById<FrameLayout>(R.id.background).background = color

            // change android status bar color
            window.statusBarColor = if (it) Color.parseColor("#90CAF9") else Color.parseColor("#41464A")
        }
    }

    companion object {
        val isDay = MutableLiveData(true)
        val isRefresh = MutableLiveData(false)
    }

    override fun onResume() {
        super.onResume()
        isRefresh.value = true
    }
}

