package com.mindera.alfie.core.configuration.handler

import com.mindera.alfie.core.configuration.RemoteConfigurationKey
import com.mindera.alfie.core.configuration.model.RemoteConfiguration

interface RemoteConfigurationHandler {

    suspend fun <T : RemoteConfiguration>get(key: RemoteConfigurationKey): T?
}
