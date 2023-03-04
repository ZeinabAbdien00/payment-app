package com.example.paymentapp.data.dataStore

import android.content.Context
import com.example.paymentapp.MyApp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class Module {

    @Provides
    @Singleton
    fun provideApp(): MyApp {
        return MyApp.instance
    }


    @Provides
    @Singleton
    fun provideDataStore(
        @ApplicationContext appContext: Context
    ): DataStoreImpl {
        return DataStoreImpl(appContext)
    }

}