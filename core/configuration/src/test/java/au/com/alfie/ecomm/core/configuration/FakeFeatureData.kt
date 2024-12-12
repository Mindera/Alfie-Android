package au.com.alfie.ecomm.core.configuration

import au.com.alfie.ecomm.core.configuration.dto.FeatureData
import au.com.alfie.ecomm.core.configuration.model.FeatureConfiguration
import au.com.alfie.ecomm.core.configuration.model.UserConfiguration

internal const val FEATURE_DATA_RESPONSE = """{
  "versions": [
    {
      "minimum_app_version": "0",
      "registered_users": {
        "available": true,
        "available_country_codes": []
      },
      "guest_users": {
        "available": false,
        "available_country_codes": []
      }
    }
  ]
}"""

internal val featureData = FeatureData(
    versions = listOf(
        FeatureConfiguration(
            minAppVersion = "0",
            registeredUser = UserConfiguration(
                isAvailable = true,
                countryCodes = listOf()
            ),
            guestUser = UserConfiguration(
                isAvailable = false,
                countryCodes = listOf()
            )
        )
    )
)
