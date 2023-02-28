package com.example.paymentapp.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class BaseModel (
    var name : String,
    var phoneNumber : String,
    var priceWithoutAddition : String,
    var addintionPercentage : Float,
    var numberOfTotalInstallments:Int,   // how many months until done
    var monthlyDayOfPaying : String,    //todo to ui
    var startDate : String,
    var nameOfBoughtItems:String,       //todo to ui
    var priceAfterAddition : String,
    var monthlyPay : String,
    var additionMoney : String
):Parcelable{

    @PrimaryKey(autoGenerate = true) var id :Int =0
    // add to this list when person starts to pay the first month
    var historyList : String = ""
    // after getting the percentage do your math and set this value
    // how much did he paid until now
    var valueOfPayInstallments : String = ""
    // how many Installments did he pay untill now
    var numberOfPaidInstallments :Int = 0
    // how much money is left to pay
    var valueOfComingInstallments:String = ""
    // how many Installments are left
    var numberOfComingInstallments : Int =0
    //number of months that didnot pay yet
    var numberOfLateMoneyMonths = 0

}