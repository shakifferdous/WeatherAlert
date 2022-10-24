package com.shakif.weatheralertapp.repository.weather

import com.shakif.weatheralertapp.model.backgroundinfo.BackgroundOperations
import com.shakif.weatheralertapp.model.geocode.Location
import com.shakif.weatheralertapp.model.geocode.SelectedLocation
import com.shakif.weatheralertapp.model.weather.CurrentWeatherInfo
import com.shakif.weatheralertapp.model.weather.ForecastInfo
import com.shakif.weatheralertapp.model.weather.Weather
import com.shakif.weatheralertapp.model.weather.WeatherInfo
import com.shakif.weatheralertapp.realm.DBManager
import java.util.*

object WeatherDao {
    fun saveSelectedLocation(location: Location) {
        DBManager.deleteAll(SelectedLocation::class.java)
        val selectedLocation = SelectedLocation(place = location)
        DBManager.save(selectedLocation)
    }

    fun getSelectedLocation(): SelectedLocation? {
        var location: SelectedLocation? = null
        DBManager.getRealm().executeTransaction { realm ->
            realm.where(SelectedLocation::class.java).findFirst()?.let {
                location = realm.copyFromRealm(it)
            }
        }
        return location
    }

    fun saveWeatherInfo(weather: WeatherInfo) {
        DBManager.deleteAll(CurrentWeatherInfo::class.java)
        DBManager.save(CurrentWeatherInfo(weather))
    }

    fun getWeatherInfo(): WeatherInfo? {
        var weather: WeatherInfo? = null
        DBManager.getRealm().executeTransaction { realm ->
            realm.where(CurrentWeatherInfo::class.java).findFirst()?.weatherInfo?.let {
                weather = realm.copyFromRealm(it)
            }
        }
        return weather
    }

    fun saveForecast(weather: Weather) {
        DBManager.deleteAll(ForecastInfo::class.java)
        DBManager.save(ForecastInfo(weather))
    }

    fun getForecast(): Weather? {
        var weather: Weather? = null
        DBManager.getRealm().executeTransaction { realm ->
            realm.where(ForecastInfo::class.java).findFirst()?.weather?.let {
                weather = realm.copyFromRealm(it)
            }
        }
        return weather
    }

    fun getBackgroundInfo(): BackgroundOperations? {
        var backgroundData: BackgroundOperations? = null
        DBManager.getRealm().executeTransaction { realm ->
            realm.where(BackgroundOperations::class.java).findFirst()?.let {
                backgroundData = realm.copyFromRealm(it)
            }
        }
        return backgroundData
    }

    fun updateLatestRainAlertTime(time: Date) {
        DBManager.getRealm().executeTransaction { realm ->
            realm.where(BackgroundOperations::class.java).findFirst()?.let {
                it.lastRainAlertTime = time
                it.rainAlertLock = false
            } ?: kotlin.run {
                val data = BackgroundOperations()
                data.lastRainAlertTime = time
                data.rainAlertLock = false
                realm.copyToRealm(data)
            }
        }
    }

    fun updateLatestBackgroundFetchTime(time: Date) {
        DBManager.getRealm().executeTransaction { realm ->
            realm.where(BackgroundOperations::class.java).findFirst()?.let {
                it.lastWeatherFetchTime = time
                it.backgroundFetchLock = false
            } ?: kotlin.run {
                val data = BackgroundOperations()
                data.lastWeatherFetchTime = time
                data.backgroundFetchLock = false
                realm.copyToRealm(data)
            }
        }
    }

    fun lockBackgroundFetch() {
        DBManager.getRealm().executeTransaction { realm ->
            realm.where(BackgroundOperations::class.java).findFirst()?.let {
                it.backgroundFetchLock = true
            } ?: kotlin.run {
                val data = BackgroundOperations()
                data.backgroundFetchLock = true
                realm.copyToRealm(data)
            }
        }
    }

    fun lockRainAlert() {
        DBManager.getRealm().executeTransaction { realm ->
            realm.where(BackgroundOperations::class.java).findFirst()?.let {
                it.rainAlertLock = true
            } ?: kotlin.run {
                val data = BackgroundOperations()
                data.rainAlertLock = true
                realm.copyToRealm(data)
            }
        }
    }
}