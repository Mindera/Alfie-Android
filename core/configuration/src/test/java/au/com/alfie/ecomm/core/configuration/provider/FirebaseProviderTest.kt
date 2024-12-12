package au.com.alfie.ecomm.core.configuration.provider

import au.com.alfie.ecomm.core.configuration.FEATURE_DATA_RESPONSE
import au.com.alfie.ecomm.core.configuration.dto.ConfigurationData
import au.com.alfie.ecomm.core.configuration.featureData
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfig.VALUE_SOURCE_REMOTE
import com.google.firebase.remoteconfig.FirebaseRemoteConfig.VALUE_SOURCE_STATIC
import com.google.firebase.remoteconfig.FirebaseRemoteConfigValue
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockkStatic
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@ExtendWith(MockKExtension::class)
class FirebaseProviderTest {

    @RelaxedMockK
    private lateinit var firebaseRemoteConfig: FirebaseRemoteConfig

    @RelaxedMockK
    private lateinit var remoteValue: FirebaseRemoteConfigValue

    @BeforeEach
    fun setUp() {
        mockkStatic(FirebaseRemoteConfig::class)
        every { FirebaseRemoteConfig.getInstance() } returns firebaseRemoteConfig
        every { firebaseRemoteConfig.getValue(any()) } returns remoteValue
    }

    @Test
    fun `getBoolean - when has key then return the value`() {
        every { remoteValue.source } returns VALUE_SOURCE_REMOTE
        every { firebaseRemoteConfig.getBoolean("key") } returns true

        val provider = FirebaseProvider()

        val result = provider.getBoolean("key")

        assertNotNull(result)
        assertTrue(result)
    }

    @Test
    fun `getBoolean - when does not have key then return null`() {
        every { remoteValue.source } returns VALUE_SOURCE_STATIC

        val provider = FirebaseProvider()

        val result = provider.getBoolean("key")

        assertNull(result)
    }

    @Test
    fun `getString - when has key then return the value`() {
        every { remoteValue.source } returns VALUE_SOURCE_REMOTE
        every { firebaseRemoteConfig.getString("key") } returns "value"

        val provider = FirebaseProvider()

        val result = provider.getString("key")

        assertNotNull(result)
        assertEquals("value", result)
    }

    @Test
    fun `getString - when does not have key then return null`() {
        every { remoteValue.source } returns VALUE_SOURCE_STATIC

        val provider = FirebaseProvider()

        val result = provider.getString("key")

        assertNull(result)
    }

    @Test
    fun `getStringList - when has key then return the value`() {
        every { remoteValue.source } returns VALUE_SOURCE_REMOTE
        every { firebaseRemoteConfig.getString("key") } returns "value1,value2,value3"

        val provider = FirebaseProvider()

        val result = provider.getStringList("key")

        assertNotNull(result)
        assertEquals(listOf("value1", "value2", "value3"), result)
    }

    @Test
    fun `getStringList - when does not have key then return null`() {
        every { remoteValue.source } returns VALUE_SOURCE_STATIC

        val provider = FirebaseProvider()

        val result = provider.getStringList("key")

        assertNull(result)
    }

    @Test
    fun `getData - when has key then return the value`() {
        every { remoteValue.source } returns VALUE_SOURCE_REMOTE
        every { remoteValue.asString() } returns FEATURE_DATA_RESPONSE

        val provider = FirebaseProvider()

        val result = provider.getData("key")

        assertNotNull(result)
        assertEquals(featureData, result)
    }

    @Test
    fun `getData - when does not have key then return null`() {
        every { remoteValue.source } returns VALUE_SOURCE_STATIC

        val provider = FirebaseProvider()

        val result = provider.getData("key")

        assertNull(result)
    }

    @Test
    fun `getConfig - when has key then return the value`() {
        every { remoteValue.source } returns VALUE_SOURCE_REMOTE
        every { remoteValue.asString() } returns """{"version": "0"}"""
        val expected = ConfigurationData(version = "0")

        val provider = FirebaseProvider()

        val result = provider.getConfig("key")

        assertNotNull(result)
        assertEquals(expected, result)
    }

    @Test
    fun `getConfig - when does not have key then return null`() {
        every { remoteValue.source } returns VALUE_SOURCE_STATIC

        val provider = FirebaseProvider()

        val result = provider.getConfig("key")

        assertNull(result)
    }
}
