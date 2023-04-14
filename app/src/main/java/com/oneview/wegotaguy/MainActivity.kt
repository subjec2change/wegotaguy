package com.oneview.wegotaguy

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.IBinder
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat


class WifiService : Service() {

    companion object {
        const val NOTIFICATION_ID = 1
        const val NOTIFICATION_CHANNEL_ID = "WifiServiceChannel"
    }
    private lateinit var wifiLock: WifiManager.WifiLock
    private lateinit var wakeLock: PowerManager.WakeLock

    override fun onCreate() {
        super.onCreate()
        // create notification channel if necessary
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            "Wifi Service Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager?.createNotificationChannel(channel)
        // On God
        //val intent = Intent(this, WifiService::class.java)
        //startService(intent)

        val intent = Intent(this, WifiService::class.java)
        ContextCompat.startForegroundService(this, intent)


        // build notification
        val notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Wi-Fi Service")
            .setContentText("Keeping Wi-Fi on while asleep")
            .setSmallIcon(R.drawable.ic_wifi_service)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        // start service in foreground mode
        startForeground(NOTIFICATION_ID, notification)
        // Register the channel with the system
        val notificationManager: NotificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        // get Wi-Fi manager
        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        // get Power manager
        val powerManager = applicationContext.getSystemService(Context.POWER_SERVICE) as PowerManager

        // create Wi-Fi lock
        wifiLock = wifiManager.createWifiLock(WifiManager.WIFI_MODE_FULL_HIGH_PERF, "BJCWIFIISPERFECT")

        // create Wake lock
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "WORKSONMYMACHINE")

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

        // remove notification
        NotificationManagerCompat.from(this).cancel(NOTIFICATION_ID)
    }
}