package au.com.alfie.ecomm.core.configuration

import au.com.alfie.ecomm.core.configuration.ConfigurationKeyType.Version

enum class RemoteConfigurationKey(
    val value: String,
    val type: ConfigurationKeyType
) {
    // Currently only testing keys are set, replace with proper ones when they are available
    ForceUpdate("android_update_minimum", Version),
    SoftUpdate("android_update_recommended", Version)
}

// The supported types for now are Boolean and Data, but it is possible to define configurations with String or Number types
enum class ConfigurationKeyType {
    Version
}
