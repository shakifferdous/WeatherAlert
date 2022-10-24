package com.shakif.weatheralertapp.model.geocode.viewdata

data class SuggestedLocationViewData(
    var locationName: String = "",
    var lat: Double = 0.0,
    var lon: Double = 0.0
): LocationSuggestionType