package com.sr_71.meteo.view.fagments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.sr_71.meteo.API.WeatherAPI
import com.sr_71.meteo.R
import com.sr_71.meteo.model.Weather
import com.sr_71.meteo.view.adapters.HourlyWeatherAdapter
import com.sr_71.meteo.view_model.WeatherViewModel


class HourlyWeatherFragment(private val _weather: Weather? = null) : Fragment() {
    private lateinit var _recyclerView: RecyclerView
    private var _weatherViewModel = WeatherViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hourly_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _recyclerView = view.findViewById(R.id.hourlyRecycler)

        if (_weather != null) {
            _recyclerView.adapter = HourlyWeatherAdapter(_weather, false)
            _recyclerView.adapter?.notifyDataSetChanged()
        } else {
            updateWeather()
            _weatherViewModel.weather(
                longitude = NavHostFragment.locationGps.value?.longitude ?: 0.0,
                latitude = NavHostFragment.locationGps.value?.latitude ?: 0.0
            )
        }
    }

    private fun updateWeather() {
        _weatherViewModel.weather.observe(viewLifecycleOwner) {
            NavHostFragment.elevetion.value = it?.elevation
            if (_recyclerView.adapter == null) {
                _recyclerView.adapter = HourlyWeatherAdapter(it)
            } else {
                val adapter = _recyclerView.adapter as HourlyWeatherAdapter
                adapter.weather = it
                adapter.notifyDataSetChanged()
            }
        }
    }

    fun refresh() {
        if (_weather != null) return
        NavHostFragment.locationGps.value?.let {
            _weatherViewModel.weather(
                longitude = it.longitude,
                latitude = it.latitude,
                weather = WeatherAPI.DAYS.TEN
            )
        }
    }
}