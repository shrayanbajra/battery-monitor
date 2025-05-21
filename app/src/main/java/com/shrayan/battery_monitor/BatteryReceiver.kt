package com.shrayan.battery_monitor

import android.content.ActivityNotFoundException
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import android.widget.Toast

class BatteryReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        val batteryLevel = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val threshold = 98

        if (batteryLevel in 1..threshold) {

            val launchIntent = Intent().apply {
                component = ComponentName(
                    "com.shrayan.message_display",
                    "com.shrayan.message_display.MainActivity"
                )
                putExtra("battery_level", batteryLevel)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            try {
                context?.startActivity(launchIntent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(context, "Message app not installed", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }

        }

    }

}