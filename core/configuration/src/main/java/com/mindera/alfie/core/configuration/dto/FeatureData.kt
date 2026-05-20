package com.mindera.alfie.core.configuration.dto

import com.mindera.alfie.core.configuration.model.FeatureConfiguration
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class FeatureData(
    val versions: List<FeatureConfiguration>
)
