package com.sr_71.meteo.API

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private val baseUrl = "https://api.open-meteo.com/"

private val retrofit = Retrofit.Builder()
    .baseUrl(baseUrl)
    .addConverterFactory(ScalarsConverterFactory.create())
    .build()

interface WeatherAPI {
    @retrofit2.http.GET("v1/forecast?")
    suspend fun getNowWheater(
        @retrofit2.http.Query("latitude") latitude: String ,
        @retrofit2.http.Query("longitude") longitude: String,
        @retrofit2.http.Query("hourly") hourly: String = "temperature_2m,weathercode,precipitation_probability",
        @retrofit2.http.Query("timezone") timezone: String = "auto",
        @retrofit2.http.Query("forecast_days") forecast_days: String = "1",

    ): String

    @retrofit2.http.GET("v1/forecast?")
    suspend fun getWheaterTenDays(
        @retrofit2.http.Query("latitude") latitude: String = "46.795166",
        @retrofit2.http.Query("longitude") longitude: String = "4.80562",
        @retrofit2.http.Query("daily") daily: String = "temperature_2m_max,temperature_2m_min,weathercode,precipitation_probability_max",
        @retrofit2.http.Query("timezone") timezone: String = "auto",
        @retrofit2.http.Query("forecast_days") forecast_days: String = "10",

    ): String
}

object WeatherApiManager {
    val retrofitService: WeatherAPI by lazy {
        retrofit.create(WeatherAPI::class.java)
    }
}