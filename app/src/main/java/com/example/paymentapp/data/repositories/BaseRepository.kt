package com.example.paymentapp.data.repositories

import com.example.paymentapp.data.models.BaseModel
import com.example.paymentapp.data.source.homeDatabase.HomeDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BaseRepository(
private val myDao: HomeDao
) {

    suspend fun getAll(): List<BaseModel> = withContext(Dispatchers.IO) {
        myDao.getAll()
    }

    suspend fun insert(modelClass: BaseModel) {
        myDao.insertAll(modelClass)
    }

    suspend fun delete(modelClass: BaseModel) {
        myDao.delete(modelClass)
    }

    suspend fun deleteAll() {
        myDao.deleteAll()
    }

    suspend fun update(modelClass: BaseModel) {
        myDao.update(modelClass)
    }

    suspend fun deleteById(id: Int) = myDao.deleteById(id)
}