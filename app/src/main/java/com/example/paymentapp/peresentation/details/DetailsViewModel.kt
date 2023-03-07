package com.example.paymentapp.peresentation.details

import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide.init
import com.example.paymentapp.MyApp
import com.example.paymentapp.data.models.BaseModel
import com.example.paymentapp.data.repositories.BaseRepository
import com.example.paymentapp.data.source.homeDatabase.HomeDataBase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor() : ViewModel() {

    lateinit var historyArray : ArrayList<String>
    private val repository: BaseRepository

    var name = ""
    var phone = ""
    var income = 0.0
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
    var newHistory = false

    init {
        val dao = HomeDataBase.getInstance(MyApp.context).myDao()
        repository = BaseRepository(dao)
    }


    fun addDateToItem(model: BaseModel, currentDate: String) {
        model.historyList.add(currentDate)
        model.payNumber++
        // Log.d("suzan" , model.numberOfPaidInstallments.toString())
        model.notPayNumber--
        model.payValue =
            (model.payValue.toFloat() + model.monthlyPay.toFloat()).toDouble()
        model.notPayValue =
            (model.notPayValue.toFloat() - model.monthlyPay.toFloat()).toDouble()
        model.userHavePaidToday()
    }

    fun removeLastDateFromItem(model: BaseModel) {
        model.historyList.removeLast()
        model.payNumber--
        model.notPayNumber++
        model.payValue =
            (model.payValue.toFloat() - model.monthlyPay.toFloat()).toDouble()
        model.notPayValue =
            (model.notPayValue.toFloat() + model.monthlyPay.toFloat()).toDouble()
        model.undoPay()
    }

    fun isNewData(model: BaseModel): Boolean =
        model.phoneNumber == phone &&
                model.priceWithoutAddition == priceBefore &&
                model.income == income &&
                model.addintionPercentage == splitPercentage(benefits) &&
                model.additionMoney == benefitsValue &&
                model.priceAfterAddition == priceAfter &&
                model.numberOfTotalInstallments == totalInstallmentsNumber.toInt() &&
                model.payNumber == payiedInstallmentsNumber.toInt() &&
                model.payValue == payiedInstallmentsValue.toDouble() &&
                model.notPayNumber == comingInstallmentsNumber.toInt() &&
                model.notPayValue == comingInstallmentsVlaue.toDouble() &&
                model.monthlyDayOfPaying == dayOfPaying &&
                model.startDate == startDate &&
                model.nameOfBoughtItems == carModel &&
                model.monthlyPay == monthlyPayValue &&
                model.note == myNote &&
                model.name == name && !newHistory


    private fun splitPercentage(benefits: String): Double {

        if (benefits.contains(" ")) {
            val o = benefits.split(" ")
            return (o[0].toDouble())
        } else if (benefits.contains("%")) {
            val o = benefits.split("%")
            return (o[0].toDouble())
        } else {
            return benefits.toDouble()
        }
    }

    suspend fun saveData(model: BaseModel) {
        model.name = name
        model.phoneNumber = phone
        model.income = income
        model.priceWithoutAddition = priceBefore
        model.addintionPercentage = splitPercentage(benefits)
        model.additionMoney = benefitsValue
        model.priceAfterAddition = priceAfter
        model.numberOfTotalInstallments = totalInstallmentsNumber.toInt()
        model.payNumber = payiedInstallmentsNumber.toInt()
        model.payValue = payiedInstallmentsValue.toDouble()
        model.notPayNumber = comingInstallmentsNumber.toInt()
        model.notPayValue = comingInstallmentsVlaue.toDouble()
        model.monthlyDayOfPaying = dayOfPaying
        model.startDate = startDate
        model.nameOfBoughtItems = carModel
        model.monthlyPay = monthlyPayValue
        model.note = myNote
        updateModel(model)
    }

    private suspend fun updateModel(model: BaseModel) = withContext(Dispatchers.IO) {
        repository.update(model)
    }

}