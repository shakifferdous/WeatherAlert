package com.shakif.weatheralertapp.network

import android.app.Application
import android.content.Context
import android.net.*
import androidx.lifecycle.LiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface NetworkState {
    val isConnectedToNetwork: Boolean
    val network: Network?
    val networkCapabilities: NetworkCapabilities?
    val linkProperties: LinkProperties?
}

fun <T> genericAPICallback(completion: (T?, APIError?) -> Unit): Callback<T> {
    return object : Callback<T> {
        override fun onFailure(call: Call<T>, t: Throwable) {
            if (!MyNetworkUtils.isConnected()) completion(null, APIError.noConnection()) else completion(null, APIError.generic())
        }

        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (response.isSuccessful && response.body() != null) {
                response.body()?.let { completion(it, null) } ?: completion(null, APIError.generic())
            } else completion(null, APIError.generic())
        }
    }
}

internal class NetworkStateManager : NetworkState {
    override var isConnectedToNetwork: Boolean = false
        set(value) {
            field = value
            NetworkEvents.notify(if (value) NetworkEvent.ConnectivityAvailable else NetworkEvent.ConnectivityLost)
        }
    override var network: Network? = null
    override var linkProperties: LinkProperties? = null
        set(value) {
            val event = NetworkEvent.LinkPropertyChanged(field)
            field = value
            NetworkEvents.notify(event)
        }
    override var networkCapabilities: NetworkCapabilities? = null
        set(value) {
            val event = NetworkEvent.NetworkCapabilityChanged(field)
            field = value
            NetworkEvents.notify(event)
        }
}

internal class NetworkCallback(private val holder: NetworkStateManager) :
    ConnectivityManager.NetworkCallback() {
    override fun onAvailable(network: Network) {
        holder.network = network
        holder.isConnectedToNetwork = true
    }

    override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
        holder.networkCapabilities = networkCapabilities
    }

    override fun onLost(network: Network) {
        holder.network = network
        holder.isConnectedToNetwork = false
    }

    override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
        holder.linkProperties = linkProperties
    }
}

object NetworkEvents : LiveData<NetworkEvent>() {
    internal fun notify(event: NetworkEvent) {
        postValue(event)
    }
}

object MyNetworkUtils {
    private lateinit var holder: NetworkStateManager
    private var networkCallback: NetworkCallback? = null

    fun Application.registerConnectivityMonitor() {
        if (::holder.isInitialized.not()) holder = NetworkStateManager()
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        networkCallback = NetworkCallback(holder)
        networkCallback?.let {
            connectivityManager.registerNetworkCallback(NetworkRequest.Builder().build(), it)
        }
    }

    fun Application.unregisterConnectivityMonitor() {
        val callback = networkCallback ?: return
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.unregisterNetworkCallback(callback)
        networkCallback = null
    }

    fun isConnected() = when (NetworkEvents.value) {
        NetworkEvent.ConnectivityAvailable -> true
        else -> false
    }
}
