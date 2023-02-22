package com.example.paymentapp.peresentation.more

import androidx.lifecycle.ViewModel
import com.example.paymentapp.data.dataStore.DataStoreImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MoreViewModel @Inject constructor(): ViewModel() {

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