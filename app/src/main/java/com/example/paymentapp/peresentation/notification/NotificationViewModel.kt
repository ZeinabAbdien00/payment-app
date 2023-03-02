package com.example.paymentapp.peresentation.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paymentapp.data.models.BaseModel
import com.example.paymentapp.data.repositories.BaseRepository
import com.example.paymentapp.data.source.homeDatabase.HomeDataBase
import com.example.paymentapp.globalUse.MyApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class NotificationViewModel : ViewModel() {

    private var _notificationsList: MutableLiveData<ArrayList<BaseModel>> = MutableLiveData()
    val notificationsList: LiveData<ArrayList<BaseModel>> = _notificationsList

    private var _firstData: MutableLiveData<Boolean> = MutableLiveData(true)
    val firstData: LiveData<Boolean> = _firstData

    private val repository: BaseRepository

    init {
        val dao = HomeDataBase.getInstance(MyApp.context).myDao()
        repository = BaseRepository(dao)
        viewModelScope.launch {
            val calendar = Calendar.getInstance()
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            _notificationsList.value = getAllFromRoom().filter {
                it.monthlyDayOfPaying == day.toString() ||
                        it.numberOfLateMoneyMonths > 0
            } as ArrayList<BaseModel>
        }
    }

    fun setFirstData(boolean: Boolean) {
        _firstData.value = boolean
    }

    private suspend fun getAllFromRoom(): ArrayList<BaseModel> = withContext(Dispatchers.IO) {
        repository.getAll() as ArrayList<BaseModel>
    }

    fun getList(): ArrayList<BaseModel> {
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val listOnTop = ArrayList<BaseModel>()
        listOnTop.addAll(_notificationsList.value!!.filter { it.monthlyDayOfPaying.toInt() == day })
        var tempList = ArrayList<BaseModel>()
        tempList.addAll(_notificationsList.value!!.sortedByDescending { it.numberOfLateMoneyMonths })
        for (i in listOnTop) tempList.remove(i)
        var listToReturn = ArrayList<BaseModel>()
        listToReturn.addAll(listOnTop)
        listToReturn.addAll(tempList)
        return listToReturn
    }
}