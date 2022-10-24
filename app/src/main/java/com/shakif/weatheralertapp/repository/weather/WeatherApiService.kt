package com.shakif.weatheralertapp.repository.weather

import com.shakif.weatheralertapp.model.geocode.Location
import com.shakif.weatheralertapp.model.weather.Weather
import com.shakif.weatheralertapp.model.weather.WeatherInfo
import com.shakif.weatheralertapp.network.NetworkService
import com.shakif.weatheralertapp.utility.StringConstants.APP_ID
import com.shakif.weatheralertapp.utility.StringConstants.GEO_LOCATION_LIMIT
import com.shakif.weatheralertapp.utility.StringConstants.UNIT_METRIC
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.*

object WeatherApiService {
    interface WeatherApi {

        @GET("geo/1.0/direct")
        fun fetchLocation(@Query("q") query: String?,
                          @Query("limit") limit: String = GEO_LOCATION_LIMIT,
                          @Query("appid") appId: String = APP_ID): Call<List<Location>>

        @GET("geo/1.0/reverse")
        fun fetchLocationFromLatLon(@Query("lat") lat: Double?,
                                    @Query("lon") lon: Double?,
                                    @Query("limit") limit: String = GEO_LOCATION_LIMIT,
                                    @Query("appid") appId: String = APP_ID): Call<List<Location>>

        @GET("data/2.5/forecast")
        fun fetchWeatherForecast(@Query("lat") lat: Double?,
                                @Query("lon") lon: Double?,
                                @Query("appid") appId: String = APP_ID,
                                @Query("units") units: String = UNIT_METRIC): Call<Weather>

        @GET("data/2.5/weather")
        fun fetchCurrentWeather(@Query("lat") lat: Double?,
                                @Query("lon") lon: Double?,
                                @Query("appid") appId: String = APP_ID,
                                @Query("units") units: String = UNIT_METRIC): Call<WeatherInfo>
    }

    fun get(): WeatherApi {
        return NetworkService.get(WeatherApi::class.java)
    }
}