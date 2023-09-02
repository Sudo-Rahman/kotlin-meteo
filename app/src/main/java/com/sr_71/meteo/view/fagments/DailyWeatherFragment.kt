package com.sr_71.meteo.view.fagments

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.sr_71.meteo.API.WeatherAPI
import com.sr_71.meteo.R
import com.sr_71.meteo.view.adapters.DailyWeatherAdapter
import com.sr_71.meteo.view_model.WeatherViewModel


class DailyWeatherFragment() : Fragment() {
    private var _weatherViewModel = WeatherViewModel()
    private lateinit var _recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_daily_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _recyclerView = view.findViewById(R.id.dailyRecycler)
        val dividerItemDecoration = DividerItemDecoration(
            _recyclerView.context,
            DividerItemDecoration.VERTICAL
        )
        dividerItemDecoration.setDrawable(
            ColorDrawable(
                resources.getColor(
                    androidx.transition.R.color.material_grey_300,
                    null
                )
            )
        )
        _recyclerView.addItemDecoration(dividerItemDecoration)
        updateWeather()
        NavHostFragment.locationGps.observe(viewLifecycleOwner) {
            refresh()
        }
    }

    private fun updateWeather() {
        _weatherViewModel.weather.observe(viewLifecycleOwner) {
            if (_recyclerView.adapter == null) {
                _recyclerView.adapter = DailyWeatherAdapter(it)
            } else {
                val adapter = _recyclerView.adapter as DailyWeatherAdapter
                adapter.weather = it
                adapter.notifyDataSetChanged()
            }
        }
    }

    private  fun refresh() {
        NavHostFragment.locationGps.value?.let {
            _weatherViewModel.weather(
                longitude = it.longitude,
                latitude = it.latitude,
                weather = WeatherAPI.DAYS.TEN
            )
        }
    }

}