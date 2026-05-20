package com.mindera.alfie.core.configuration.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class ConfigurationData(
    val version: String
)
