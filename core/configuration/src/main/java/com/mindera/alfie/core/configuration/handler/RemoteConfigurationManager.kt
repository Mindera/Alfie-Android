package com.mindera.alfie.core.configuration.handler

import com.mindera.alfie.core.configuration.ConfigurationKeyType
import com.mindera.alfie.core.configuration.RemoteConfigurationKey
import com.mindera.alfie.core.configuration.Version
import com.mindera.alfie.core.configuration.compareTo
import com.mindera.alfie.core.configuration.model.RemoteConfiguration
import com.mindera.alfie.core.configuration.model.RemoteConfiguration.VersionConfiguration
import com.mindera.alfie.core.configuration.provider.FirebaseProvider
import com.mindera.alfie.core.environment.model.BuildConfiguration
import javax.inject.Inject

internal class RemoteConfigurationManager @Inject constructor(
    private val firebaseProvider: FirebaseProvider,
    private val buildConfiguration: BuildConfiguration
) : RemoteConfigurationHandler {

    @Suppress("UNCHECKED_CAST")
    override suspend fun <T : RemoteConfiguration> get(key: RemoteConfigurationKey): T? =
        when (key.type) {
            ConfigurationKeyType.Version -> {
                val config = firebaseProvider.getConfig(key.value)

                config?.let {
                    val appVersion = buildConfiguration.versionName
                    val version = Version(config.version)
                    val isEnabled = version > Version(appVersion)

                    VersionConfiguration(
                        version = version,
                        isEnabled = isEnabled
                    ) as? T
                }
            }
        }
}
