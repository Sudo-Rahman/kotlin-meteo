package com.sr_71.meteo.view.fagments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.sr_71.meteo.API.WeatherAPI
import com.sr_71.meteo.R
import com.sr_71.meteo.model.Weather
import com.sr_71.meteo.view.adapters.HourlyWeatherAdapter
import com.sr_71.meteo.view_model.HomeViewModel


class HourlyWeatherFragment(private val _weather: Weather? = null) : Fragment() {
    private lateinit var _recyclerView: RecyclerView
    val _viewModel: HomeViewModel by activityViewModels()

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
            _viewModel.let {
                _viewModel.weather(
                    latitude = it.locationGps.value?.first ?: 0.0,
                    longitude = it.locationGps.value?.second ?: 0.0,
                )
            }
            _viewModel.setDay(true)
        }
        _viewModel.locationGps.observe(viewLifecycleOwner) {
            refresh()
        }
    }

     private fun updateWeather() {
         _viewModel.weatherHourly.observe(viewLifecycleOwner) {
            _viewModel.setElevetion(it.elevation)
            if (_recyclerView.adapter == null) {
                _recyclerView.adapter = HourlyWeatherAdapter(it)
            } else {
                val adapter = _recyclerView.adapter as HourlyWeatherAdapter
                adapter.weather = it
                adapter.notifyDataSetChanged()
            }
        }
    }

     private fun refresh() {
        if (_weather != null) return
        _viewModel.locationGps.value?.let {
            _viewModel.weather(
                latitude = it.first,
                longitude = it.second,
            )
        }
    }
}