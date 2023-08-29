package com.sr_71.meteo.API

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private val baseUrl = "https://api.geoapify.com/"

private val retrofit = Retrofit.Builder()
    .baseUrl(baseUrl)
    .addConverterFactory(ScalarsConverterFactory.create())
    .build()

interface GeoapifyAPI {

    @retrofit2.http.GET("v1/geocode/autocomplete?")
    suspend fun getCity(
        @retrofit2.http.Query("text") name: String,
        @retrofit2.http.Query("apiKey") limit: String = "1d1435fb7d6a434183e932c107ad5a03",
    ): String
}

object GeoapifyApiManager {
    val retrofitService: GeoapifyAPI by lazy {
        retrofit.create(GeoapifyAPI::class.java)
    }
}