package com.sr_71.meteo.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sr_71.meteo.R
import com.sr_71.meteo.model.Weather
import com.sr_71.meteo.model.WeatherCode
import com.sr_71.meteo.model.weatherCodeToImg
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.math.roundToInt


class DailyWeatherAdapter(var weather: Weather) : RecyclerView.Adapter<DailyWeatherViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyWeatherViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.daily_item_layout, parent, false)
        return DailyWeatherViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: DailyWeatherViewHolder, position: Int) {
        if (position == 0) {
            holder.day.text = "Aujourd'hui"
        } else {
            if (position == 1) holder.day.text = "Demain"
            else {
                // get day string in system language
                val sdf = SimpleDateFormat("EEEE")
                val d = Date().time + (position * 24 * 60 * 60 * 1000)
                val dayOfTheWeek: String = sdf.format(d).replaceFirstChar { it.uppercase() }
                holder.day.text = dayOfTheWeek
            }
        }
        val weatherCode = WeatherCode.from(weather.daily?.weathercode?.get(position) ?: 0)
        holder.weatherIcon.setImageResource(weatherCodeToImg[weatherCode]!!.day)
        val max_precipitation = weather.daily?.precipitation_probability_max?.get(position)
        if (max_precipitation != null && max_precipitation > 20)
            holder.precipitation.text =
                "${weather.daily?.precipitation_probability_max?.get(position)}%"

        holder.minTemperature.text =
            "${weather.daily?.temperature_2m_min?.get(position)?.roundToInt()}°"
        holder.maxTemperature.text =
            "${weather.daily?.temperature_2m_max?.get(position)?.roundToInt()}°"
    }

}

class DailyWeatherViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val day = view.findViewById<TextView>(R.id.day)
    val weatherIcon = view.findViewById<ImageView>(R.id.dailyWeatherIcon)
    val precipitation = view.findViewById<TextView>(R.id.dailyWeatherPrecipitation)
    val minTemperature = view.findViewById<TextView>(R.id.minTemperature)
    val maxTemperature = view.findViewById<TextView>(R.id.maxTemperature)

}