package au.com.alfie.ecomm.core.configuration.dto

import au.com.alfie.ecomm.core.configuration.model.FeatureConfiguration
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class FeatureData(
    val versions: List<FeatureConfiguration>
)
