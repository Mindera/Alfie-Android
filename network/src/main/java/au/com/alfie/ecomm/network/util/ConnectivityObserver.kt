package au.com.alfie.ecomm.network.util

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {

    fun observe(): Flow<ConnectivityStatus>

    enum class ConnectivityStatus {
        Available, Lost
    }
}
