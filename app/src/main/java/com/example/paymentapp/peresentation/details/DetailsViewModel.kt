package com.example.paymentapp.peresentation.details

import androidx.lifecycle.ViewModel
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

    lateinit var model: BaseModel
    var name = ""
    var phone = ""
    var income = 0.0
    var priceBefore = 0.0
    var benefits = 0.0
    var benefitsValue = 0.0
    var priceAfter = 0.0
    var totalInstallmentsNumber = 0
    var payiedInstallmentsNumber = 0
    var payiedInstallmentsValue = 0.0
    var comingInstallmentsVlaue = 0.0
    var comingInstallmentsNumber = 0
    var dayOfPaying = ""
    var startDate = " "
    var carModel = ""
    var monthlyPayValue = 0.0
    var myNote = ""
    var newHistory = false

    init {
        val dao = HomeDataBase.getInstance(MyApp.context).myDao()
        repository = BaseRepository(dao)
    }

    fun setMyModel(myModel: BaseModel){
        model = myModel
        historyArray=model.historyList
    }

    fun addDateToItem(currentDate: String) {
        historyArray.add(currentDate)

        payiedInstallmentsNumber++

        if (comingInstallmentsVlaue>0){
        comingInstallmentsNumber--
        }

        payiedInstallmentsValue += monthlyPayValue
        comingInstallmentsVlaue -= monthlyPayValue

        model.userHavePaidToday()
    }

    fun removeLastDateFromItem() {
        historyArray.removeAt(historyArray.size-1)

        if (payiedInstallmentsNumber>0)
        payiedInstallmentsNumber--

        comingInstallmentsNumber++
        payiedInstallmentsValue -= monthlyPayValue.toDouble()
        comingInstallmentsVlaue += monthlyPayValue.toDouble()

        model.undoPay()
    }

    fun isNewData(): Boolean =
        model.phoneNumber == phone &&
                model.priceWithoutAddition == priceBefore.toString() &&
                model.income == income &&
                model.addintionPercentage == splitPercentage(benefits.toString()) &&
                model.additionMoney == benefitsValue.toString() &&
                model.priceAfterAddition == priceAfter.toString() &&
                model.numberOfTotalInstallments == totalInstallmentsNumber.toInt() &&

                model.payNumber == payiedInstallmentsNumber &&
                model.payValue == payiedInstallmentsValue&&
                model.notPayNumber == comingInstallmentsNumber &&
                model.notPayValue == comingInstallmentsVlaue &&

                model.monthlyDayOfPaying == dayOfPaying &&
                model.startDate == startDate &&
                model.nameOfBoughtItems == carModel &&
                model.monthlyPay == monthlyPayValue.toString() &&
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

    suspend fun saveData() {
        model.name = name
        model.phoneNumber = phone
        model.income = income
        model.priceWithoutAddition = priceBefore.toString()
        model.addintionPercentage = splitPercentage(benefits.toString())
        model.additionMoney = benefitsValue.toString()
        model.priceAfterAddition = priceAfter.toString()
        model.numberOfTotalInstallments = totalInstallmentsNumber.toInt()

        model.payNumber = payiedInstallmentsNumber.toInt()
        model.payValue = payiedInstallmentsValue.toDouble()
        model.notPayNumber = comingInstallmentsNumber.toInt()
        model.notPayValue = comingInstallmentsVlaue.toDouble()

        model.monthlyDayOfPaying = dayOfPaying
        model.startDate = startDate
        model.nameOfBoughtItems = carModel
        model.monthlyPay = monthlyPayValue.toString()
        model.note = myNote
        updateModel(model)
    }

    private suspend fun updateModel(model: BaseModel) = withContext(Dispatchers.IO) {
        repository.update(model)
    }

}