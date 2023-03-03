package com.example.paymentapp.peresentation.splash

import androidx.lifecycle.ViewModel
import com.example.paymentapp.data.dataStore.DataStoreImpl
import com.example.paymentapp.data.models.BaseModel
import com.example.paymentapp.data.repositories.BaseRepository
import com.example.paymentapp.data.source.homeDatabase.HomeDataBase
import com.example.paymentapp.globalUse.MyApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor() : ViewModel() {


    var isReady = false

    @Inject
    lateinit var dataStore: DataStoreImpl

    suspend fun isUsePassword(): Boolean = dataStore.getUsePassword()

    private var repository: BaseRepository

    init {
        val dao = HomeDataBase.getInstance(MyApp.context).myDao()
        repository = BaseRepository(dao)
    }

    suspend fun updateList() {
        val list = getAllFromRoom().first()
        for (model in list) {
            model.customInit()
            updateModel(model)
        }
    }

    private suspend fun updateModel(model: BaseModel) = withContext(Dispatchers.IO) {
        repository.update(model)
    }

    private suspend fun getAllFromRoom(): Flow<List<BaseModel>> = withContext(Dispatchers.IO) {
        repository.getAllToObserve()
    }

}

