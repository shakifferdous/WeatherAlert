package com.shakif.weatheralertapp.ui.main.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shakif.weatheralertapp.databinding.EachForecastBinding
import com.shakif.weatheralertapp.model.weather.WeatherInfo
import com.shakif.weatheralertapp.utility.datetime.TimeFormat.DD_MMM_HH_MM
import com.shakif.weatheralertapp.utility.datetime.TimeFormat.YYYY_MM_DD_HH_MM_SS
import com.shakif.weatheralertapp.utility.datetime.formatDateWithPattern

class ForecastAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val items = mutableListOf<WeatherInfo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = EachForecastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ForecastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ForecastViewHolder).show(items[position])
    }

    override fun getItemCount() = items.size

    fun setData(list: List<WeatherInfo>) {
        val oldSize = items.size
        val newSize = list.size
        items.clear()
        notifyItemRangeRemoved(0, oldSize)
        items.addAll(list)
        notifyItemRangeInserted(0, newSize)
    }

    inner class ForecastViewHolder(private val binding: EachForecastBinding): RecyclerView.ViewHolder(binding.root) {
        fun show(item: WeatherInfo) {
            item.dt_txt?.let { binding.timeTextView.text = formatDateWithPattern(it, YYYY_MM_DD_HH_MM_SS, DD_MMM_HH_MM) }
            "${item.main?.temp}C".also { binding.temperatureTextView.text = it }
        }
    }
}