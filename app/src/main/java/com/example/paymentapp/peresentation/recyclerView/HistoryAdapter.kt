package com.example.paymentapp.peresentation.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.paymentapp.databinding.ListViewBinding

class HistoryAdapter(private val list: ArrayList<String>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewModel>() {

    inner class HistoryViewModel(val binding: ListViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewModel {
        return HistoryViewModel(ListViewBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: HistoryViewModel, position: Int) {
        holder.binding.tvHistoryView.text = list[position]
    }

    override fun getItemCount(): Int {
        return list.size ?: 0
    }
}