package com.sr_71.meteo.view.fagments

import android.location.Location
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sr_71.meteo.R
import com.sr_71.meteo.view.adapters.HourlyWeatherAdapter
import com.sr_71.meteo.view_model.HourlyWeatherViewModel
import com.sr_71.meteo.view_model.LocationViewModel


class HourlyWeatherFragment(private val _location: LocationViewModel) : Fragment() {
    private lateinit var _recyclerView: RecyclerView
    private var _hourlyWeatherViewModel = HourlyWeatherViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hourly_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _recyclerView = view.findViewById(R.id.recycler)
        updateWeather()
        observeLocation()
        _hourlyWeatherViewModel.weather(longitude = _location.location.value?.longitude ?:0.0, latitude = _location.location.value?.latitude ?:0.0)
    }
    private fun observeLocation() {
        _location.location.observe(viewLifecycleOwner) {
            _hourlyWeatherViewModel.weather(longitude = _location.location.value!!.longitude, latitude = _location.location.value!!.latitude)
        }
    }

    private fun updateWeather() {
        _hourlyWeatherViewModel.weather.observe(viewLifecycleOwner) {
            if (_recyclerView.adapter == null) {
                _recyclerView.adapter = HourlyWeatherAdapter(it)
            } else {
                val adapter = _recyclerView.adapter as HourlyWeatherAdapter
                adapter.weather = it
                adapter!!.notifyDataSetChanged()
            }
        }
    }
}