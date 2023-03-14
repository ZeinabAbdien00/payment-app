package com.example.paymentapp.peresentation.password

import androidx.lifecycle.ViewModel
import com.example.paymentapp.data.dataStore.DataStoreImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PasswordViewModel @Inject constructor() : ViewModel() {
    @Inject
    lateinit var dataStore: DataStoreImpl


    suspend fun getPassword(): String = dataStore.getPassword()


}