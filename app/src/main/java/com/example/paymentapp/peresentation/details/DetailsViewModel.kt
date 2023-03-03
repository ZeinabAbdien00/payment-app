package com.example.paymentapp.peresentation.details

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.paymentapp.data.models.BaseModel
import com.example.paymentapp.data.repositories.BaseRepository
import com.example.paymentapp.data.source.homeDatabase.HomeDataBase
import com.example.paymentapp.globalUse.MyApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor() : ViewModel() {


    private val repository: BaseRepository

    var name = ""
    var phone = ""
    var priceBefore = ""
    var benefits = ""
    var benefitsValue = ""
    var priceAfter = ""
    var totalInstallmentsNumber = ""
    var payiedInstallmentsNumber = ""
    var payiedInstallmentsValue = ""
    var comingInstallmentsVlaue = ""
    var comingInstallmentsNumber = ""
    var dayOfPaying = ""
    var startDate = " "
    var carModel = ""
    var monthlyPayValue = ""
    var myNote = ""

    init {
        val dao = HomeDataBase.getInstance(MyApp.context).myDao()
        repository = BaseRepository(dao)
    }


    suspend fun addDateToItem(model: BaseModel, currentDate: String) {
        model.historyList.add(currentDate)
        model.numberOfPaidInstallments++
        model.numberOfComingInstallments--
        model.valueOfPayInstallments =
            (model.valueOfPayInstallments.toFloat() + model.monthlyPay.toFloat()).toString()
        model.valueOfComingInstallments =
            (model.valueOfComingInstallments.toFloat() - model.monthlyPay.toFloat()).toString()
        updateModel(model)
    }

    suspend fun removeLastDateFromItem(model: BaseModel) {
        model.historyList.removeLast()
        model.numberOfPaidInstallments--
        model.numberOfComingInstallments++
        model.valueOfPayInstallments =
            (model.valueOfPayInstallments.toFloat() - model.monthlyPay.toFloat()).toString()
        model.valueOfComingInstallments =
            (model.valueOfComingInstallments.toFloat() + model.monthlyPay.toFloat()).toString()
        updateModel(model)
    }


    private suspend fun updateModel(model: BaseModel) = withContext(Dispatchers.IO) {
        repository.update(model)
    }

    //return true if any data was changed
    fun isNewData(model: BaseModel): Boolean =
        model.phoneNumber == phone &&
                model.priceWithoutAddition == priceBefore &&
                model.addintionPercentage == splitPercentage(benefits) &&
                model.additionMoney == benefitsValue &&
                model.priceAfterAddition == priceAfter &&
                model.numberOfTotalInstallments == totalInstallmentsNumber.toInt() &&
                model.numberOfPaidInstallments == payiedInstallmentsNumber.toInt() &&
                model.valueOfPayInstallments == payiedInstallmentsValue &&
                model.numberOfComingInstallments == comingInstallmentsNumber.toInt() &&
                model.valueOfComingInstallments == comingInstallmentsVlaue &&
                model.monthlyDayOfPaying == dayOfPaying &&
                model.startDate == startDate &&
                model.nameOfBoughtItems == carModel &&
                model.monthlyPay == monthlyPayValue &&
                model.note == myNote &&
                model.name == name


    private fun splitPercentage(benefits:String): Float {

        if (benefits.contains(" ")) {
            val o = benefits.split(" ")
            return (o[0].toFloat())
        } else if (benefits.contains("%")) {
            val o = benefits.split("%")
            return (o[0].toFloat())
        }else{
            return benefits.toFloat()
        }
    }

    suspend fun saveData(model: BaseModel) {

        model.name = name
        model.phoneNumber = phone
        model.priceWithoutAddition = priceBefore
        model.addintionPercentage = splitPercentage(benefits)
        model.additionMoney = benefitsValue
        model.priceAfterAddition = priceAfter
        model.numberOfTotalInstallments = totalInstallmentsNumber.toInt()
        model.numberOfPaidInstallments = payiedInstallmentsNumber.toInt()
        model.valueOfPayInstallments = payiedInstallmentsValue
        model.numberOfComingInstallments = comingInstallmentsNumber.toInt()
        model.valueOfComingInstallments = comingInstallmentsVlaue
        model.monthlyDayOfPaying = dayOfPaying
        model.startDate = startDate
        model.nameOfBoughtItems = carModel
        model.monthlyPay = monthlyPayValue
        model.note = myNote
        updateModel(model)
    }
}