package com.shakif.weatheralertapp.model.geocode

import io.realm.RealmObject

open class SelectedLocation(
    var place: Location? = null
): RealmObject()