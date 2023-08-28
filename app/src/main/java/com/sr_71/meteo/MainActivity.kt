package com.sr_71.meteo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sr_71.meteo.API.*
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // lauch couroutine
        lifecycleScope.launch {
            val weather = WeatherApiManager.retrofitService.getWheaterTenDays()
            println(weather)
        }
    }
}