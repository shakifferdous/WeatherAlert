package com.shakif.weatheralertapp.model.backgroundinfo

import io.realm.RealmObject
import java.util.*

open class BackgroundOperations(
    var lastRainAlertTime: Date? = null,
    var lastWeatherFetchTime: Date? = null,
    var rainAlertLock:  Boolean = false,
    var backgroundFetchLock: Boolean = false
): RealmObject()