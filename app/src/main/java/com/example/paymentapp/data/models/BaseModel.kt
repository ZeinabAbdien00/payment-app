package com.example.paymentapp.data.models

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@Entity
@Keep
data class BaseModel(
    var name: String,
    var phoneNumber: String,
    var priceWithoutAddition: String,
    var addintionPercentage: Double,
    var numberOfTotalInstallments: Int,   // how many months until done
    var monthlyDayOfPaying: String,
    var startDate: String,
    var nameOfBoughtItems: String,
    var priceAfterAddition: String,
    var monthlyPay: String,
    var additionMoney: String,
    var income: Double,
    var payValue :Double ,
    var payNumber :Int ,
    var notPayValue :Double ,
    var notPayNumber :Int
) : Parcelable {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    // add to this list when person starts to pay the first month
    lateinit var historyList: ArrayList<String>

//    // how much did he paid until now
//    var valueOfPayInstallments: String = "0"
//
//    // how many Installments did he pay untill now
//    var numberOfPaidInstallments: Int = 0
//
//    // how much money is left to pay
//    var valueOfComingInstallments: String = "0"
//
//    // how many Installments are left
//    var numberOfComingInstallments: Int = 0

    //number of months that didnot pay yet
    var numberOfLateMoneyMonths = 0

    //notes
    var note = " "

    // indicates if we added 1 to late or not if today is paying day
    var oneWasAdded = false

    //indicates if user have paid that 1 or not
    var userPaidToday = false

    init {
        customInit()
    }

    fun customInit() {
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        if (day.toString() == monthlyDayOfPaying && !oneWasAdded) {
            oneWasAdded = true
            numberOfLateMoneyMonths++
        } else if (day.toString() != monthlyDayOfPaying) {
            oneWasAdded = false
            userPaidToday = false
        }
    }

    fun userHavePaidToday() {
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        if (monthlyDayOfPaying == day.toString()) {
            userPaidToday = true
        }
        if(numberOfLateMoneyMonths > 0 )numberOfLateMoneyMonths--
    }

    fun undoPay() {
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        if (day.toString() == monthlyDayOfPaying) {
            userPaidToday = false
        }
        numberOfLateMoneyMonths++

    }

}