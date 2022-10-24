package com.shakif.weatheralertapp.model.geocode.viewdata

data class LocationViewData(
    var name: String = "",
    var lat: Double = 0.0,
    var lon: Double = 0.0,
    var country: String = "",
    var state: String = ""
)