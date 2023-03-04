package com.example.paymentapp.data.dataStore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext


private val Context.dataStore by preferencesDataStore("app_data")

class DataStoreImpl (
    appContext: Context,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : DataStore {

    private val mDataStore by lazy {
        appContext.dataStore
    }

    companion object {
        const val USE_PASSWORD = "usePassword"
        const val USE_NOTIFICATIONS = "userNotifications"
        const val PASSWORD = "password"
    }

    override suspend fun setUsePassword(boolean: Boolean) {
        mDataStore.edit { settings ->
            settings[booleanPreferencesKey(USE_PASSWORD)] = boolean
        }
    }

    override suspend fun getUsePassword(): Boolean = withContext(dispatcher) {
        mDataStore.data.map { settings ->
            settings[booleanPreferencesKey(USE_PASSWORD)] ?: false
        }.first()
    }

    override suspend fun setUseNotifications(boolean: Boolean) {
        mDataStore.edit { settings ->
            settings[booleanPreferencesKey(USE_NOTIFICATIONS)] = boolean
        }    }

    override suspend fun getUseNotifications(): Boolean = withContext(dispatcher) {
        mDataStore.data.map { settings ->
            settings[booleanPreferencesKey(USE_NOTIFICATIONS)] ?: true
        }.first()
    }

    override suspend fun setPassword(string: String) {
        mDataStore.edit { settings ->
            settings[stringPreferencesKey(PASSWORD)] = string
        }    }

    override suspend fun getPassword(): String = withContext(dispatcher) {
        mDataStore.data.map { settings ->
            settings[stringPreferencesKey(PASSWORD)] ?: "0000"
        }.first()
    }


}