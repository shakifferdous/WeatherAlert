package com.shakif.weatheralertapp.model.geocode

import io.realm.RealmObject

open class Location(
    var name: String? = null,
    var lat: Double? = null,
    var lon: Double? = null,
    var country: String? = null,
    var state: String? = null
): RealmObject()