package com.sr_71.meteo.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.sr_71.meteo.R
import com.sr_71.meteo.model.Citys
import com.sr_71.meteo.view.fagments.NavHostFragment

class SearchCityAdapter(var city: Citys) : RecyclerView.Adapter<SearchCityViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchCityViewHolder {
        val vew = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_city_item_layout, parent, false)
        return SearchCityViewHolder(vew)
    }

    override fun getItemCount(): Int {
        return city.features?.size ?: 0
    }

    override fun onBindViewHolder(holder: SearchCityViewHolder, position: Int) {
        val city = city.features?.get(position)
        println(city)
        holder.name.text = city?.properties?.city ?: city?.properties?.formatted
        holder.country.text = city?.properties?.country

        holder.view.setOnClickListener {
            NavHostFragment.locationCity.value =
                Pair(city?.properties?.lon ?: 0.0, city?.properties?.lat ?: 0.0)
            holder.view.findNavController().popBackStack()
        }
    }
}

class SearchCityViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val name: TextView = view.findViewById(R.id.city)
    val country: TextView = view.findViewById(R.id.country)
}