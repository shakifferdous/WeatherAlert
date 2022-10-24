package com.shakif.weatheralertapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shakif.weatheralertapp.model.geocode.Location
import com.shakif.weatheralertapp.model.geocode.SelectedLocation
import com.shakif.weatheralertapp.model.geocode.viewdata.*
import com.shakif.weatheralertapp.model.weather.Weather
import com.shakif.weatheralertapp.model.weather.WeatherInfo
import com.shakif.weatheralertapp.repository.weather.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel@Inject constructor(private val repository: WeatherRepository): ViewModel() {
    private val suggestionsList = MutableLiveData<List<LocationSuggestionType>>().also { it.value = listOf() }
    val suggestionsLiveData: LiveData<List<LocationSuggestionType>> = suggestionsList

    private val locationSearchData = MutableLiveData<Pair<String, List<LocationViewData>>>().also { it.value = Pair("", listOf()) }
    val searchLiveData: LiveData<Pair<String, List<LocationViewData>>> = locationSearchData

    private val selectedLocationData = MutableLiveData<SelectedLocation>().also { it.value = SelectedLocation() }
    val selectedLocationLiveData: LiveData<SelectedLocation> = selectedLocationData

    private val isLoading = MutableLiveData<Boolean>().also { it.value = false }
    val loadingLiveData: LiveData<Boolean> = isLoading

    private val errorData = MutableLiveData<String>().also { it.value = "" }
    val errorLiveData: LiveData<String> = errorData

    private val currentWeather = MutableLiveData<WeatherInfo>().also { it.value = WeatherInfo() }
    val currentWeatherLiveData: LiveData<WeatherInfo> = currentWeather

    private val forecast = MutableLiveData<Weather>().also { it.value = Weather() }
    val forecastLiveData: LiveData<Weather> = forecast

    init {
        repository.getSelectedLocation()?.let {
            selectedLocationData.value = it
            fetchCurrentWeather()
            fetchForecast()
        }
    }

    fun loadSuggestions() {
        val list = mutableListOf<LocationSuggestionType>()
        list.add(HintViewData(title = "Get weather info for current location"))
        list.add(LocationPickerViewData(title = "Current Location"))
        list.add(HintViewData(title = "or Search for a city"))
        list.add(LocationPickerViewData(title = "Search City", searchType = true))
        list.add(HintViewData("Suggested Cities"))
        list.add(SuggestedLocationViewData("Dhaka", 23.8103, 90.4125))
        list.add(SuggestedLocationViewData("Tokyo", 35.652832, 139.839478))
        list.add(SuggestedLocationViewData("Beijing", 39.916668, 116.383331))
        list.add(SuggestedLocationViewData("London", 51.5072, 0.1276))
        list.add(SuggestedLocationViewData("Chittagong", 22.3569, 91.7832))
        list.add(SuggestedLocationViewData("New york", 40.7128, 74.0060))

        suggestionsList.value = list
    }

    fun search(query: String) {
        repository.fetchLocation(query) { data, apiError ->
            apiError?.let {
                if (it.message().isNotBlank()) errorData.value = it.message()
                clearError()
            }
            data?.let { list ->
                val viewDataList = mutableListOf<LocationViewData>()
                list.forEach {
                    viewDataList.add(LocationViewData(
                        it.name ?: "",
                        it.lat ?: 0.0,
                        it.lon ?: 0.0,
                    it.country ?: "",
                    it.state ?: ""))
                }
                locationSearchData.value = Pair(query, viewDataList)
            }
        }
    }

    fun fetchLocation(lat: Double, lon: Double) {
        isLoading.value = true
        repository.fetchLatLonLocation(lat, lon) { list, apiError ->
            apiError?.let {
                isLoading.value = false
                if (it.message().isNotBlank()) errorData.value = it.message()
                clearError()
            }
            list?.let {
                it.firstOrNull()?.let { data ->
                    updateSelectedLocation(data)
                    fetchCurrentWeather()
                    fetchForecast()
                } ?: kotlin.run { isLoading.value = false }
            }
        }
    }

    fun saveSelectedLocation(location: LocationViewData) {
        val data = Location(location.name, location.lat, location.lon, location.country)
        updateSelectedLocation(data)
    }

    private fun updateSelectedLocation(data: Location) {
        repository.saveSelectedLocation(data)
        selectedLocationData.value = SelectedLocation(place = data)
    }

    fun fetchCurrentWeather() {
        val location = selectedLocationData.value?.place
        location?.let {
            isLoading.value = true
            repository.fetchCurrentWeather(it.lat, it.lon) { data, apiError ->
                isLoading.value = false
                apiError?.let { error ->
                    if (error.message().isNotBlank()) errorData.value = error.message()
                    clearError()
                    repository.getCurrentWeatherInfo()?.let { currentWeather.value = it }
                }
                data?.let { weather ->
                    currentWeather.value = weather
                    repository.saveCurrentWeatherInfo(weather)
                }
            }
        }
    }

    fun fetchForecast() {
        val location = selectedLocationData.value?.place
        location?.let {
            isLoading.value = true
            repository.fetchForecast(it.lat, it.lon) { data, apiError ->
                isLoading.value = false
                apiError?.let { error ->
                    if (error.message().isNotBlank()) errorData.value = error.message()
                    clearError()
                    repository.getForecastInfo()?.let { forecast.value = it }
                }
                data?.let { weather ->
                    forecast.value = weather
                    repository.saveForecastInfo(weather)
                }
            }
        }
    }

    fun getBackgroundInfo() = repository.getBackgroundInfo()

    fun lockBackgroundFetch() = repository.lockBackgroundFetch()

    fun lockAlert() = repository.lockAlert()

    private fun clearError() {
        errorData.value = ""
    }
}