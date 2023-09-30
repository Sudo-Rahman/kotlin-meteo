package com.sr_71.meteo.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sr_71.meteo.API.WeatherAPI
import com.sr_71.meteo.API.WeatherApiManager
import com.sr_71.meteo.model.Weather
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId

class HomeViewModel : ViewModel() {
    private val _weatherDaily = MutableLiveData<Weather>()
    val weatherDaily: LiveData<Weather>
        get() = _weatherDaily

    private val _weatherHourly = MutableLiveData<Weather>()
    val weatherHourly: LiveData<Weather>
        get() = _weatherHourly

    private val _location = MutableLiveData<Pair<Double, Double>>()

    private val _elevetion = MutableLiveData<Int>()

    val elevetion: LiveData<Int>
        get() = _elevetion

    fun setElevetion(elevetion: Int) {
        _elevetion.value = elevetion
    }

    val locationGps: LiveData<Pair<Double, Double>>
        get() = _location

    fun setLocation(latitude: Double, longitude: Double) {
        _location.value = Pair(latitude, longitude)
    }

    fun setLocation(Pair: Pair<Double, Double>) {
        _location.value = Pair
    }

    private val _isDay = MutableLiveData<Boolean>()
    val isDay: LiveData<Boolean>
        get() = _isDay

    fun setDay(isDay: Boolean) {
        _isDay.value = isDay
    }

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
                    _weatherHourly.value = weather
                    _isDay.value = weather.hourly?.is_day?.get(getTime()) == 1
                }
            } else
                WeatherApiManager.retrofitService.getWheaterTenDays(
                    longitude = longitude.toString(),
                    latitude = latitude.toString()
                ).let {
                    val gson = com.google.gson.Gson()
                    val weather = gson.fromJson(it, Weather::class.java)
                    _weatherDaily.value = weather
                }
        }
    }

    private fun getTime(): Int {
        // get UTC dateTime
        val utc_date_time = LocalDateTime.now(ZoneId.of("UTC"))

        //get offset of the country
        val offset = _weatherHourly.value?.utc_offset_seconds?.div(3600)

        //get date time on utc + offset
        val date_in_country = offset?.let { utc_date_time.plusHours(it.toLong()) }

        return date_in_country?.hour ?: 0
    }
}