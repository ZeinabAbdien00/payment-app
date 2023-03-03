package com.example.paymentapp.globalUse

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.paymentapp.data.source.notification.alarmManger.NotificationReceiver

class BootReceiver :BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            NotificationReceiver.startAlarm(context = context)
            Toast.makeText(context,"HHH",Toast.LENGTH_SHORT).show()
        }
    }
}