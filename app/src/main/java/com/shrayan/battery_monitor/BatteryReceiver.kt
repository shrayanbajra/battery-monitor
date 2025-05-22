package com.shrayan.battery_monitor

import android.content.ActivityNotFoundException
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.BatteryManager
import android.widget.Toast

class BatteryReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        if (context == null || intent == null) {
            return
        }

        val sharedPreferences = context.getSharedPreferences(BATTERY_MONITOR, MODE_PRIVATE)
        val threshold = sharedPreferences?.getInt(THRESHOLD, 0) ?: 0

        val batteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)

        if (batteryLevel in 0..threshold) {

            val launchIntent = Intent().apply {
                component = ComponentName(
                    "com.shrayan.message_display",
                    "com.shrayan.message_display.MainActivity"
                )
                putExtra(BATTERY_LEVEL, batteryLevel)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }

            try {
                context.startActivity(launchIntent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(context, "Message app not installed", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }

        }

    }

}