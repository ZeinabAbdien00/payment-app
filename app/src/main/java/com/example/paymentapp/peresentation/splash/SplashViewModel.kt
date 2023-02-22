package com.example.paymentapp.peresentation.splash

import androidx.lifecycle.ViewModel
import com.example.paymentapp.data.dataStore.DataStoreImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(): ViewModel() {
    @Inject
    lateinit var dataStore: DataStoreImpl

    suspend fun isUsePassword():Boolean = dataStore.getUsePassword()

}