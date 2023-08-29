package com.sr_71.meteo.view_model

import android.location.Location
import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.parcelize.Parcelize

@Parcelize
class LocationViewModel() : ViewModel(), Parcelable {
    private var _location: MutableLiveData<Location> = MutableLiveData()
    val location: LiveData<Location>
        get() = _location

    fun setLocation(location: Location) {
        if (_location.value?.longitude != location.longitude || _location.value?.latitude != location.latitude)
            _location.value = location
    }
}