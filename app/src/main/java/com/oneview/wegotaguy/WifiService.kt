package com.oneview.wegotaguy

import android.app.*
import android.content.Intent
import android.graphics.Color
import android.os.IBinder
import android.os.PowerManager
import android.provider.Settings
import android.widget.Toast
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*



class WifiService : Service() {

    private var wakeLock: PowerManager.WakeLock? = null
    private var isServiceStarted = false


    override fun onBind(intent: Intent): IBinder? {
        log()
        // We don't provide binding, so return null
        return null
    }

    fun log() {

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        log()
        if (intent != null) {
            val action = intent.action
            log()
            when (action) {
                Actions.START.name -> startService()
                Actions.STOP.name -> stopService()
                else -> log()
            }
        } else {
            log()
        }
        // by returning this we make sure the service is restarted if the system kills the service
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        val notification = createNotification()
        startForeground(1, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this, "Service destroyed", Toast.LENGTH_SHORT).show()
    }

    private fun startService() {
        if (isServiceStarted) return
        log()
        Toast.makeText(this, "Service starting its task", Toast.LENGTH_SHORT).show()
        isServiceStarted = true
        setServiceState(this, ServiceState.STARTED)

        // we need this lock so our service gets not affected by Doze Mode
        wakeLock =
            (getSystemService(POWER_SERVICE) as PowerManager).run {
                newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "ADTWAKELOCK::lock").apply {
                    acquire()
                }
            }

        // we're starting a loop in a coroutine
        GlobalScope.launch(Dispatchers.IO) {
            while (isServiceStarted) {
                launch(Dispatchers.IO) {
                    pingFakeServer()
                }
                delay(1 * 60 * 500)
            }
            log()
        }
    }

    private fun stopService() {
        log()
        Toast.makeText(this, "Service stopping", Toast.LENGTH_SHORT).show()
        try {
            wakeLock?.let {
                if (it.isHeld) {
                    it.release()
                }
            }
            stopSelf()
        } catch (e: Exception) {
            log()
        }
        isServiceStarted = false
        setServiceState(this, ServiceState.STOPPED)
    }


    private fun pingFakeServer() {
        val df = SimpleDateFormat("yyyy-MM-DD'T'HH:mm:ss.mmmZ")
        val gmtTime = df.format(Date())

        val deviceId = Settings.Secure.getString(applicationContext.contentResolver, Settings.Secure.ANDROID_ID)

        val json =
            """
                {
                    "deviceId": "$deviceId",
                    "createdAt": "$gmtTime"
                }
            """
        try {
            Fuel.post("https://jsonplaceholder.typicode.com/posts")
                .jsonBody(json)
                .response { _, _, result ->
                    val (bytes, _) = result
                    if (bytes != null) {
                        log()
                    } else {
                        log()
                    }
                }
        } catch (e: Exception) {
            log()
        }
    }


    private fun createNotification(): Notification {
        val notificationChannelId = "WifiServiceChannel"

        // depending on the Android API that we're dealing with we will have
        // to use a specific method to create the notification
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            notificationChannelId,
            "ADTWAKELOCK notifications channel",
            NotificationManager.IMPORTANCE_HIGH
        ).let {
            it.description = "ADTWAKELOCK Service channel"
            it.enableLights(true)
            it.lightColor = Color.RED
            it.enableVibration(true)
            it.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            it
        }
        notificationManager.createNotificationChannel(channel)

        val pendingIntent: PendingIntent = Intent(this, MainActivity::class.java).let { notificationIntent ->
            PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)
        }

        val builder: Notification.Builder = Notification.Builder(
            this,
            notificationChannelId
        )

        return builder
            .setContentTitle("ADTWAKELOCKSERVICE")
            .setContentText("This is your favorite SWISS ARMY SCAB WITH THE CLUTCH AGAIN")
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.dcb)
            .setTicker("Ticker text")
            .build()
    }
}
