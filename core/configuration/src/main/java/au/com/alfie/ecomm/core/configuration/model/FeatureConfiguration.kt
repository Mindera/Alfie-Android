package au.com.alfie.ecomm.core.configuration.model

import au.com.alfie.ecomm.core.configuration.Version
import au.com.alfie.ecomm.core.configuration.compareTo
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@JsonClass(generateAdapter = true)
data class FeatureConfiguration(
    @Json(name = "minimum_app_version")
    val minAppVersion: String,
    @Json(name = "guest_users")
    val guestUser: UserConfiguration,
    @Json(name = "registered_users")
    val registeredUser: UserConfiguration
) {

    suspend fun isSupported(
        version: String,
        isRegistered: Boolean,
        countryCode: String
    ): Boolean = withContext(Dispatchers.Default) {
        isVersionSupported(version) && isAvailableToUser(countryCode, isRegistered)
    }

    private fun isVersionSupported(version: String) = Version(version) >= Version(minAppVersion)

    private fun isAvailableToUser(countryCode: String, isRegistered: Boolean): Boolean {
        val user = if (isRegistered) registeredUser else guestUser
        val countryCodes = user.countryCodes
        val isCountrySupported = countryCodes.isEmpty() || countryCodes.any { it.equals(countryCode, ignoreCase = true) }

        return user.isAvailable && isCountrySupported
    }
}
