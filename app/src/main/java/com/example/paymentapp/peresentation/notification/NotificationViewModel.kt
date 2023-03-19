package com.example.paymentapp.peresentation.notification

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.paymentapp.MyApp
import com.example.paymentapp.data.models.BaseModel
import com.example.paymentapp.data.repositories.BaseRepository
import com.example.paymentapp.data.source.homeDatabase.HomeDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

class NotificationViewModel : ViewModel() {

    private var _notificationsList: MutableLiveData<ArrayList<BaseModel>> = MutableLiveData()
    val notificationsList: LiveData<ArrayList<BaseModel>> = _notificationsList

    private var _firstData: MutableLiveData<Boolean> = MutableLiveData(true)
    val firstData: LiveData<Boolean> = _firstData

    val repository: BaseRepository

    init {
        val dao = HomeDataBase.getInstance(MyApp.context).myDao()
        repository = BaseRepository(dao)
        _notificationsList.value = ArrayList()
    }

    fun setFirstData(boolean: Boolean) {
        _firstData.value = boolean
    }

    suspend fun getAllFromRoom(): Flow<List<BaseModel>> = withContext(Dispatchers.IO) {
        repository.getAllToObserve()
    }

    fun getList(): ArrayList<BaseModel> {
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)

        val listOnTop = ArrayList<BaseModel>()
        listOnTop.addAll(_notificationsList.value!!.filter { it.monthlyDayOfPaying.toInt() == day })
        var tempList = ArrayList<BaseModel>()
        tempList.addAll(_notificationsList.value!!.sortedByDescending { it.numberOfLateMoneyMonths })
        for (i in listOnTop) tempList.remove(i)
        var listToReturn = ArrayList<BaseModel>()
        listToReturn.addAll(listOnTop)
        listToReturn.addAll(tempList)

        val lastList = ArrayList<BaseModel>()
        for (i in listToReturn) {
            val x = i.startDate.split("/")
            if ((month + 1).toString() >= x[1]) {
                lastList.add(i)
            }else if ((month + 1).toString() < x[1] && year.toString() > x[0]){
                lastList.add(i)
            }

        }
        Log.d("suz" , lastList.map { it.name }.toString())
        return lastList
    }

    suspend fun resetArrayList(baseModels: List<BaseModel>) {
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        _notificationsList.value!!.clear()
        _notificationsList.value!!.addAll(baseModels.filter {
            it.monthlyDayOfPaying == day.toString() ||
                    it.numberOfLateMoneyMonths > 0
        })
    }

}