package com.example.paymentapp.peresentation.home

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


class HomeViewModel : ViewModel() {

    private var _dataList: MutableLiveData<ArrayList<BaseModel>?> = MutableLiveData()
    val dataList: LiveData<ArrayList<BaseModel>?> = _dataList

    private var _firstData: MutableLiveData<Boolean> = MutableLiveData(true)
    val firstData: LiveData<Boolean> = _firstData

    private var _isSearch: MutableLiveData<Boolean> = MutableLiveData(false)
    val isSearch: LiveData<Boolean> = _isSearch

    private var _normalMode: MutableLiveData<Boolean> = MutableLiveData(true)
    val normalMode: LiveData<Boolean> = _normalMode


    private val repository: BaseRepository


    init {
        val dao = HomeDataBase.getInstance(MyApp.context).myDao()
        repository = BaseRepository(dao)
        _dataList.value = ArrayList()
    }

    suspend fun deleteFromRoom(model: BaseModel) = withContext(Dispatchers.IO) {
        repository.delete(model)
    }

    suspend fun getAllFromRoom(): Flow<List<BaseModel>> = withContext(Dispatchers.IO) {
        repository.getAllToObserve()
    }

    suspend fun resetArrayList(baseModels: List<BaseModel>) {
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
//        _dataList.value!!.clear()
//        _dataList.value!!.addAll(baseModels)
        _dataList.value= baseModels as ArrayList<BaseModel>
        _dataList.value!!.sortByDescending { it.monthlyDayOfPaying.toInt() - day }
    }

    fun setFirstData(boolean: Boolean) {
        _firstData.value = boolean
    }

    fun setIsSearch(boolean: Boolean) {
        _isSearch.value = boolean
    }

    fun setIsNormalMode(b: Boolean) {
        _normalMode.value = b
    }

    suspend fun removeItemOf(position: Int) {
        val item = _dataList.value!!.get(position)
        deleteFromRoom(item)
    }

    fun getListForNonNormalMode(day: Int) {
        val list = ArrayList<BaseModel>()
        list.addAll(dataList.value!!)
        list.sortByDescending { day - it.monthlyDayOfPaying.toInt() }
        _dataList.value!!.clear()
        _dataList.value!!.addAll(list)
    }

    fun getListForNormalMode(day: Int) {
        val list = ArrayList<BaseModel>()
        list.addAll(dataList.value!!)
        list.sortBy { day - it.monthlyDayOfPaying.toInt() }
        _dataList.value!!.clear()
        _dataList.value!!.addAll(list)
    }

}