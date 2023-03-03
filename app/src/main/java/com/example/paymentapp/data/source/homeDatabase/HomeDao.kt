package com.example.paymentapp.data.source.homeDatabase

import androidx.room.*
import com.example.paymentapp.data.models.BaseModel
import kotlinx.coroutines.flow.Flow

@Dao
interface HomeDao {
    @Query("SELECT * FROM BaseModel")
    fun getAllToObserve(): Flow<List<BaseModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg dataModel: BaseModel)

    @Query("delete from BaseModel")
    fun deleteAll()

    @Delete
    fun delete(dataModel: BaseModel)

    @Query("DELETE FROM BaseModel WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Update
    fun update(dataModel: BaseModel)

}