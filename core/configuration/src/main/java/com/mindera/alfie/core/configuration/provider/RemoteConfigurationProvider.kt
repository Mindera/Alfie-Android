package com.mindera.alfie.core.configuration.provider

import com.mindera.alfie.core.configuration.dto.ConfigurationData

internal interface RemoteConfigurationProvider : Configuration {

    fun getConfig(key: String): ConfigurationData?
}
