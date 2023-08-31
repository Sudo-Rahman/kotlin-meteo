package com.sr_71.meteo.view.fagments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.sr_71.meteo.R
import com.sr_71.meteo.model.Weather
import com.sr_71.meteo.view.adapters.HourlyWeatherAdapter
import com.sr_71.meteo.view_model.LocationViewModel
import com.sr_71.meteo.view_model.WeatherViewModel


class HourlyWeatherFragment(
    private val _location: LocationViewModel? = null,
    private val _weather: Weather? = null
) : Fragment() {
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
            observeLocation()
            _weatherViewModel.weather(
                longitude = _location?.location?.value?.longitude ?: 0.0,
                latitude = _location?.location?.value?.latitude ?: 0.0
            )
        }
    }

    private fun observeLocation() {
        _location?.location?.observe(viewLifecycleOwner) {
            _weatherViewModel.weather(
                longitude = _location.location.value!!.longitude,
                latitude = _location.location.value!!.latitude
            )
        }
    }

    private fun updateWeather() {
        _weatherViewModel.weather.observe(viewLifecycleOwner) {
            if (_recyclerView.adapter == null) {
                _recyclerView.adapter = HourlyWeatherAdapter(it)
            } else {
                val adapter = _recyclerView.adapter as HourlyWeatherAdapter
                adapter.weather = it
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (_weather != null) return
        _weatherViewModel.weather(
            longitude = _location?.location?.value!!.longitude,
            latitude = _location.location.value!!.latitude
        )
    }
}