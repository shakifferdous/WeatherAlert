package com.shakif.weatheralertapp.network

import android.net.LinkProperties
import android.net.NetworkCapabilities

sealed class NetworkEvent {
    object ConnectivityLost : NetworkEvent()
    object ConnectivityAvailable : NetworkEvent()
    data class NetworkCapabilityChanged(val old: NetworkCapabilities?) : NetworkEvent()
    data class LinkPropertyChanged(val old: LinkProperties?) : NetworkEvent()
}