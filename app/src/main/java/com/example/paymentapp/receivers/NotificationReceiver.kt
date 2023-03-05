package com.example.paymentapp.data.source.notification.alarmManger

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.paymentapp.MainActivity
import com.example.paymentapp.R
import com.example.paymentapp.data.dataStore.DataStoreImpl
import com.example.paymentapp.data.models.BaseModel
import com.example.paymentapp.data.repositories.BaseRepository
import com.example.paymentapp.data.source.homeDatabase.HomeDataBase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit
import java.util.*
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

            val alarmTime = LocalTime.of(15, 50)

            var now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)
            if (now.toLocalTime().isAfter(alarmTime)) {
                now = now.plusDays(1)
            }

            now = now.withHour(alarmTime.hour)
                .withMinute(alarmTime.minute)

            val utc = now.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneOffset.UTC)
                .toLocalDateTime()
            val triggerAtMillis = utc.atZone(ZoneOffset.UTC)!!.toInstant()!!.toEpochMilli()

            alarm.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
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
            val dao = HomeDataBase.getInstance(context).myDao()
            repository = BaseRepository(dao)
            Log.d("mohamed", "showNotification: 1")
            laterCustomInit()
            val useNotifications = dataStore.getUseNotifications()
            if (useNotifications) {
                showNotification(context, "العملاء اليوم", todayData(), 123)
            }
        }
        GlobalScope.launch {
            delay(1000*70)
            startAlarm(context)
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


    private fun showNotification(
        context: Context,
        title: String?,
        message: String?,
        reqCode: Int,
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

        val CHANNEL_ID = "main_chanel" // The id of the channel.
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.app_icon)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
      //  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "Main Notification Channel"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            notificationManager.createNotificationChannel(mChannel)
      //  }
        notificationManager.notify(
            reqCode,
            notificationBuilder.build()
        )
        Log.d("mohamed", "showNotification: $reqCode")
    }
}