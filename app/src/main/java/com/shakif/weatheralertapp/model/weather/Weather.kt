package com.shakif.weatheralertapp.model.weather

import io.realm.RealmList
import io.realm.RealmObject

open class Weather(
    var list: RealmList<WeatherInfo>? = null,
    var city: CityInfo? = null
): RealmObject()