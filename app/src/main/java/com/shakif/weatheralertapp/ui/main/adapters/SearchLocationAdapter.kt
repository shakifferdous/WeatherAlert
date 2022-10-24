package com.shakif.weatheralertapp.ui.main.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shakif.weatheralertapp.databinding.EachLocationSearchItemBinding
import com.shakif.weatheralertapp.model.geocode.viewdata.LocationViewData

class SearchLocationAdapter(private val onClick: (LocationViewData) -> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val items = mutableListOf<LocationViewData>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = EachLocationSearchItemBinding.inflate(LayoutInflater.from(parent.context))
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as SearchViewHolder).show(items[position])
    }

    override fun getItemCount() = items.size

    fun setData(data: List<LocationViewData>) {
        val oldSize = items.size
        val newSize = data.size
        items.clear()
        notifyItemRangeRemoved(0, oldSize)
        items.addAll(data)
        notifyItemRangeInserted(0, newSize)
    }

    inner class SearchViewHolder(val binding: EachLocationSearchItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun show(item: LocationViewData) {
            listOf(item.name, item.state).joinToString(", ").also { binding.searchTextView.text = it }
            itemView.setOnClickListener { onClick(item) }
        }
    }
}