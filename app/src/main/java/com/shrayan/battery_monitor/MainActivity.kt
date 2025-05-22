package com.shrayan.battery_monitor

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.shrayan.battery_monitor.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var receiver: BatteryReceiver

    private val sharedPreferences by lazy { getSharedPreferences(BATTERY_MONITOR, MODE_PRIVATE) }

    private var threshold: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        threshold = sharedPreferences.getInt(THRESHOLD, 0)
        if (threshold != 0) {
            binding.seekbar.progress = threshold
            binding.tvThreshold.text = "$threshold%"
        }

        binding.seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekbar: SeekBar?, progress: Int, p2: Boolean) {
                threshold = progress
                binding.tvThreshold.text = "$threshold%"
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                // Do nothing
            }

            override fun onStopTrackingTouch(seekbar: SeekBar?) {
                sharedPreferences.edit {
                    putInt(THRESHOLD, threshold)
                }
            }

        })

        receiver = BatteryReceiver()
        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        registerReceiver(receiver, filter)

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

}