package com.example.paymentapp.peresentation.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.paymentapp.R
import com.example.paymentapp.data.models.BaseModel
import com.example.paymentapp.databinding.CardViewBinding
import java.util.*

class HomeAdapter(private val list: ArrayList<BaseModel>) :
    RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    private lateinit var listener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }


    inner class HomeViewHolder(val binding: CardViewBinding, listener: OnItemClickListener) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.rvItem.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding = CardViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val currentItem = list[position]
        holder.binding.nameOfCustomer.text = currentItem.name
        holder.binding.valueOfTheDebt.text = currentItem.monthlyPay

        if (currentItem.monthlyDayOfPaying == day.toString()) {
            holder.binding.dateOfTheDebt.text = "اليوم"
            holder.binding.nameOfCustomer.background= ContextCompat.getDrawable(holder.binding.nameOfCustomer.context
                , R.color.day_is_today
            )
            holder.binding.btnNavigate.background= ContextCompat.getDrawable(holder.binding.btnNavigate.context
                , R.drawable.day_is_today_tint
            )
            holder.binding.rvItems.background= ContextCompat.getDrawable(holder.binding.btnNavigate.context
                , R.color.day_is_today_background
            )
        } else {
            holder.binding.dateOfTheDebt.text = currentItem.monthlyDayOfPaying
        }

        if (currentItem.numberOfLateMoneyMonths > 0) {
            holder.binding.numberOfLateMonths.visibility = View.VISIBLE
            holder.binding.numberOfLateMonths.text = currentItem.numberOfLateMoneyMonths.toString()
        } else {
            holder.binding.numberOfLateMonths.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}