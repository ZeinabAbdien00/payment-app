package com.example.paymentapp.globalUse

import android.app.Application
import com.example.paymentapp.data.dataStore.DataStoreImpl
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyApp : Application() {


    @Inject
    lateinit var dataStore: DataStoreImpl

    companion object {
        lateinit var instance: MyApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

}