package com.example.carrental_fe

import android.app.Application
import com.example.carrental_fe.data.AppContainer
import com.example.carrental_fe.data.DefaultAppContainer

class CarRentalApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}