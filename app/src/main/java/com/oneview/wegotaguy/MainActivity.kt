package com.oneview.wegotaguy

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

    class MainActivity() : AppCompatActivity(), Parcelable {

        constructor(parcel: Parcel) : this()


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            val Actions = Intent(this, WifiService::class.java)
            actionOnService(com.oneview.wegotaguy.Actions.START)

            title = "ADT WAKE LOCK SERVICE"

            findViewById<Button>(R.id.btnStartService).let {
                it.setOnClickListener {
                    log("START THE FOREGROUND SERVICE")
                    actionOnService(com.oneview.wegotaguy.Actions.START)
                }
            }

            findViewById<Button>(R.id.btnStopService).let {
                it.setOnClickListener {
                    log("STOP THE FOREGROUND SERVICE")
                    actionOnService(com.oneview.wegotaguy.Actions.STOP)
                }
            }
        }

        private fun actionOnService(action: Actions) {
            if (getServiceState(this) == ServiceState.STOPPED && action == Actions.STOP) return
            Intent(this, WifiService::class.java).also {
                it.action = action.name
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    log("Starting the service in >=26 Mode")
                    startForegroundService(it)
                    return
                }
                log("Starting the service in < 26 Mode")
                startService(it)
            }
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {

        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<MainActivity> {
            override fun createFromParcel(parcel: Parcel): MainActivity {
                return MainActivity(parcel)
            }

            override fun newArray(size: Int): Array<MainActivity?> {
                return arrayOfNulls(size)
            }
        }
    }
    ///////////////////////////////////////////////////////////////////////////////