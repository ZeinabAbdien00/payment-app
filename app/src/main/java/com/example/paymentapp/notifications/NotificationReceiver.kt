package com.example.paymentapp.notifications

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.paymentapp.MainActivity
import com.example.paymentapp.R
import com.example.paymentapp.data.dataStore.DataStoreImpl
import com.example.paymentapp.data.models.BaseModel
import com.example.paymentapp.data.repositories.BaseRepository
import com.example.paymentapp.data.source.homeDatabase.HomeDataBase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@AndroidEntryPoint
class NotificationReceiver : BroadcastReceiver() {

    @Inject
    lateinit var dataStore: DataStoreImpl

    private lateinit var repository: BaseRepository

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("mohamed", "onReceive: ff")
        GlobalScope.launch{
            updateRoomData()
            val dao = HomeDataBase.getInstance(context).myDao()
            repository = BaseRepository(dao)
            val useNotifications = dataStore.getUseNotifications()
            if (useNotifications) {
                createNotification(context, "العملاء اليوم", todayData(), 123)
            }
        }
    }

    private fun updateRoomData() {
        GlobalScope.launch {
            val namedData = getAllFromRoom()
            for (i in namedData) {
                i.customInit()
                updateModel(i)
            }
        }
    }

    private fun createNotification(
        context: Context,
        title: String?,
        message: String?,
        reqCode: Int
    ) {

        val intent = Intent(context, MainActivity::class.java)

        val pendingIntent: PendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE)
        } else {
            PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_ONE_SHOT
            )
        }

        val notification = NotificationCompat.Builder(context, "AB Motors")
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.app_icon)
            .setContentIntent(pendingIntent)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(reqCode, notification.build())
    }

    private suspend fun todayData(): String {
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val namedData = getAllFromRoom().filter { it.monthlyDayOfPaying == day.toString() }
            .map { it.name } as ArrayList
        var stringData = ""
        for (i in namedData) {
            stringData += i + "\n"
        }
        if (stringData.isEmpty()) stringData = "لا يوجد عملاء اليوم"
        return stringData
    }

    private suspend fun getAllFromRoom(): List<BaseModel> = withContext(Dispatchers.IO) {
        repository.getAllToObserve().first()
    }

    private suspend fun updateModel(model: BaseModel) = withContext(Dispatchers.IO) {
        repository.update(model)
    }


}