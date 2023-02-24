package com.example.paymentapp.peresentation.RecyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.paymentapp.data.models.BaseModel
import com.example.paymentapp.databinding.CardViewBinding

class HomeAdapter(private val list :ArrayList<BaseModel>):RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    private lateinit var listener : OnItemClickListener

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener)
    {
        this.listener = listener
    }


    inner class HomeViewHolder(val binding:CardViewBinding,listener: OnItemClickListener):RecyclerView.ViewHolder(binding.root){

        init {
            binding.rvItem.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }

    }
    override fun onCreateViewHolder(parent:ViewGroup,viewType:Int):HomeViewHolder {
        val binding = CardViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(binding,listener)
    }

    override fun onBindViewHolder(holder: HomeViewHolder,position: Int) {
        holder.binding.nameOfCustomer.text = list[position].name
        holder.binding.valueOfTheDebt.text = list[position].monthlyPay
        holder.binding.dateOfTheDebt.text = list[position].monthlyDayOfPaying
        holder.binding.value.text = "قيمة القسط : "
        holder.binding.date.text = "موعد السداد : "
    }

    override fun getItemCount(): Int {
        return list.size ?: 0
    }

}