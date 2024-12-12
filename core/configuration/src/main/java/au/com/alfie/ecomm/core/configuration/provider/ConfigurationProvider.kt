package au.com.alfie.ecomm.core.configuration.provider

import au.com.alfie.ecomm.core.configuration.dto.FeatureData
import timber.log.Timber
import javax.inject.Inject

internal class ConfigurationProvider @Inject constructor(
    firebaseProvider: FirebaseProvider,
    localProvider: LocalProvider
) : Configuration {

    // The order matters since the first match is going to be the one returned, so we check Firebase Remote Configuration
    // first and fallback to a local configuration if needed
    private val providers = listOf(firebaseProvider, localProvider)

    override fun getBoolean(key: String): Boolean? = providers.firstNotNullOfOrNull { it.getBoolean(key) }.alsoIfNull { key }

    override fun getString(key: String): String? = providers.firstNotNullOfOrNull { it.getString(key) }.alsoIfNull { key }

    override fun getStringList(key: String): List<String>? = providers.firstNotNullOfOrNull { it.getStringList(key) }.alsoIfNull { key }

    override fun getData(key: String): FeatureData? = providers.firstNotNullOfOrNull { it.getData(key) }.alsoIfNull { key }

    private fun <T> T?.alsoIfNull(block: (T?) -> String): T? {
        Timber.w("Feature configuration key ${block(this)} not available")
        return this
    }
}
