package com.oneview.wegotaguy

import android.content.Context
import android.content.SharedPreferences

enum class ServiceState {
    STARTED,
    STOPPED,;

    companion object
}

private const val name = "SERVICE_KEY"
private const val key = "SERVICE_STATE"

fun setServiceState(context: Context, state: ServiceState) {
    val sharedPrefs = getPreferences(context)
    sharedPrefs.edit().let {
        it.putString(key, state.name)
        it.apply()
    }
}

fun getServiceState(context: Context): ServiceState {
    val sharedPrefs = getPreferences(context)
    val value = sharedPrefs.getString(key, ServiceState.STOPPED.name)
    return ServiceState.valueOf(value.toString())
}


private fun getPreferences(context: Context): SharedPreferences {
    return context.getSharedPreferences(name, 0)
}