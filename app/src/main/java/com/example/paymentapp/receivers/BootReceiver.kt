package com.example.paymentapp.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.paymentapp.data.source.notification.alarmManger.NotificationReceiver

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            NotificationReceiver.startAlarm(context = context)
        }
    }
}