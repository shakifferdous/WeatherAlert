package com.shakif.weatheralertapp.ui.main.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.shakif.weatheralertapp.databinding.FragmentSearchLocationBinding
import com.shakif.weatheralertapp.model.geocode.viewdata.LocationViewData
import com.shakif.weatheralertapp.ui.base.BaseFragment
import com.shakif.weatheralertapp.ui.main.activities.MainActivity
import com.shakif.weatheralertapp.ui.main.adapters.SearchLocationAdapter
import com.shakif.weatheralertapp.viewmodel.MainViewModel

class SearchLocationFragment: BaseFragment() {
    private var binding: FragmentSearchLocationBinding? = null
    private val viewModel: MainViewModel by activityViewModels()
    private val searchAdapter by lazy { SearchLocationAdapter { onItemClick(it) } }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (binding == null) binding = FragmentSearchLocationBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setAdapter()
        setObservers()
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    private fun setupUI() {
        binding?.backButton?.setOnClickListener {
            binding?.root?.hideKeyboard()
            activity?.onBackPressed()
        }

        binding?.searchEditText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(text: Editable?) {
                val query = text.toString()
                binding?.searchProgressbar?.visibility = if (query.length > 1) View.VISIBLE else View.GONE
                if (query.length > 1) {
                    viewModel.search(query)
                } else searchAdapter.setData(listOf())
            }
        })
    }

    private fun setAdapter() {
        binding?.locationSearchRecyclerView?.apply {
            adapter = searchAdapter
            itemAnimator = null
        }
    }

    private fun setObservers() {
        viewModel.searchLiveData.observe(viewLifecycleOwner) { pair ->
            val searchQuery = pair.first.trim()
            val currentText = binding?.searchEditText?.text?.toString()?.trim() ?: ""
            if (currentText.length > 1) {
                if (searchQuery == currentText) {
                    binding?.searchProgressbar?.visibility = View.GONE
                    searchAdapter.setData(pair.second)
                } else searchAdapter.setData(listOf())
            } else binding?.searchProgressbar?.visibility = View.GONE
        }

        viewModel.errorLiveData.observe(viewLifecycleOwner) {
            if (it.isNotBlank()) binding?.searchProgressbar?.visibility = View.GONE
        }
    }

    private fun onItemClick(item: LocationViewData) {
        viewModel.saveSelectedLocation(item)
        viewModel.fetchCurrentWeather()
        viewModel.fetchForecast()
        binding?.root?.hideKeyboard()
        activity?.let { (it as MainActivity).showForecastFragment() }
    }
}