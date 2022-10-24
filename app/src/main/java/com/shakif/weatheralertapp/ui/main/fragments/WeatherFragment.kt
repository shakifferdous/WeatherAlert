package com.shakif.weatheralertapp.ui.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.shakif.weatheralertapp.databinding.FragmentWeatherBinding
import com.shakif.weatheralertapp.ui.base.BaseFragment
import com.shakif.weatheralertapp.ui.main.activities.MainActivity
import com.shakif.weatheralertapp.ui.main.adapters.ForecastAdapter
import com.shakif.weatheralertapp.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherFragment: BaseFragment() {
    private var binding: FragmentWeatherBinding? = null
    private val forecastAdapter by lazy { ForecastAdapter() }
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (binding == null) binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setAdapter()
        setObservers()
    }

    private fun setupUI() {
        binding?.newLocationPickerButton?.setOnClickListener {
            activity?.let {
                (it as MainActivity).showLocationSelectionFragment()
            }
        }
    }

    private fun setAdapter() {
        binding?.forecastRecyclerView?.apply {
            adapter = forecastAdapter
            itemAnimator = null
        }
    }

    private fun setObservers() {
        viewModel.currentWeatherLiveData.observe(viewLifecycleOwner) { data ->
            binding?.cityNameTextView?.text = data.name
            data.main?.let { main ->
                "${main.temp}C".also { binding?.currentWeatherTextView?.text = it }
                "${main.feels_like}C".also { binding?.feelsLikeTextView?.text = "Feels like: $it" }
            }
        }

        viewModel.forecastLiveData.observe(viewLifecycleOwner) { data ->
            data.list?.let {
                forecastAdapter.setData(it)
                binding?.forecastLabel?.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
            }
        }

        viewModel.loadingLiveData.observe(viewLifecycleOwner) { isLoading ->
            binding?.loadingView?.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.errorLiveData.observe(viewLifecycleOwner) { error ->
            if (error.isNotBlank()) showErrorDialog(error)
        }
    }
}