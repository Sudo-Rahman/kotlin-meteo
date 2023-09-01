package com.sr_71.meteo.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sr_71.meteo.API.GeoapifyApiManager
import com.sr_71.meteo.model.Citys
import kotlinx.coroutines.launch

class CitySearchViewModel : ViewModel() {
    private var _city = MutableLiveData<Citys>()
    val city: LiveData<Citys>
        get() = _city

    fun searchCity(name: String) {
        viewModelScope.launch {
            val result = GeoapifyApiManager.retrofitService.getCity(name = name)
            val gson = com.google.gson.Gson()
            val city = gson.fromJson(result, Citys::class.java)
            _city.value = city
        }
    }
}