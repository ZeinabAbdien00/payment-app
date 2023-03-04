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

    private var _newItemInserted: MutableLiveData<Boolean> = MutableLiveData(false)
    val newItemInserted: LiveData<Boolean> = _newItemInserted

    private var _isSearch: MutableLiveData<Boolean> = MutableLiveData(false)
    val isSearch: LiveData<Boolean> = _isSearch

    private var _normalMode: MutableLiveData<Boolean> = MutableLiveData(false)
    val normalMode: LiveData<Boolean> = _normalMode


    private val repository: BaseRepository


    init {
        val dao = HomeDataBase.getInstance(MyApp.context).myDao()
        repository = BaseRepository(dao)
        _dataList.value= ArrayList()
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
//        viewModelScope.launch {
//            _dataList.value!!.addAll(getAllFromRoom().first())
//            _dataList.value!!.sortByDescending { day - it.monthlyDayOfPaying.toInt()}
//        }
    }

    suspend fun insertToRoom(model: BaseModel) {
        _dataList.value!!.add(model)
        withContext(Dispatchers.IO) {
            repository.insert(model)
        }
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
        _dataList.value!!.clear()
        _dataList.value!!.addAll(baseModels)
        _dataList.value!!.sortByDescending { day - it.monthlyDayOfPaying.toInt()}
    }

    fun setFirstData(boolean: Boolean) {
        _firstData.value = boolean
    }

    fun setNewItemInserted(boolean: Boolean) {
        _newItemInserted.value = boolean
    }

    fun setIsSearch(boolean: Boolean) {
        _isSearch.value = boolean
    }

    fun setIsNormalMode(b: Boolean) {
        _normalMode.value = b
    }

    suspend fun removeItemOf(position: Int) {
        val item = _dataList.value!!.get(position)
        _dataList.value!!.remove(item)
        deleteFromRoom(item)
    }

    fun removeItemFromDataList(item: BaseModel) {
        _dataList.value!!.remove(item)
    }

    fun createFakeData(name: String): BaseModel {
        return BaseModel(
            name,
            "01045687563",
            "2500",
            0.0f,
            0,
            "12/3/2023",
            "12/1/2023",
            "botato",
            "1500",
            "500",
            additionMoney = "50",
            income = 1000.0f
        )
    }

}