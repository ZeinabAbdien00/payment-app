package com.example.paymentapp.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar

class BootReceiver :BroadcastReceiver() {
    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            setNotificationAlarm(context)
        }
    }

    private fun setNotificationAlarm(myContext: Context) {
        alarmMgr = myContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(myContext, NotificationReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(myContext, 0, intent, 0)
        }
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 5)
        }
        alarmMgr?.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            alarmIntent
        )
    }

}