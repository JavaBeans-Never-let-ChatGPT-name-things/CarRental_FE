package com.example.carrental_fe.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import com.example.carrental_fe.CarRentalApplication
import com.example.carrental_fe.MainActivity
import com.example.carrental_fe.R
import com.example.carrental_fe.data.TokenManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FirebaseService: FirebaseMessagingService() {
    private val tokenManager by lazy { TokenManager(applicationContext) }
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("Token Firebase", token)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                tokenManager.saveDeviceToken(token)
                Log.d("Token Firebase", "Token saved successfully")
            } catch (e: Exception) {
                Log.e("Token Firebase", e.message.toString())
            }
        }
    }
    @RequiresPermission(android.Manifest.permission.POST_NOTIFICATIONS)
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.v("CloudMessage", "From ${message.from}")

        //Log Data Payload
        if (message.data.isNotEmpty()) {
            Log.v("CloudMessage", "Message Data ${message.data}")
        }

        //Check if message contains a notification payload

        message.notification?.let { message ->
            sendNotification(message)
            sendBroadcastMessage(message)
        }
        if (message.notification != null) {

            Log.v("CloudMessage", "Notification ${message.notification}")
            Log.v("CloudMessage", "Notification Title ${message.notification!!.title}")
            Log.v("CloudMessage", "Notification Body ${message.notification!!.body}")

        }

    }
    private fun sendBroadcastMessage(message: RemoteMessage.Notification) {
        val intent = Intent("com.example.carrental_fe.NEW_MESSAGE").apply {
            putExtra("message", message.body)
            putExtra("title", message.title)
            putExtra("imageUrl", message.imageUrl.toString())
            setPackage("com.example.carrental_fe")
        }

        sendBroadcast(intent)
    }
    private fun sendNotification(message: RemoteMessage.Notification) {
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(FLAG_ACTIVITY_CLEAR_TOP)
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, FLAG_IMMUTABLE
        )

        val channelId = this.getString(R.string.default_notification_channel_id)
        val channelName = this.getString(R.string.default_notification_channel_id)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(message.title)
            .setContentText(message.body)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(channelId, channelName, IMPORTANCE_HIGH)
        manager.createNotificationChannel(channel)

        manager.notify(Random.nextInt(), notificationBuilder.build())
    }
}