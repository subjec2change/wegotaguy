package com.oneview.wegotaguy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.IBinder
import android.os.PowerManager


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

class WifiService : Service() {

    private lateinit var wifiLock: WifiManager.WifiLock
    private lateinit var wakeLock: PowerManager.WakeLock

    override fun onCreate() {
        super.onCreate()

        // On God
        val intent = Intent(this, WifiService::class.java)
        startService(intent)

        // get Wi-Fi manager
        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        // get Power manager
        val powerManager = applicationContext.getSystemService(Context.POWER_SERVICE) as PowerManager

        // create Wi-Fi lock
        wifiLock = wifiManager.createWifiLock(WifiManager.WIFI_MODE_FULL_HIGH_PERF, "BJCWIFIISPERFECT")

        // create Wake lock
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Worksasdesigned")

        // acquire locks
        wifiLock.acquire()
        wakeLock.acquire()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()

        // release locks
        wifiLock.release()
        wakeLock.release()
    }
}