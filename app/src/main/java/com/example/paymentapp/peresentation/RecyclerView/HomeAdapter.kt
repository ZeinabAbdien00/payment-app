package com.example.paymentapp.peresentation.RecyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.helper.widget.Carousel.Adapter
import androidx.recyclerview.widget.RecyclerView
import com.example.paymentapp.data.models.BaseModel
import com.example.paymentapp.databinding.CardViewBinding

class HomeAdapter(private val list :ArrayList<BaseModel>):RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    inner class HomeViewHolder(val binding:CardViewBinding):RecyclerView.ViewHolder(binding.root){

    }
    override fun onCreateViewHolder(parent:ViewGroup,viewType:Int):HomeViewHolder {
        return HomeViewHolder(CardViewBinding.inflate(LayoutInflater.from(parent.context),parent,false))
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

    class OnAddClickListener(val clickListener: (baseModel: BaseModel, position: Int) -> Unit) {
        fun onAddBirdClick(baseModel: BaseModel, position: Int) = clickListener(baseModel, position)
    }
}