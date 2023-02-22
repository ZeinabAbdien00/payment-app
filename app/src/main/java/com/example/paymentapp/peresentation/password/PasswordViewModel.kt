package com.example.paymentapp.peresentation.password

import androidx.lifecycle.ViewModel
import com.example.paymentapp.data.dataStore.DataStoreImpl
import javax.inject.Inject

class PasswordViewModel : ViewModel() {
    @Inject
    lateinit var dataStore: DataStoreImpl

    suspend fun isUsePassword():Boolean = dataStore.getUsePassword()

    suspend fun getPassword():String = dataStore.getPassword()


}