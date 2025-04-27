package com.example.carrental_fe

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import com.example.carrental_fe.data.AppContainer
import com.example.carrental_fe.data.DefaultAppContainer

class CarRentalApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
        createNotificationChannel()
    }
    private fun createNotificationChannel(){
        val name = "Car Rental"
        val description ="Car Rental Notification"
        val importance = NotificationManager.IMPORTANCE_DEFAULT

        //Now Create Notification Channel.
        // it take three parameters. notification id,name, and importance.
        val channel = NotificationChannel("Global",name,importance)
        channel.description = description;

        // Get Notification Manager.
        val notificationManager : NotificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        //Lets Create Notification channel.
        notificationManager.createNotificationChannel(channel)

    }
}