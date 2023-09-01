package com.sr_71.meteo.API

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private val baseUrl = "https://api.open-meteo.com/"

private val retrofit = Retrofit.Builder()
    .baseUrl(baseUrl)
    .addConverterFactory(ScalarsConverterFactory.create())
    .build()

interface WeatherAPI {

    enum class DAYS(val value: String) {
        ONE("1"),
        TEN("10")
    }
    @retrofit2.http.GET("v1/forecast?")
    suspend fun getNowWheater(
        @retrofit2.http.Query("latitude") latitude: String ,
        @retrofit2.http.Query("longitude") longitude: String,
        @retrofit2.http.Query("hourly") hourly: String = "temperature_2m,weathercode,precipitation_probability,is_day",
        @retrofit2.http.Query("timezone") timezone: String = "auto",
        @retrofit2.http.Query("forecast_days") forecast_days: String = "2",

        @retrofit2.http.Query("daily") daily: String = "sunrise,sunset",

        @retrofit2.http.Query("models") models: String = "best_match",

    ): String

    @retrofit2.http.GET("v1/forecast?")
    suspend fun getWheaterTenDays(
        @retrofit2.http.Query("latitude") latitude: String,
        @retrofit2.http.Query("longitude") longitude: String,
        @retrofit2.http.Query("daily") daily: String = "weathercode,temperature_2m_max,temperature_2m_min,sunrise,sunset,uv_index_max,precipitation_probability_max,windspeed_10m_max,winddirection_10m_dominant",
        @retrofit2.http.Query("timezone") timezone: String = "auto",
        @retrofit2.http.Query("forecast_days") forecast_days: String = "10",
        @retrofit2.http.Query("models") models: String = "best_match",
        @retrofit2.http.Query("hourly") hourly: String = "temperature_2m,weathercode,precipitation_probability,is_day",

        ): String
}

object WeatherApiManager {
    val retrofitService: WeatherAPI by lazy {
        retrofit.create(WeatherAPI::class.java)
    }
}