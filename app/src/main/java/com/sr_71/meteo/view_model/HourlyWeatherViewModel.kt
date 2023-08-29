package com.sr_71.meteo.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sr_71.meteo.API.WeatherAPI
import com.sr_71.meteo.API.WeatherApiManager
import com.sr_71.meteo.model.Weather
import kotlinx.coroutines.launch

class HourlyWeatherViewModel : ViewModel() {
    private val _weather = MutableLiveData<Weather>()
    val weather: LiveData<Weather>
        get() = _weather

    fun weather(
        longitude: Double,
        latitude: Double,
        weather: WeatherAPI.DAYS = WeatherAPI.DAYS.ONE
    ) {
        viewModelScope.launch {
            if (weather == WeatherAPI.DAYS.ONE) {
                WeatherApiManager.retrofitService.getNowWheater(
                    longitude = longitude.toString(),
                    latitude = latitude.toString()
                ).let {
                    val gson = com.google.gson.Gson()
                    val weather = gson.fromJson(it, Weather::class.java)
                    _weather.value = weather
                }
            } else
                WeatherApiManager.retrofitService.getWheaterTenDays(
                    longitude = longitude.toString(),
                    latitude = latitude.toString()
                ).let {
                    val gson = com.google.gson.Gson()
                    val weather = gson.fromJson(it, Weather::class.java)
                    _weather.value = weather
                }
        }
    }
}