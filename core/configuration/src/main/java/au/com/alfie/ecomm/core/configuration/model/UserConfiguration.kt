package au.com.alfie.ecomm.core.configuration.model

import com.squareup.moshi.Json

data class UserConfiguration(
    @Json(name = "available")
    val isAvailable: Boolean,
    @Json(name = "available_country_codes")
    val countryCodes: List<String>
)
