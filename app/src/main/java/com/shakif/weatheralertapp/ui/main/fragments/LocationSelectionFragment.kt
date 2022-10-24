package com.shakif.weatheralertapp.ui.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.shakif.weatheralertapp.databinding.FragmentLocationSelectionBinding
import com.shakif.weatheralertapp.model.geocode.viewdata.LocationPickerViewData
import com.shakif.weatheralertapp.model.geocode.viewdata.LocationSuggestionType
import com.shakif.weatheralertapp.model.geocode.viewdata.SuggestedLocationViewData
import com.shakif.weatheralertapp.ui.base.BaseFragment
import com.shakif.weatheralertapp.ui.main.activities.MainActivity
import com.shakif.weatheralertapp.ui.main.adapters.LocationSelectionAdapter
import com.shakif.weatheralertapp.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationSelectionFragment: BaseFragment() {
    private var binding: FragmentLocationSelectionBinding? = null
    private val viewModel: MainViewModel by activityViewModels()
    private val selectionAdapter by lazy { LocationSelectionAdapter { onItemClick(it) } }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (binding == null) binding = FragmentLocationSelectionBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        setObservers()
    }

    private fun setAdapter() {
        val layoutManager = FlexboxLayoutManager(context)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.FLEX_START

        binding?.locationRecyclerView?.apply {
            this.layoutManager = layoutManager
            adapter = selectionAdapter
            itemAnimator = null
        }
    }

    private fun setObservers() {
        viewModel.suggestionsLiveData.observe(viewLifecycleOwner) { list ->
            selectionAdapter.setData(list)
        }
    }

    private fun onItemClick(item: LocationSuggestionType) {
        when (item) {
            is LocationPickerViewData -> {
                if (item.searchType) {
                    activity?.let { (it as MainActivity).showSearchLocation() }
                }
                else {
                    activity?.let { (it as MainActivity).showWeatherFragmentForCurrentLocation() }
                }
            }
            is SuggestedLocationViewData -> {
                viewModel.fetchLocation(item.lat, item.lon)
                activity?.let { (it as MainActivity).showForecastFragment() }
            }
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}