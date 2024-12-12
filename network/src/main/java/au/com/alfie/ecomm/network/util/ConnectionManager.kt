package au.com.alfie.ecomm.network.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ConnectionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    val isConnected: Boolean
        get() = _isConnected.get()

    private val _isConnected = AtomicBoolean(false)

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            _isConnected.set(true)
        }

        override fun onLost(network: Network) {
            _isConnected.set(false)
        }
    }

    init {
        listenToNetworkChanges()
    }

    private fun listenToNetworkChanges() {
        val connectivityManager = context.getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
    }
}
