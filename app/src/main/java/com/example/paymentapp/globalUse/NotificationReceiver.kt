package com.example.paymentapp.data.source.notification.alarmManger

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
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
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit
import javax.inject.Inject


@AndroidEntryPoint
class NotificationReceiver : BroadcastReceiver() {

    @Inject
    lateinit var dataStore: DataStoreImpl

    private lateinit var repository: BaseRepository

    companion object {
        private const val REQUEST_TIMER1 = 1
        private fun getIntent(context: Context, requestCode: Int): PendingIntent? {
            val intent = Intent(context, NotificationReceiver::class.java)


            return PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        }

        fun startAlarm(context: Context) {

            val pendingIntent = getIntent(context, REQUEST_TIMER1)
            val alarm = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val alarmTime = LocalTime.of(17, 35)

            var now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)
            if (now.toLocalTime().isAfter(alarmTime)) {
                now = now.plusDays(1)
            }

            now = now.withHour(alarmTime.hour)
                .withMinute(alarmTime.minute)

            val utc = now.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneOffset.UTC)
                .toLocalDateTime()
            val triggerAtMillis = utc.atZone(ZoneOffset.UTC)!!.toInstant()!!.toEpochMilli()

            alarm.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        }

        fun cancelAlarm(context: Context) {
            val pendingIntent = getIntent(context, REQUEST_TIMER1)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
        }
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

    override fun onReceive(context: Context, intent: Intent) {
        GlobalScope.launch {

            laterCustomInit()
            val dao = HomeDataBase.getInstance(context).myDao()
            repository = BaseRepository(dao)
            val useNotifications = dataStore.getUseNotifications()
            if (useNotifications) {
                createNotification(context, "العملاء اليوم", todayData(), 123)
            }
        }
    }

    private fun laterCustomInit() {

        GlobalScope.launch {
            val namedData = getAllFromRoom()
            for (i in namedData) {
                i.customInit()
                updateModel(i)
            }

        }
    }

    private suspend fun updateModel(model: BaseModel) = withContext(Dispatchers.IO) {
        repository.update(model)
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

}