package com.sr_71.meteo.view.fagments

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.R
import com.sr_71.meteo.databinding.FragmentSearchCityBinding
import com.sr_71.meteo.model.Citys
import com.sr_71.meteo.view.adapters.SearchCityAdapter
import com.sr_71.meteo.view_model.CitySearchViewModel

class SearchCityFragment() : Fragment() {
    private var _citySearchViewModel = CitySearchViewModel()

    private lateinit var recyclerView: RecyclerView

    private lateinit var _binding: FragmentSearchCityBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchCityBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = _binding.searchCityRecycler
        val dividerItemDecoration = DividerItemDecoration(
            recyclerView.context,
            DividerItemDecoration.VERTICAL
        )

        dividerItemDecoration.setDrawable(
            ColorDrawable(
                resources.getColor(
                    R.color.material_grey_600,
                    null
                )
            )
        )
        recyclerView.addItemDecoration(dividerItemDecoration)

        _binding.cityTextEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.isNullOrBlank()) return
                _citySearchViewModel.searchCity(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        _binding.cityTextEdit.setOnClickListener {
            _binding.cityTextEdit.setText("")
        }
        listener()
    }

    private fun listener() {
        _citySearchViewModel.city.observe(viewLifecycleOwner) {
            if (recyclerView.adapter == null) {
                recyclerView.adapter = SearchCityAdapter(_citySearchViewModel.city.value ?: Citys())
            } else {
                val adapter = recyclerView.adapter as SearchCityAdapter
                adapter.city = _citySearchViewModel.city.value ?: Citys()
                adapter.notifyDataSetChanged()
            }
        }
    }
}