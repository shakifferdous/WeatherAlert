package com.shakif.weatheralertapp.model.weather

import io.realm.RealmObject

open class CityInfo(
    var name: String? = null,
    var country: String? = null
): RealmObject()