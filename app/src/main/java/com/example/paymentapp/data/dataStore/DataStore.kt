package com.example.paymentapp.data.dataStore

interface DataStore {

    suspend fun setUsePassword(boolean: Boolean)

    suspend fun getUsePassword(): Boolean

    suspend fun setUseNotifications(boolean: Boolean)

    suspend fun getUseNotifications(): Boolean

    suspend fun setPassword(string: String)

    suspend fun getPassword(): String

}