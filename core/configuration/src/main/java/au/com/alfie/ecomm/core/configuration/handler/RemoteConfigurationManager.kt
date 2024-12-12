package au.com.alfie.ecomm.core.configuration.handler

import au.com.alfie.ecomm.core.configuration.ConfigurationKeyType
import au.com.alfie.ecomm.core.configuration.RemoteConfigurationKey
import au.com.alfie.ecomm.core.configuration.Version
import au.com.alfie.ecomm.core.configuration.compareTo
import au.com.alfie.ecomm.core.configuration.model.RemoteConfiguration
import au.com.alfie.ecomm.core.configuration.model.RemoteConfiguration.VersionConfiguration
import au.com.alfie.ecomm.core.configuration.provider.FirebaseProvider
import au.com.alfie.ecomm.core.environment.model.BuildConfiguration
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
