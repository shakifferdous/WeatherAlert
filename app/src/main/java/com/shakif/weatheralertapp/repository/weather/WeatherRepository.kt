package com.shakif.weatheralertapp.repository.weather

import com.shakif.weatheralertapp.model.geocode.Location
import com.shakif.weatheralertapp.model.weather.Weather
import com.shakif.weatheralertapp.model.weather.WeatherInfo
import com.shakif.weatheralertapp.network.APIError
import com.shakif.weatheralertapp.network.genericAPICallback
import java.util.*

class WeatherRepository {
    private val apiService by lazy { WeatherApiService }
    private val dao by lazy { WeatherDao }

    fun fetchLocation(query: String, completion: (List<Location>?, APIError?) -> Unit) =
        apiService.get().fetchLocation(query).enqueue(genericAPICallback(completion))

    fun fetchLatLonLocation(lat: Double?, lon: Double?, completion: (List<Location>?, APIError?) -> Unit) =
        apiService.get().fetchLocationFromLatLon(lat, lon).enqueue(genericAPICallback(completion))

    fun fetchCurrentWeather(lat: Double?, lon: Double?, completion: (WeatherInfo?, APIError?) -> Unit) =
        apiService.get().fetchCurrentWeather(lat, lon).enqueue(genericAPICallback(completion))

    fun fetchForecast(lat: Double?, lon: Double?, completion: (Weather?, APIError?) -> Unit) =
        apiService.get().fetchWeatherForecast(lat, lon).enqueue(genericAPICallback(completion))

    fun saveSelectedLocation(location: Location) = dao.saveSelectedLocation(location)

    fun getSelectedLocation() = dao.getSelectedLocation()

    fun saveCurrentWeatherInfo(info: WeatherInfo) = dao.saveWeatherInfo(info)

    fun getCurrentWeatherInfo() = dao.getWeatherInfo()

    fun saveForecastInfo(weather: Weather) = dao.saveForecast(weather)

    fun getForecastInfo() = dao.getForecast()

    fun getBackgroundInfo() = dao.getBackgroundInfo()

    fun saveLastRainAlertTime(date: Date) = dao.updateLatestRainAlertTime(date)

    fun saveLastBackgroundFetchTime(date: Date) = dao.updateLatestBackgroundFetchTime(date)

    fun lockBackgroundFetch() = dao.lockBackgroundFetch()

    fun lockAlert() = dao.lockRainAlert()
}