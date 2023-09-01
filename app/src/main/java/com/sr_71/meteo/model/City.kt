package com.sr_71.meteo.model


data class Citys(
    val features: List<Properties>? = null
)

data class Properties(
    val properties: Propertie
)

data class Propertie(
    val city: String,
    val country: String,
    val lon : Double,
    val lat : Double,
    val formatted: String
)

