package com.shakif.weatheralertapp.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.shakif.weatheralertapp.repository.weather.WeatherRepository
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class BackgroundFetchBroadcastReceiver: BroadcastReceiver() {
    @Inject
    lateinit var repository: WeatherRepository

    override fun onReceive(context: Context?, intent: Intent?) {
        val selectedLocation = repository.getSelectedLocation()
        selectedLocation?.let {
            repository.fetchForecast(it.place?.lat, it.place?.lon) { data, _ ->
                data?.let { weather ->
                    repository.saveForecastInfo(weather)
                    repository.saveLastBackgroundFetchTime(Date())
                }
            }

            repository.fetchCurrentWeather(it.place?.lat, it.place?.lon) { data, _ ->
                data?.let { info -> repository.saveCurrentWeatherInfo(info) }
            }
        }
    }
}