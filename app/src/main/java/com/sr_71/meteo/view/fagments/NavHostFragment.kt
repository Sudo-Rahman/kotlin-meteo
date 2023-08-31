package com.sr_71.meteo.view.fagments

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import com.sr_71.meteo.R
import com.sr_71.meteo.databinding.FragmentNavHostBinding
import com.sr_71.meteo.view_model.LocationViewModel
import java.util.Locale

class NavHostFragment : Fragment() {
    private var locationManager: LocationManager? = null
    private var _location = LocationViewModel()

    private lateinit var _hourlyWeatherFragment: HourlyWeatherFragment
    private lateinit var _dailyWeatherFragment: DailyWeatherFragment

    private lateinit var _binding: FragmentNavHostBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentNavHostBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        _hourlyWeatherFragment = HourlyWeatherFragment(_location = _location)
        childFragmentManager.beginTransaction()
            .replace(
                R.id.hourlyWeatherFragment,
                _hourlyWeatherFragment,
                HourlyWeatherFragment::class.java.simpleName
            )
            .commit()

        _dailyWeatherFragment = DailyWeatherFragment(_location)
        childFragmentManager.beginTransaction()
            .replace(
                R.id.dailyWeatherFragment,
                _dailyWeatherFragment,
                DailyWeatherFragment::class.java.simpleName
            )
            .commit()


        val sharedPref = activity?.getSharedPreferences("weather", AppCompatActivity.MODE_PRIVATE)
        sharedPref?.run {
            val city = getString("city", "Paris")
            val longitude = getString("longitude", "0.0")!!.toDouble()
            val latitude = getString("latitude", "0.0")!!.toDouble()
            _location.setLocation(Location("").apply {
                this.longitude = longitude
                this.latitude = latitude
            })
            _binding.txt.text = city
        }
        getLocation()
    }

    private fun getLocation() {
        locationManager = getSystemService(requireContext(), LocationManager::class.java)


        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1
            )
            return
        } else {

            locationManager?.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0L,
                0f,
                _locationListener
            )
        }
    }

    private val _locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            _location.setLocation(location)
            val city = getCityName(location.latitude, location.longitude)
            _binding.txt.text = city

            requireContext().getSharedPreferences("weather", AppCompatActivity.MODE_PRIVATE)?.edit {
                putString("city", city)
                putString("longitude", location.longitude.toString())
                putString("latitude", location.latitude.toString())
            }
        }

        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    private fun getCityName(lat: Double, long: Double): String {
        val geoCoder = context?.let { Geocoder(it, Locale.getDefault()) }
        val address = geoCoder?.getFromLocation(lat, long, 1)
        return address?.get(0)?.locality ?: address?.get(0)?.adminArea ?: "Unknown"
    }

}