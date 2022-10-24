package com.shakif.weatheralertapp.ui.main.activities

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.location.LocationManagerCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.gms.location.*
import com.shakif.weatheralertapp.R
import com.shakif.weatheralertapp.databinding.ActivityMainBinding
import com.shakif.weatheralertapp.model.weather.WeatherInfo
import com.shakif.weatheralertapp.network.NetworkEvent
import com.shakif.weatheralertapp.network.NetworkEvents
import com.shakif.weatheralertapp.service.BackgroundFetchBroadcastReceiver
import com.shakif.weatheralertapp.service.WeatherBroadcastReceiver
import com.shakif.weatheralertapp.ui.base.BaseActivity
import com.shakif.weatheralertapp.ui.main.fragments.LocationSelectionFragment
import com.shakif.weatheralertapp.ui.main.fragments.SearchLocationFragment
import com.shakif.weatheralertapp.ui.main.fragments.WeatherFragment
import com.shakif.weatheralertapp.utility.Colors
import com.shakif.weatheralertapp.utility.datetime.TimeFormat.YYYY_MM_DD_HH_MM_SS
import com.shakif.weatheralertapp.utility.datetime.formatStringToDate
import com.shakif.weatheralertapp.utility.datetime.getFiveMinutesBeforeTime
import com.shakif.weatheralertapp.utility.datetime.getTwelveHourFromTime
import com.shakif.weatheralertapp.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private var binding: ActivityMainBinding? = null

    private val viewModel: MainViewModel by viewModels()

    override fun getLayoutRes() = R.layout.activity_main

    override fun additionalCheckForRelaunch() = viewModel.suggestionsLiveData.value == null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel.loadSuggestions()
        if (locationPermissionAvailable()) {
            showForecastFragment()
        } else {
            showFragment(LocationSelectionFragment(), false)
        }
        observeConnection()
        setObserver()
    }

    private fun showFragment(fragment: Fragment, animate: Boolean = true) {
        with (supportFragmentManager.beginTransaction()) {
            replace(R.id.container, fragment)
            if (animate) {
                setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_out_left, R.anim.slide_in_right)
                addToBackStack(fragment::class.java.simpleName)
            }
            commit()
        }
    }

    fun showForecastFragment() {
        showFragment(WeatherFragment(), false)
    }

    fun showLocationSelectionFragment() {
        showFragment(LocationSelectionFragment(), false)
    }

    fun showWeatherFragmentForCurrentLocation() {
        if (locationPermissionAvailable()) {
            checkForLocation()
        } else {
            askLocationPermission()
        }
    }

    fun showSearchLocation() {
        showFragment(SearchLocationFragment())
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            LOCATION_REQUEST -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkForLocation()
                } else {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        showAlert(
                            "Location Permission Needed",
                            "In order to get you current location weather info, please grant location permission from Settings") {
                            startActivity(Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", this.packageName, null)
                            ))
                        }
                    }
                }
                return
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun checkForLocation() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (LocationManagerCompat.isLocationEnabled(locationManager)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                LocationServices.getFusedLocationProviderClient(this).lastLocation.addOnSuccessListener { location ->
                    if (location == null) {
                        showAlert(
                            "Turn on Location",
                            "Weather Alert needs your location to show latest info") {
                            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                            startActivity(intent)
                        }
                        return@addOnSuccessListener
                    }
                    viewModel.fetchLocation(location.latitude, location.longitude)
                    showForecastFragment()
                }
            }
        } else {
            showAlert(
                "Turn on Location",
                "Weather Alert needs your location to show latest info") {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        }
    }

    private fun startNotificationService(time: Long) {
        viewModel.lockAlert()
        val intent = Intent(this, WeatherBroadcastReceiver::class.java)
        val pendingIntent = if (SDK_INT >= android.os.Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(applicationContext, Notification_REQUEST_CODE, intent, PendingIntent.FLAG_MUTABLE)
        } else PendingIntent.getBroadcast(applicationContext, Notification_REQUEST_CODE, intent, 0)
        ((getSystemService(ALARM_SERVICE)) as AlarmManager).set(AlarmManager.RTC_WAKEUP, time, pendingIntent)
    }

    private fun observeConnection() {
        NetworkEvents.observe(this) { event ->
            when (event) {
                NetworkEvent.ConnectivityLost -> {
                    binding?.noConnectionTextView?.apply {
                        text = getString(R.string.no_connection)
                        setBackgroundColor(Colors.dangerColor)
                        fontColor =  Colors.whiteColor
                        visibility = View.VISIBLE
                    }
                }
                else -> {
                    if (binding?.noConnectionTextView?.visibility == View.VISIBLE) {
                        binding?.noConnectionTextView?.apply {
                            text = getString(R.string.connection_restored)
                            setBackgroundColor(Colors.successColor)
                            fontColor =  Colors.whiteColor
                            visibility = View.VISIBLE
                        }
                        CoroutineScope(IO).launch {
                            delay(2000)
                            withContext(Main) {
                                binding?.noConnectionTextView?.visibility = View.GONE
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setObserver() {
        viewModel.forecastLiveData.observe(this) {
            checkBackgroundFetch()
            it.list?.let { list -> checkForRainAlert(list) }
        }
    }

    private fun checkBackgroundFetch() {
        viewModel.getBackgroundInfo()?.let {
            if (it.backgroundFetchLock.not()) {
                startBackgroundFetchService()
            }
        } ?: kotlin.run {
            startBackgroundFetchService()
        }
    }

    private fun startBackgroundFetchService() {
        viewModel.lockBackgroundFetch()
        val intent = Intent(this, BackgroundFetchBroadcastReceiver::class.java)
        val pendingIntent = if (SDK_INT >= android.os.Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(applicationContext, Notification_REQUEST_CODE, intent, PendingIntent.FLAG_MUTABLE)
        } else PendingIntent.getBroadcast(applicationContext, Notification_REQUEST_CODE, intent, 0)
        ((getSystemService(ALARM_SERVICE)) as AlarmManager).set(
            AlarmManager.RTC_WAKEUP,
            getTwelveHourFromTime(),
            pendingIntent)
    }

    private fun checkForRainAlert(list: List<WeatherInfo>) {
        viewModel.getBackgroundInfo()?.let {
            if (it.rainAlertLock.not()) {
                var found = false
                for (info in list) {
                    info.weather?.firstOrNull()?.id?.let { id ->
                        if (id < 600) {
                            info.dt_txt?.let { dt ->
                                val time = formatStringToDate(dt, YYYY_MM_DD_HH_MM_SS)
                                val beforeTime = getFiveMinutesBeforeTime(time)
                                startNotificationService(beforeTime)
                                found = true
                            }
                        }
                    }
                    if (found) break
                }
            }
        } ?: kotlin.run {
            var found = false
            for (info in list) {
                info.weather?.firstOrNull()?.id?.let { id ->
                    if (id < 600) {
                        info.dt_txt?.let { dt ->
                            val time = formatStringToDate(dt, YYYY_MM_DD_HH_MM_SS)
                            val beforeTime = getFiveMinutesBeforeTime(time)
                            startNotificationService(beforeTime)
                            found = true
                        }
                    }
                }
                if (found) break
            }
        }
    }

    private fun requestLocation() {
        val mLocationRequest = LocationRequest.create()
        mLocationRequest.interval = 60000
        mLocationRequest.fastestInterval = 5000
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val mLocationCallback: LocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
            }
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.getFusedLocationProviderClient(this)
                .requestLocationUpdates(mLocationRequest, mLocationCallback, null)
        }
    }

    override fun onStart() {
        super.onStart()
        requestLocation()
    }

    companion object {
        const val Notification_REQUEST_CODE = 10001
    }
}