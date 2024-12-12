package au.com.alfie.ecomm.core.configuration.provider

import au.com.alfie.ecomm.core.configuration.dto.ConfigurationData

internal interface RemoteConfigurationProvider : Configuration {

    fun getConfig(key: String): ConfigurationData?
}
