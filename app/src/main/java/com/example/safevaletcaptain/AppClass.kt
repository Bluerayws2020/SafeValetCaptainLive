package com.example.safevaletcaptain

import android.app.Application
import com.onesignal.OneSignal
import com.onesignal.OneSignalNotificationManager

private const val ONESIGNAL_APP_ID = "7d04e67f-c575-4c00-8e16-78c9358c8469"

class AppClass : Application() {
    override fun onCreate() {
        super.onCreate()
        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization

        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)
    }
}