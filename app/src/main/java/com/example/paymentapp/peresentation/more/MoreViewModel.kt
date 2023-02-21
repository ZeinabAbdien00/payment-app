package com.example.paymentapp.peresentation.more

import com.example.paymentapp.data.dataStore.DataStoreImpl
import javax.inject.Inject

class MoreViewModel {
    @Inject
    lateinit var dataStore: DataStoreImpl


    suspend fun isUsePassword():Boolean = dataStore.getUsePassword()

    suspend fun setIsUsePassword(boolean: Boolean) {
        dataStore.setUsePassword(boolean)
    }

    suspend fun isUseNotifications():Boolean = dataStore.getUseNotifications()

    suspend fun setIsUseNotifications(boolean: Boolean){
        dataStore.setUseNotifications(boolean)
    }

    suspend fun getPassword():String = dataStore.getPassword()

    suspend fun changePassword(newPassword: String){
        dataStore.setPassword(newPassword)
    }
}