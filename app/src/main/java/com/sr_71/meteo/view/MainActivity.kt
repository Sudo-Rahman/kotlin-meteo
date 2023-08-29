package com.sr_71.meteo.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.sr_71.meteo.API.*
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.sr_71.meteo.R
import com.sr_71.meteo.model.Citys
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private var locationManager: LocationManager? = null
    private var _location: Location? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // lauch couroutine
        lifecycleScope.launch {
            getLocation()
            val autoComplete = GeoapifyApiManager.retrofitService.getCity("chalo")
            println("autoComplete: $autoComplete")
            var gson = Gson()
            var citys = gson.fromJson(autoComplete, Citys::class.java)

            println(citys)


        }
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
//            println("location: $location")
            if (_location == null) _location = location
            else {
                if (_location != location) _location = location
            }

            findViewById<TextView>(R.id.txt).text =
                getCityName(location.latitude, location.longitude)

            lifecycleScope.launch {

                val weather = WeatherApiManager.retrofitService.getNowWheater(
                    _location?.latitude.toString(),
                    _location?.longitude.toString()
                )
            }
        }

        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    private fun getCityName(lat: Double, long: Double): String {
        val cityName: String?
        val geoCoder = Geocoder(this, Locale.getDefault())
        val address = geoCoder.getFromLocation(lat, long, 1)
        cityName = address?.get(0)?.locality
        return cityName ?: "Unknown"
    }

}

