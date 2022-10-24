package com.shakif.weatheralertapp.model.weather

import io.realm.RealmObject

open class WeatherInfoDetails(
    var id: Int? = null,
    var main: String? = null,
    var description: String? = null,
    var icon: String? = null
): RealmObject()