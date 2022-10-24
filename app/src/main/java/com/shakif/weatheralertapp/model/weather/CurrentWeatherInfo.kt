package com.shakif.weatheralertapp.model.weather

import io.realm.RealmObject

open class CurrentWeatherInfo(
    var weatherInfo: WeatherInfo? = null
): RealmObject()