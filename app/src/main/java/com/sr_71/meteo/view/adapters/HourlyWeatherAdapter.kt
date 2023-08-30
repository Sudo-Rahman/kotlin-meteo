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
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import kotlin.math.roundToInt

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
        // get UTC + 0 hour
        val utc_hour = LocalDateTime.now(ZoneId.of("UTC")).hour
//        println("utc_hour $utc_hour")

        //get offset of the country
        val offset = weather.utc_offset_seconds/3600

        //get time on local machine
        val local_hour = LocalTime.now().hour
        val local_pos = position + local_hour

        //get current hour in country
        val current_hour_in_country = utc_hour + offset
        val pos_in_country = position + current_hour_in_country


        holder.time.text = "${getHour(local_pos)}h"
        holder.temperature.text = "${weather.hourly?.temperature_2m?.get(pos_in_country)?.roundToInt()}°"

        if ((weather.hourly?.precipitation_probability?.get(pos_in_country)?:0 )>5)
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

    private fun getHour(pos: Int): Int {
        return pos % 24
    }

    private fun isDay(pos: Int): Boolean {
        val  offset = weather.utc_offset_seconds/3600
        val sunrise = LocalDateTime.parse(weather.daily?.sunrise?.get(0)).atOffset(ZoneOffset.UTC)
            .atZoneSameInstant(ZoneId.of(weather.timezone)).hour - offset
        val sunset = LocalDateTime.parse(weather.daily?.sunset?.get(0)).atOffset(ZoneOffset.UTC)
            .atZoneSameInstant(ZoneId.of(weather.timezone)).hour - offset
        println("sunrise $sunrise sunset $sunset")
        return getHour(pos) > sunrise  && getHour(pos) <= sunset
    }

}


class HourlyWeatherViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    var time: TextView = view.findViewById(R.id.date)
    var temperature: TextView = view.findViewById(R.id.temperature)
    var weatherIcon: ImageView = view.findViewById(R.id.weatherIcon)
    var precipitation: TextView = view.findViewById(R.id.precipitation)

}

