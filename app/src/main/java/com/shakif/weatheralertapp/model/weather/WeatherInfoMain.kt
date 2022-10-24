package com.shakif.weatheralertapp.model.weather

import io.realm.RealmObject

open class WeatherInfoMain(
    var temp: String? = null,
    var feels_like: String? = null
): RealmObject()