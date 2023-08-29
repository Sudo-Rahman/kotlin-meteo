package com.sr_71.meteo.view.adapters

import android.view.LayoutInflater
import com.sr_71.meteo.R
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sr_71.meteo.model.Weather
import com.sr_71.meteo.model.WeatherCode
import com.sr_71.meteo.model.weatherCodeToImg
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

class HourlyWeatherAdapter(var weather: Weather) : RecyclerView.Adapter<HourlyWeatherViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyWeatherViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.hourly_item_layout, parent, false)
        return HourlyWeatherViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 24
    }

    override fun onBindViewHolder(holder: HourlyWeatherViewHolder, position: Int) {
        // get local hour
        val current_hour = LocalDateTime.now().hour
        val pos_local = position + current_hour

        // get the current hour to the timezone of the country
        val current_hour_in_country = LocalDateTime.now().atOffset(ZoneOffset.UTC)
            .atZoneSameInstant(ZoneId.of(weather.timezone)).hour
        val pos_in_country = position + current_hour_in_country


        holder.time.text = getHourString(pos_local)
        holder.temperature.text = "${weather.hourly?.temperature_2m?.get(pos_in_country)}°"
        holder.precipitation.text =
            "${weather.hourly?.precipitation_probability?.get(pos_in_country)}%"

        val weatherCode = WeatherCode.from(weather.hourly?.weathercode?.get(pos_in_country) ?: 0)
        if (isDay(pos_in_country)) {
            holder.weatherIcon.setImageResource(weatherCodeToImg[weatherCode]!!.day)
        } else
            holder.weatherIcon.setImageResource(
                weatherCodeToImg[weatherCode]?.night ?: weatherCodeToImg[weatherCode]!!.day
            )
    }

    private fun getHourString(pos: Int): String {
        val hour = LocalDateTime.now().hour
        return if (getHour(pos) == hour) "Now" else "${getHour(pos)}h"
    }

    private fun getHour(pos: Int): Int {
        return if (pos >= 24) {
            pos - 24
        } else {
            pos
        }
    }

    private fun isDay(pos: Int): Boolean {
        val sunrise = LocalDateTime.parse(weather.daily?.sunrise?.get(0)).atOffset(ZoneOffset.UTC)
            .atZoneSameInstant(ZoneId.of(weather.timezone)).hour
        val sunset = LocalDateTime.parse(weather.daily?.sunset?.get(0)).atOffset(ZoneOffset.UTC)
            .atZoneSameInstant(ZoneId.of(weather.timezone)).hour
        println("sunrise: $sunrise sunset: $sunset  hour: ${getHour(pos)}")
        return getHour(pos) >= sunrise && getHour(pos) <= sunset
    }

}


class HourlyWeatherViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    var time: TextView = view.findViewById(R.id.date)
    var temperature: TextView = view.findViewById(R.id.temperature)
    var weatherIcon: ImageView = view.findViewById(R.id.weatherIcon)
    var precipitation: TextView = view.findViewById(R.id.precipitation)

}

