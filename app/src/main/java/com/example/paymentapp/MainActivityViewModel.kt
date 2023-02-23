package com.example.paymentapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel : ViewModel() {

    var isFirstTimeSplash : MutableLiveData<Boolean> = MutableLiveData(true)
    var isFirstTimePassword : MutableLiveData<Boolean> = MutableLiveData(true)
}