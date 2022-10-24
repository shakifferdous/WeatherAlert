package com.shakif.weatheralertapp.model.weather

import io.realm.RealmObject

open class ForecastInfo(
    var weather: Weather? = null
): RealmObject()