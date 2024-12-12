package au.com.alfie.ecomm.core.configuration.handler

import au.com.alfie.ecomm.core.configuration.RemoteConfigurationKey
import au.com.alfie.ecomm.core.configuration.model.RemoteConfiguration

interface RemoteConfigurationHandler {

    suspend fun <T : RemoteConfiguration>get(key: RemoteConfigurationKey): T?
}
