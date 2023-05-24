package com.example.safevaletcaptain.api

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.MediaPlayer
import android.media.MediaSession2Service
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.safevaletcaptain.HomeActivity
import com.example.safevaletcaptain.MainActivity
import com.example.safevaletcaptain.R
import com.example.safevaletcaptain.helpers.HelperUtils
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

//import com.huawei.hms.push.HmsMessageService
//import com.huawei.hms.push.RemoteMessage

class MyFirebaseMessagingService :FirebaseMessagingService() {


    //private Long timeOutDuration = null;

    //private Long timeOutDuration = null;
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

//        /* if (remoteMessage.getData().get("duration") != null)
////            timeOutDuration = Long.parseLong(Objects.requireNonNull(remoteMessage.getData().get("duration")));*/MediaSession2Service.MediaNotification(
//            applicationContext, R.raw.annoying
//        )
//        final MediaPlayer mp = MediaPlayer.create(this, R.raw.soho);
val mp = MediaPlayer.create(applicationContext,R.raw.annoying)
mp.start()
        //MediaNotification.setTimeOut(timeOutDuration);
//        showNotify(
//            remoteMessage.data["titleX"],
//            remoteMessage.data["messageX"],
//            remoteMessage.data["action"],
//            remoteMessage.data["switch"],
//            remoteMessage.data["switch_to"]
//        )
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun showNotify(
        title: String?,
        body: String?,
        key: String?,
        switchState: String?,
        carSwitchId: String?
    ) {
        val mBuilder = NotificationCompat.Builder(
            applicationContext, "GLTRC_ID"
        )
        val ii = Intent(applicationContext, MainActivity::class.java)
        ii.putExtra("action", key)
        ii.putExtra("switch_state", switchState)
        ii.putExtra("switch_to_car", carSwitchId)
        //TODO test it
        //pending intent flag for mutability
        val flag: Int
        flag =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else PendingIntent.FLAG_UPDATE_CURRENT
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, ii, flag)
        val bigText = NotificationCompat.BigTextStyle()
        bigText.bigText(body)
        bigText.setBigContentTitle(title)
        bigText.setSummaryText("إشعار")
        mBuilder.setContentIntent(pendingIntent)
//        mBuilder.setSmallIcon(R.drawable.alert)
        mBuilder.setContentTitle(title)
        mBuilder.setContentText(body)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) mBuilder.priority =
            NotificationManager.IMPORTANCE_HIGH else mBuilder.priority =
            Notification.PRIORITY_MAX
        mBuilder.setStyle(bigText)
        mBuilder.setAutoCancel(true)


        /*if (duration != null)
            mBuilder.setTimeoutAfter(duration);*/
        val mNotificationManager = getSystemService(
            NOTIFICATION_SERVICE
        ) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "Your_channel_id"
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_HIGH
            )
            mNotificationManager.createNotificationChannel(channel)
            mBuilder.setChannelId(channelId)
        }
//        mNotificationManager.notify(0, mBuilder.build())
    }

    override fun onNewToken(s: String) {
        super.onNewToken(s)
        val preferences = getSharedPreferences(HelperUtils.SHARED_PREF, MODE_PRIVATE)
        if (preferences.contains("uid")) {
            val editor = preferences.edit()
            editor.clear()
            editor.apply()
        }
    }
}