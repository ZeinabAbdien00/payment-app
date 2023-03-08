package com.example.paymentapp

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.example.paymentapp.data.dataStore.DataStoreImpl
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyApp : Application() {

    @Inject
    lateinit var dataStore: DataStoreImpl

    companion object {

        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

        @SuppressLint("StaticFieldLeak")
        lateinit var instance: MyApp
            private set

    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        instance = this
    }

}