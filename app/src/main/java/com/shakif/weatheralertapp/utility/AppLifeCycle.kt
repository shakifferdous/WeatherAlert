package com.shakif.weatheralertapp.utility

import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.shakif.weatheralertapp.network.MyNetworkUtils.registerConnectivityMonitor
import com.shakif.weatheralertapp.network.MyNetworkUtils.unregisterConnectivityMonitor

class AppLifecycle(private val application: Application): DefaultLifecycleObserver {

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        application.registerConnectivityMonitor()
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        application.unregisterConnectivityMonitor()
    }
}