package com.oneview.wegotaguy

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

class StartReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED && getServiceState(context) == ServiceState.STARTED) {
            Intent(context, WifiService::class.java).also {
                it.action = Actions.START.name
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    log()
                    context.startForegroundService(it)
                    return
                }
                log()
                context.startService(it)
            }
        }
    }
    private fun log() {
        if (BuildConfig.DEBUG) {
            println("StartReceiver")
        }
    }
}