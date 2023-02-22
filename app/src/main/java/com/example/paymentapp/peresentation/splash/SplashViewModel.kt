package com.example.paymentapp.peresentation.splash

import androidx.lifecycle.ViewModel
import com.example.paymentapp.data.dataStore.DataStoreImpl
import javax.inject.Inject

class SplashViewModel : ViewModel() {
    @Inject
    lateinit var dataStore: DataStoreImpl

    suspend fun isUsePassword():Boolean = dataStore.getUsePassword()

}