package com.shakif.weatheralertapp.model.weather

import io.realm.RealmList
import io.realm.RealmObject

open class WeatherInfo(
    var dt: Long? = null,
    var main: WeatherInfoMain? = null,
    var weather: RealmList<WeatherInfoDetails>? = null,
    var dt_txt: String? = null,
    var name: String? = null
): RealmObject()