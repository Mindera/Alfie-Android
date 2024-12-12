package au.com.alfie.ecomm.core.configuration.model

import au.com.alfie.ecomm.core.configuration.Version
import com.squareup.moshi.JsonClass

sealed interface RemoteConfiguration {

    @JsonClass(generateAdapter = true)
    data class VersionConfiguration(
        val version: Version,
        val isEnabled: Boolean
    ) : RemoteConfiguration
}
