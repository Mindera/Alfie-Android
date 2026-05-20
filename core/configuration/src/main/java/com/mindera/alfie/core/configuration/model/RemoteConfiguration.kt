package com.mindera.alfie.core.configuration.model

import com.mindera.alfie.core.configuration.Version
import com.squareup.moshi.JsonClass

sealed interface RemoteConfiguration {

    @JsonClass(generateAdapter = true)
    data class VersionConfiguration(
        val version: Version,
        val isEnabled: Boolean
    ) : RemoteConfiguration
}
