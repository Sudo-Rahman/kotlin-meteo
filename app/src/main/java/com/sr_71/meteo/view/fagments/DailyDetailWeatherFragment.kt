package com.sr_71.meteo.view.fagments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.sr_71.meteo.R
import com.sr_71.meteo.databinding.FragmentDailyDetailWeatherBinding
import com.sr_71.meteo.model.Weather
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DailyDetailWeatherFragment : Fragment() {
    private lateinit var _weather : Weather
    private lateinit var _day : String
    private var _position : Int = 0

    private lateinit var _binding : FragmentDailyDetailWeatherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            _weather = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) after(it)!! else before(it)!!
            _day = it.getString("dayString")!!
            _position = it.getInt("position")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDailyDetailWeatherBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val  fragment = HourlyWeatherFragment(_weather = _weather)

        childFragmentManager.beginTransaction()
            .replace(
                R.id.hourlyDetailFragment,
                fragment,
                HourlyWeatherFragment::class.java.simpleName
            )
            .commit()

        _binding.dayString.text = _day

        val  formatter = DateTimeFormatter.ofPattern("HH:mm")
        val sunrise = LocalDateTime.parse(_weather.daily?.sunrise?.get(_position))
        val sunset = LocalDateTime.parse(_weather.daily?.sunset?.get(_position))

        _binding.sunrise.text = formatter.format(sunrise)
        _binding.sunset.text = formatter.format(sunset)

        _binding.uvTxt.text = _weather.daily?.uv_index_max?.get(_position)?.toInt().toString()

        _binding.ventTxt.text = _weather.daily?.windspeed_10m_max?.get(_position)?.toInt().toString() + "km/h"

        _binding.ventDirectionTxt.text = _weather.daily?.winddirection_10m_dominant?.get(_position)?.toInt().toString() + "°"
        _binding.ventImg.rotation = _weather.daily?.winddirection_10m_dominant?.get(_position)?.toFloat() ?: 0.0f

        _binding.maxPrecipitationTxt.text = _weather.daily?.precipitation_probability_max?.get(_position)?.toString() + "%"
    }

    @Suppress("DEPRECATION")
    private fun before(bundle: Bundle) : Weather? { return bundle.getParcelable<Weather>("weather") }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun after(bundle: Bundle) : Weather? { return bundle.getParcelable("weather",Weather::class.java) }
}