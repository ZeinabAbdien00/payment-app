package com.example.paymentapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BaseModel (
    var name : String,
    var phoneNumber : String,
    var priceWithoutAddition : String,
    var addintionPercentage : Float,
    var numberOfTotalInstallments:Int,   // how many months until done
    var monthlyDayOfPaying : String,
    var startDate : String,
        ){
    @PrimaryKey(autoGenerate = true) var id :Int =0
    // add to this list when person starts to pay the first month
    var historyList : String = ""
    // let user add it later if he wants
    var nameOfBoughtItems:String =""
    // after getting the percentage do your math and set this value
    var additionMoney : String=""
    // add the additionMoney to starter money
    var priceAfterAddition : String=""
    //  total money / numberOfTotalInstallments
    var monthlyPay : String= ""
    // how much did he paid until now
    var valueOfPayInstallments : String = ""
    // how many Installments did he pay untill now
    var numberOfPaidInstallments :Int = 0
    // how much money is left to pay
    var valueOfComingInstallments:String = ""
    // how many Installments are left
    var numberOfComingInstallments : Int =0
    // depending on how many moths is he going to pay in
    var endDate : String=""

}