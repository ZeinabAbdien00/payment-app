package com.example.paymentapp.peresentation.home

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


class HomeViewModel : ViewModel() {

    private var _dataList: MutableLiveData<ArrayList<BaseModel>?> = MutableLiveData()
    val dataList: LiveData<ArrayList<BaseModel>?> = _dataList

    private var _firstData: MutableLiveData<Boolean> = MutableLiveData(true)
    val firstData: LiveData<Boolean> = _firstData


    private val repository: BaseRepository


    init {
        val dao = HomeDataBase.getInstance(MyApp.context).myDao()
        repository = BaseRepository(dao)
        viewModelScope.launch {
            _dataList.value = getAllFromRoom()
        }
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

    suspend fun getAllFromRoom(): ArrayList<BaseModel> = withContext(Dispatchers.IO) {
        repository.getAll() as ArrayList<BaseModel>
    }

    fun createFakeData(name: String): BaseModel {
        return BaseModel(
            name,
            "01045687563",
            "2500",
            0.0f,
            0,
            "12/3/2023",
            "12/1/2023"
        )
    }

    fun setFirstData(boolean: Boolean){
        _firstData.value=boolean
    }
}