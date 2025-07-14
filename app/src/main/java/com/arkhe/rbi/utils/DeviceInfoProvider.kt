package com.arkhe.rbi.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings

class DeviceInfoProvider(private val context: Context) {
    fun getDeviceInfo(): DeviceInfo {
        return DeviceInfo(
            imei = getIMEI(),
            brand = Build.MANUFACTURER,
            type = Build.MODEL,
            osVersion = "${Build.VERSION.RELEASE} ( SDK ${Build.VERSION.SDK_INT})",
            osName = "Android"
        )
    }

    @SuppressLint("HardwareIds")
    private fun getIMEI(): String {
        return try {
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        } catch (e: Exception) {
            "unknown"
        }
    }
}

data class DeviceInfo(
    val imei: String,
    val brand: String,
    val type: String,
    val osVersion: String,
    val osName: String
)