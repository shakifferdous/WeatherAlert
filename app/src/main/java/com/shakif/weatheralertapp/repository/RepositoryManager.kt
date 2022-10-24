package com.shakif.weatheralertapp.repository

import com.shakif.weatheralertapp.repository.weather.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryManager {

    @Provides
    @Singleton
    fun getWeatherRepository(): WeatherRepository {
        return WeatherRepository()
    }
}