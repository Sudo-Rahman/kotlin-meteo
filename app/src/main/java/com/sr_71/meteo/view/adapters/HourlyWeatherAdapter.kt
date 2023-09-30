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
import com.sr_71.meteo.view.activities.MainActivity
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.math.roundToInt

class HourlyWeatherAdapter(var weather: Weather, private val _currentHour: Boolean = true) :
    RecyclerView.Adapter<HourlyWeatherViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyWeatherViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.hourly_item_layout, parent, false)
        return HourlyWeatherViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 24
    }

    override fun onBindViewHolder(holder: HourlyWeatherViewHolder, position: Int) {
        val pos: Int
        pos = if (_currentHour) {
            // get UTC dateTime
            val utc_date_time = LocalDateTime.now(ZoneId.of("UTC"))

            //get offset of the country
            val offset = weather.utc_offset_seconds / 3600

            //get date time on utc + offset
            val date_in_country = utc_date_time.plusHours(offset.toLong())

            (date_in_country.hour + position) % 24
        } else {
            position % 24
        }


        holder.time.text = "${pos}h"
        holder.temperature.text = "${weather.hourly?.temperature_2m?.get(pos)?.roundToInt()}°"

        if ((weather.hourly?.precipitation_probability?.get(pos) ?: 0) > 5)
            holder.precipitation.text =
                "${weather.hourly?.precipitation_probability?.get(position)}%"

        val weatherCode = WeatherCode.from(weather.hourly?.weathercode?.get(pos) ?: 0)
        if (weather.hourly?.is_day?.get(pos) == 1)
            holder.weatherIcon.setImageResource(weatherCodeToImg[weatherCode]!!.day)
        else
            holder.weatherIcon.setImageResource(
                weatherCodeToImg[weatherCode]?.night ?: weatherCodeToImg[weatherCode]!!.day
            )
    }
}


class HourlyWeatherViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    var time: TextView = view.findViewById(R.id.date)
    var temperature: TextView = view.findViewById(R.id.temperature)
    var weatherIcon: ImageView = view.findViewById(R.id.weatherIcon)
    var precipitation: TextView = view.findViewById(R.id.precipitation)
}

