package au.com.alfie.ecomm.core.configuration.handler

import au.com.alfie.ecomm.core.configuration.FeatureKey
import au.com.alfie.ecomm.core.configuration.FeatureKeyType
import au.com.alfie.ecomm.core.configuration.provider.ConfigurationProvider
import javax.inject.Inject

internal class FeatureConfigurationManager @Inject constructor(
    private val configurationProvider: ConfigurationProvider
) : FeatureHandler {

    override suspend fun isEnabled(key: FeatureKey): Boolean =
        when (key.type) {
            FeatureKeyType.Boolean -> {
                configurationProvider.getBoolean(key.value) ?: false
            }

            FeatureKeyType.Data -> {
                // Implement feature configuration here
                false

                // val isSignedIn = when (val result = signInRepository.isSignedIn()) {
                //     is RepositoryResult.Success -> result.data
                //     is RepositoryResult.Error -> false
                // }
                // val countryCode = appSessionRepository.getCountry()
                // val appVersion = buildConfiguration.versionName

                // val data = configurationProvider.getData(key.value)
                // val versions = data?.versions.orEmpty()

                // versions.any {
                //     it.isSupported(
                //         version = appVersion,
                //         isRegistered = isSignedIn,
                //         countryCode = countryCode
                //     )
                // }
            }
        }
}
