package com.example.paymentapp.peresentation.details

import androidx.lifecycle.ViewModel
import com.example.paymentapp.data.models.BaseModel
import com.example.paymentapp.data.repositories.BaseRepository
import com.example.paymentapp.data.source.homeDatabase.HomeDataBase
import com.example.paymentapp.globalUse.MyApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor() : ViewModel() {


    private val repository: BaseRepository


    init {
        val dao = HomeDataBase.getInstance(MyApp.context).myDao()
        repository = BaseRepository(dao)
    }


    suspend fun addDateToItem(model: BaseModel, currentDate: String) {
        model.historyList.add(currentDate)
        updateModel(model)
    }

    suspend fun removeLastDateFromItem(model: BaseModel) {
        model.historyList.removeLast()
        updateModel(model)
    }


    private suspend fun updateModel(model: BaseModel) = withContext(Dispatchers.IO) {
        repository.update(model)
    }
}