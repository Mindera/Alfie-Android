package au.com.alfie.ecomm.core.configuration.model

import au.com.alfie.ecomm.core.configuration.featureConfiguration
import au.com.alfie.ecomm.core.configuration.featureConfigurationUnavailableGuest
import au.com.alfie.ecomm.core.configuration.featureConfigurationUnavailableRegistered
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FeatureConfigurationTest {

    @Test
    fun `isSupported - GIVEN current app state and logged in WHEN configuration is valid THEN true is returned`() = runTest {
        val result = featureConfiguration.isSupported(
            version = "1.11.0",
            isRegistered = true,
            countryCode = "DE"
        )

        assertTrue(result)
    }

    @Test
    fun `isSupported - GIVEN current app state and logged out WHEN configuration is valid THEN true is returned`() = runTest {
        val result = featureConfiguration.isSupported(
            version = "1.11.0",
            isRegistered = false,
            countryCode = "DE"
        )

        assertTrue(result)
    }

    @Test
    fun `isSupported - GIVEN current app state WHEN configuration min app version is not valid THEN false is returned`() = runTest {
        val result = featureConfiguration.isSupported(
            version = "1.9.0",
            isRegistered = false,
            countryCode = "DE"
        )

        assertFalse(result)
    }

    @Test
    fun `isSupported - GIVEN current app state WHEN configuration guest user is not available THEN false is returned`() = runTest {
        val result = featureConfigurationUnavailableGuest.isSupported(
            version = "1.11.0",
            isRegistered = false,
            countryCode = "DE"
        )

        assertFalse(result)
    }

    @Test
    fun `isSupported - GIVEN current app state WHEN configuration guest user country is empty THEN true is returned`() = runTest {
        val guestEmptyCountries = featureConfiguration.guestUser.copy(countryCodes = emptyList())
        val featureConfigurationEmptyCountries = featureConfiguration.copy(guestUser = guestEmptyCountries)
        val result = featureConfigurationEmptyCountries.isSupported(
            version = "1.11.0",
            isRegistered = false,
            countryCode = "DE"
        )

        assertTrue(result)
    }

    @Test
    fun `isSupported - GIVEN current app state WHEN configuration guest user country is not valid THEN false is returned`() = runTest {
        val result = featureConfiguration.isSupported(
            version = "1.11.0",
            isRegistered = false,
            countryCode = "FR"
        )

        assertFalse(result)
    }

    @Test
    fun `isSupported - GIVEN current app state WHEN configuration registered user is not available THEN false is returned`() = runTest {
        val result = featureConfigurationUnavailableRegistered.isSupported(
            version = "1.11.0",
            isRegistered = true,
            countryCode = "DE"
        )

        assertFalse(result)
    }

    @Test
    fun `isSupported - GIVEN current app state WHEN configuration registered user country is empty THEN true is returned`() = runTest {
        val guestEmptyCountries = featureConfiguration.registeredUser.copy(countryCodes = emptyList())
        val featureConfigurationEmptyCountries = featureConfiguration.copy(guestUser = guestEmptyCountries)
        val result = featureConfigurationEmptyCountries.isSupported(
            version = "1.11.0",
            isRegistered = true,
            countryCode = "DE"
        )

        assertTrue(result)
    }

    @Test
    fun `isSupported - GIVEN current app state WHEN configuration registered user country is not valid THEN false is returned`() = runTest {
        val result = featureConfiguration.isSupported(
            version = "1.11.0",
            isRegistered = true,
            countryCode = "FR"
        )

        assertFalse(result)
    }
}
