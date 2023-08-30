package com.sr_71.meteo.model

import com.sr_71.meteo.R
import androidx.annotation.DrawableRes


data class Weather(
    val timezone: String,
    val utc_offset_seconds: Int,

    // for daily weather
    val daily: WeatherDaily? = null,

    // for hourly weather
    val hourly: WeatherHourly? = null,
)

data class WeatherDaily(
    val time: List<String>,
    val precipitation_probability_max: List<Int>? = null,
    val temperature_2m_max: List<Double>? = null,
    val temperature_2m_min: List<Double>? = null,
    val weathercode: List<Int>? = null,
    val sunrise: List<String>? = null,
    val sunset: List<String>? = null,
)

data class WeatherHourly(
    val time: List<String>?,
    val precipitation_probability: List<Int>?,
    val temperature_2m: List<Double>?,
    val weathercode: List<Int>,
)


enum class WeatherCode(val code: Int) {
    CLEAR_SKY(0),

    MAINLY_CLEAR_SKY(1),
    PARTLY_CLOUDY(2),
    OVERCAST(3),

    FOG(45),
    DEPOSITED_RIME_FOG(48),

    DRIZZLE_LIGHT(51),
    DRIZZLE_MODERATE(53),
    DRIZZLE_HEAVY(55),

    FREEZING_DRIZZLE_LIGHT(56),
    FREEZING_DRIZZLE_MODERATE_OR_HEAVY(57),

    RAIN_LIGHT(61),
    RAIN_MODERATE(63),
    RAIN_HEAVY(65),

    FREEZING_RAIN_LIGHT(66),
    FREEZING_RAIN_MODERATE_OR_HEAVY(67),

    SNOW_LIGHT(71),
    SNOW_MODERATE(73),
    SNOW_HEAVY(75),

    SNOW_GRAINS(77),

    RAIN_SHOWER_LIGHT(80),
    RAIN_SHOWER_MODERATE_OR_HEAVY(81),
    RAIN_SHOWER_VIOLENT(82),

    SNOW_SHOWER_LIGHT(85),
    SNOW_SHOWER_MODERATE_OR_HEAVY(86),

    THUNDERSTORM_LIGHT_OR_MODERATE_WITHOUT_HAIL(95),
    THUNDERSTORM_LIGHT_OR_MODERATE_WITH_HAIL(96),
    THUNDERSTORM_HEAVY_WITH_HAIL(99);

    companion object {
        infix fun from(value: Int): WeatherCode? =
            WeatherCode.values().firstOrNull() { it.code == value }
    }
}


// link local svg to weather code
data class WeatherImg(
    @DrawableRes val day: Int,
    @DrawableRes val night: Int? = null
)

// map weather code to weather img
val weatherCodeToImg = mapOf<WeatherCode, WeatherImg>(
    WeatherCode.CLEAR_SKY to WeatherImg(
        R.drawable.day_clear,
        R.drawable.night_half_moon_clear,
    ),
    WeatherCode.MAINLY_CLEAR_SKY to WeatherImg(
        R.drawable.day_clear,
        R.drawable.night_half_moon_clear,
    ),
    WeatherCode.PARTLY_CLOUDY to WeatherImg(
        R.drawable.day_partial_cloud,
        R.drawable.night_half_moon_partial_cloud,
    ),
    WeatherCode.OVERCAST to WeatherImg(
        R.drawable.overcast,
    ),
    WeatherCode.FOG to WeatherImg(
        R.drawable.fog,
    ),
    WeatherCode.DEPOSITED_RIME_FOG to WeatherImg(
        R.drawable.fog,
    ),
    WeatherCode.DRIZZLE_LIGHT to WeatherImg(
        R.drawable.day_rain,
        R.drawable.night_half_moon_rain,
    ),
    WeatherCode.DRIZZLE_MODERATE to WeatherImg(
        R.drawable.day_rain,
        R.drawable.night_half_moon_rain,
    ),
    WeatherCode.DRIZZLE_HEAVY to WeatherImg(
        R.drawable.rain,
    ),
    WeatherCode.FREEZING_DRIZZLE_LIGHT to WeatherImg(
        R.drawable.day_rain,
        R.drawable.night_half_moon_rain,
    ),
    WeatherCode.FREEZING_DRIZZLE_MODERATE_OR_HEAVY to WeatherImg(
        R.drawable.rain,
    ),
    WeatherCode.RAIN_LIGHT to WeatherImg(
        R.drawable.day_rain,
        R.drawable.night_half_moon_rain,
    ),
    WeatherCode.RAIN_MODERATE to WeatherImg(
        R.drawable.day_rain,
        R.drawable.night_half_moon_rain,
    ),
    WeatherCode.RAIN_HEAVY to WeatherImg(
        R.drawable.rain,
    ),
    WeatherCode.FREEZING_RAIN_LIGHT to WeatherImg(
        R.drawable.day_rain,
        R.drawable.night_half_moon_rain,
    ),
    WeatherCode.FREEZING_RAIN_MODERATE_OR_HEAVY to WeatherImg(
        R.drawable.rain
    ),
    WeatherCode.SNOW_LIGHT to WeatherImg(
        R.drawable.day_snow,
        R.drawable.night_half_moon_snow,
    ),
    WeatherCode.SNOW_MODERATE to WeatherImg(
        R.drawable.day_snow,
        R.drawable.night_half_moon_snow,
    ),
    WeatherCode.SNOW_HEAVY to WeatherImg(
        R.drawable.snow,
    ),
    WeatherCode.SNOW_GRAINS to WeatherImg(
        R.drawable.snow,
    ),
    WeatherCode.RAIN_SHOWER_LIGHT to WeatherImg(
        R.drawable.day_rain,
        R.drawable.night_half_moon_rain,
    ),
    WeatherCode.RAIN_SHOWER_MODERATE_OR_HEAVY to WeatherImg(
        R.drawable.day_rain,
        R.drawable.night_half_moon_rain,
    ),
    WeatherCode.RAIN_SHOWER_VIOLENT to WeatherImg(
        R.drawable.rain,
    ),
    WeatherCode.SNOW_SHOWER_LIGHT to WeatherImg(
        R.drawable.day_snow,
        R.drawable.night_half_moon_snow,
    ),
    WeatherCode.SNOW_SHOWER_MODERATE_OR_HEAVY to WeatherImg(
        R.drawable.snow,
        R.drawable.snow,
    ),
    WeatherCode.THUNDERSTORM_LIGHT_OR_MODERATE_WITHOUT_HAIL to WeatherImg(
        R.drawable.thunder,
    ),
    WeatherCode.THUNDERSTORM_LIGHT_OR_MODERATE_WITH_HAIL to WeatherImg(
        R.drawable.day_rain_thunder,
        R.drawable.night_half_moon_rain_thunder,
    ),
    WeatherCode.THUNDERSTORM_HEAVY_WITH_HAIL to WeatherImg(
        R.drawable.rain_thunder,
    ),
)

