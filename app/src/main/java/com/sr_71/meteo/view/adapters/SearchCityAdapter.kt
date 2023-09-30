package com.sr_71.meteo.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sr_71.meteo.R
import com.sr_71.meteo.databinding.SearchCityItemLayoutBinding
import com.sr_71.meteo.model.Citys
import com.sr_71.meteo.model.Propertie

class SearchCityAdapter(var city: Citys, val callback: AdapterCityOnClick) : RecyclerView.Adapter<SearchCityAdapter.SearchCityViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchCityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_city_item_layout, parent, false)
        return SearchCityViewHolder(SearchCityItemLayoutBinding.bind(view))
    }

    override fun getItemCount(): Int {
        return city.features?.size ?: 0
    }

    override fun onBindViewHolder(holder: SearchCityViewHolder, position: Int) {
        val city = city.features?.get(position)
        holder.onBind(city?.properties)
    }

    inner class SearchCityViewHolder( binding: SearchCityItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        val name: TextView = binding.city
        val country: TextView = binding.country

        fun onBind(city: Propertie?) {
            name.text = city?.city ?: city?.formatted
            country.text = city?.country

            itemView.setOnClickListener {
                callback.onClick(Pair(city?.lat ?: 0.0, city?.lon ?: 0.0))
            }
        }
    }
}