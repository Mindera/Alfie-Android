package com.mindera.alfie.core.configuration

import com.mindera.alfie.core.configuration.model.FeatureConfiguration
import com.mindera.alfie.core.configuration.model.UserConfiguration

internal val userConfigurationGuest = UserConfiguration(
    isAvailable = true,
    countryCodes = listOf("DE", "BE")
)

internal val userConfigurationRegistered = UserConfiguration(
    isAvailable = true,
    countryCodes = listOf("DE", "BE", "AU")
)

internal val featureConfiguration = FeatureConfiguration(
    minAppVersion = "1.10.1",
    guestUser = userConfigurationGuest,
    registeredUser = userConfigurationRegistered
)

internal val featureConfigurationUnavailableGuest = FeatureConfiguration(
    minAppVersion = "1.10.1",
    guestUser = userConfigurationGuest.copy(isAvailable = false),
    registeredUser = userConfigurationRegistered
)

internal val featureConfigurationUnavailableRegistered = FeatureConfiguration(
    minAppVersion = "1.10.1",
    guestUser = userConfigurationGuest,
    registeredUser = userConfigurationRegistered.copy(isAvailable = false)
)
