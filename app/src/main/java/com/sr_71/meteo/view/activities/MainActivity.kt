package com.sr_71.meteo.view.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.edit
import com.sr_71.meteo.API.*
import com.sr_71.meteo.R
import com.sr_71.meteo.view.fagments.DailyWeatherFragment
import com.sr_71.meteo.view.fagments.HourlyWeatherFragment
import com.sr_71.meteo.view_model.LocationViewModel
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private var locationManager: LocationManager? = null
    private var _location = LocationViewModel()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(
                R.id.hourlyWeatherFragment,
                HourlyWeatherFragment(_location),
                HourlyWeatherFragment::class.java.simpleName
            )
            .commit()

        supportFragmentManager.beginTransaction()
            .replace(
                R.id.dailyWeatherFrangment,
                DailyWeatherFragment(_location),
                DailyWeatherFragment::class.java.simpleName
            )
            .commit()

        val sharedPref = getSharedPreferences("weather", MODE_PRIVATE)
        sharedPref.run {
            val city = getString("city", "Paris")
            val longitude = getString("longitude", "0.0")!!.toDouble()
            val latitude = getString("latitude", "0.0")!!.toDouble()
            _location.setLocation(Location("").apply {
                this.longitude = longitude
                this.latitude = latitude
            })
            findViewById<TextView>(R.id.txt).text = city
        }
        getLocation()

    }

    // get permission when app start and get location
    private fun getLocation() {
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?


        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
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
                locationListener
            )
        }


    }

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            _location.setLocation(location)
            val city = getCityName(location.latitude, location.longitude)
            findViewById<TextView>(R.id.txt).text = city

            val sharedPref = getSharedPreferences("weather", MODE_PRIVATE)
            sharedPref.edit {
                putString("city", city)
                putString("longitude", location.longitude.toString())
                putString("latitude", location.latitude.toString())
            }
        }

        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    private fun getCityName(lat: Double, long: Double): String {
        val geoCoder = Geocoder(this, Locale.getDefault())
        val address = geoCoder.getFromLocation(lat, long, 1)
        return address?.get(0)?.locality ?: address?.get(0)?.adminArea ?: "Unknown"
    }

}

