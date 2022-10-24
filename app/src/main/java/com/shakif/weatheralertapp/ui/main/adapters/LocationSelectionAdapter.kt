package com.shakif.weatheralertapp.ui.main.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shakif.weatheralertapp.databinding.EachHintLabelBinding
import com.shakif.weatheralertapp.databinding.EachLocationPickerBinding
import com.shakif.weatheralertapp.databinding.EachSuggestedCityBinding
import com.shakif.weatheralertapp.model.geocode.viewdata.HintViewData
import com.shakif.weatheralertapp.model.geocode.viewdata.LocationPickerViewData
import com.shakif.weatheralertapp.model.geocode.viewdata.LocationSuggestionType
import com.shakif.weatheralertapp.model.geocode.viewdata.SuggestedLocationViewData

class LocationSelectionAdapter(private val onItemClick: (LocationSuggestionType) -> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val items = mutableListOf<LocationSuggestionType>()

    override fun getItemViewType(position: Int): Int {
        return when(items[position]) {
            is HintViewData -> TYPE_HINT
            is LocationPickerViewData -> TYPE_PICKER
            is SuggestedLocationViewData -> TYPE_SUGGESTION
            else -> throw IllegalArgumentException()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HINT -> {
                val binding = EachHintLabelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                HintViewHolder(binding)
            }
            TYPE_PICKER -> {
                val binding = EachLocationPickerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                PickerViewHolder(binding)
            }
            TYPE_SUGGESTION -> {
                val binding = EachSuggestedCityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                SuggestionViewHolder(binding)
            }
            else -> throw IllegalArgumentException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            TYPE_HINT -> {
                (holder as HintViewHolder).show((items[position] as HintViewData))
            }
            TYPE_PICKER -> {
                (holder as PickerViewHolder).show((items[position] as LocationPickerViewData))
            }
            TYPE_SUGGESTION -> {
                (holder as SuggestionViewHolder).show((items[position] as SuggestedLocationViewData))
            }
        }
    }

    override fun getItemCount() = items.size

    fun setData(list: List<LocationSuggestionType>) {
        val oldSize = items.size
        val newSize = list.size
        items.clear()
        notifyItemRangeRemoved(0, oldSize)
        items.addAll(list)
        notifyItemRangeInserted(0, newSize)
    }

    inner class HintViewHolder(val binding: EachHintLabelBinding): RecyclerView.ViewHolder(binding.root) {
        fun show(item: HintViewData) {
            binding.topLabel.text = item.title
        }
    }

    inner class PickerViewHolder(val binding: EachLocationPickerBinding): RecyclerView.ViewHolder(binding.root) {
        fun show(item: LocationPickerViewData) {
            binding.titleTextView.text = item.title
            binding.iconImageView.visibility = if (item.searchType) View.VISIBLE else View.GONE
            itemView.setOnClickListener { onItemClick(item) }
        }
    }

    inner class SuggestionViewHolder(val binding: EachSuggestedCityBinding): RecyclerView.ViewHolder(binding.root) {
        fun show(item: SuggestedLocationViewData) {
            binding.cityNameTextView.text = item.locationName
            itemView.setOnClickListener { onItemClick(item) }
        }
    }

    companion object {
        const val TYPE_HINT = 0
        const val TYPE_PICKER = 1
        const val TYPE_SUGGESTION = 2
    }
}