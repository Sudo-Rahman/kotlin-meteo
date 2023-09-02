package com.sr_71.meteo.view.fagments

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
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
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sr_71.meteo.R
import com.sr_71.meteo.databinding.FragmentNavHostBinding
import com.sr_71.meteo.view.activities.MainActivity
import java.util.Locale


class NavHostFragment : Fragment() {
    private var locationManager: LocationManager? = null

    private lateinit var _hourlyWeatherFragment: HourlyWeatherFragment
    private lateinit var _dailyWeatherFragment: DailyWeatherFragment

    private lateinit var _binding: FragmentNavHostBinding

    private var isLaunched = false

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

        _binding.gpsButton.setOnClickListener { getLocation() }

        _binding.txt.setOnClickListener {
            val action =
                NavHostFragmentDirections.actionNavHostFragmentToSearchCityFragment()
            findNavController().navigate(action)
        }

        _hourlyWeatherFragment = HourlyWeatherFragment()
        childFragmentManager.beginTransaction()
            .replace(
                R.id.hourlyWeatherFragment,
                _hourlyWeatherFragment,
                HourlyWeatherFragment::class.java.simpleName
            )
            .commit()

        _dailyWeatherFragment = DailyWeatherFragment()
        childFragmentManager.beginTransaction()
            .replace(
                R.id.dailyWeatherFragment,
                _dailyWeatherFragment,
                DailyWeatherFragment::class.java.simpleName
            )
            .commit()

        locationGps.observe(viewLifecycleOwner) {
            _binding.txt.text = getCityName(long = it.longitude, lat = it.latitude)
        }


        val sharedPref = activity?.getSharedPreferences("weather", AppCompatActivity.MODE_PRIVATE)
        sharedPref?.run {
            val city = getString("city", "Paris")
            val longitude = getString("longitude", "0.0")!!.toDouble()
            val latitude = getString("latitude", "0.0")!!.toDouble()
            val loc = LocationGps
            loc.longitude = longitude
            loc.latitude = latitude
            locationGps.value = loc
            _binding.txt.text = city
        }

        elevetion.observe(viewLifecycleOwner) {
            _binding.elevationTxt.text = "${it}m"
        }

        locationCity.observe(viewLifecycleOwner) {
            val loc = LocationGps
            loc.longitude = it.first
            loc.latitude = it.second
            saveDate(getCityName(loc.latitude, loc.longitude), loc.longitude, loc.latitude)
            locationGps.value = loc
        }

        if (!isLaunched) {
            isLaunched = true;getLocation()
        }

        MainActivity.isDay.observe(viewLifecycleOwner) {
            // color day : #2196F3 night : #FF445667
            _binding.gpsButton.backgroundTintList = ColorStateList.valueOf(if (it) Color.parseColor("#2196F3") else Color.parseColor("#FF445667"))
        }
    }

    private fun getLocation() {
        locationManager = getSystemService(requireContext(), LocationManager::class.java)

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
             ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                1
            )
            return
        }else {
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
            val loc = LocationGps
            loc.longitude = location.longitude
            loc.latitude = location.latitude
            locationGps.value = loc
            val city = getCityName(location.latitude, location.longitude)
            _binding.txt.text = city

            saveDate(city, location.longitude, location.latitude)
            locationManager?.removeUpdates(this)
            locationManager = null
        }

        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    private fun saveDate(city : String, longitude : Double, latitude : Double) {
        requireContext().getSharedPreferences("weather", AppCompatActivity.MODE_PRIVATE)?.edit {
            putString("city", city)
            putString("longitude", longitude.toString())
            putString("latitude", latitude.toString())
        }
    }

    private fun getCityName(lat: Double, long: Double): String {
        var name = "Unknown"
        val geoCoder = context?.let { Geocoder(it, Locale.getDefault()) }
        val address = geoCoder?.getFromLocation(lat, long, 1)
        if (address != null && address.size > 0) {
            name =
                address[0].locality ?: address[0].subAdminArea ?: address[0].adminArea
        }
        return name
    }

    companion object {

        object LocationGps {
            var longitude = 0.0
            var latitude = 0.0
        }

        var locationGps: MutableLiveData<LocationGps> = MutableLiveData(LocationGps)
        var locationCity: MutableLiveData<Pair<Double, Double>> = MutableLiveData()
        var elevetion = MutableLiveData<Int>()
    }
}