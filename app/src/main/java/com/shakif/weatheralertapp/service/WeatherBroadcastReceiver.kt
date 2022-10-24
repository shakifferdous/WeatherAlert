package com.shakif.weatheralertapp.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.shakif.weatheralertapp.repository.weather.WeatherRepository
import com.shakif.weatheralertapp.service.NotificationUtil.Companion.createNotificationNoIntent
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class WeatherBroadcastReceiver: BroadcastReceiver() {
    @Inject
    lateinit var repository: WeatherRepository

    override fun onReceive(context: Context?, intent: Intent?) {
        createNotificationNoIntent("Rain forecasted soon", "Make sure to find a shelter", context)
        repository.saveLastRainAlertTime(Date())
    }
}