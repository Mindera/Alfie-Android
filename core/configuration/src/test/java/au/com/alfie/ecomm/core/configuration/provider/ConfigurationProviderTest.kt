package au.com.alfie.ecomm.core.configuration.provider

import au.com.alfie.ecomm.core.configuration.featureData
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@ExtendWith(MockKExtension::class)
class ConfigurationProviderTest {

    @RelaxedMockK
    private lateinit var firebaseProvider: FirebaseProvider

    @RelaxedMockK
    private lateinit var localProvider: LocalProvider

    @InjectMockKs
    private lateinit var configurationProvider: ConfigurationProvider

    @Test
    fun `getBoolean - when has firebase value then return firebase value`() {
        every { firebaseProvider.getBoolean("key") } returns true
        every { localProvider.getBoolean("key") } returns false

        val result = configurationProvider.getBoolean("key")

        assertNotNull(result)
        assertTrue(result)
    }

    @Test
    fun `getBoolean - when has only local value then return local value`() {
        every { firebaseProvider.getBoolean("key") } returns null
        every { localProvider.getBoolean("key") } returns false

        val result = configurationProvider.getBoolean("key")

        assertNotNull(result)
        assertFalse(result)
    }

    @Test
    fun `getBoolean - when does not have any value then return null`() {
        every { firebaseProvider.getBoolean("key") } returns null
        every { localProvider.getBoolean("key") } returns null

        val result = configurationProvider.getBoolean("key")

        assertNull(result)
    }

    @Test
    fun `getString - when has firebase value then return firebase value`() {
        every { firebaseProvider.getString("key") } returns "firebase"
        every { localProvider.getString("key") } returns "local"

        val result = configurationProvider.getString("key")

        assertNotNull(result)
        assertEquals("firebase", result)
    }

    @Test
    fun `getString - when has only local value then return local value`() {
        every { firebaseProvider.getString("key") } returns null
        every { localProvider.getString("key") } returns "local"

        val result = configurationProvider.getString("key")

        assertNotNull(result)
        assertEquals("local", result)
    }

    @Test
    fun `getString - when does not have any value then return null`() {
        every { firebaseProvider.getString("key") } returns null
        every { localProvider.getString("key") } returns null

        val result = configurationProvider.getString("key")

        assertNull(result)
    }

    @Test
    fun `getStringList - when has firebase value then return firebase value`() {
        every { firebaseProvider.getStringList("key") } returns listOf("firebase")
        every { localProvider.getStringList("key") } returns listOf("local")

        val result = configurationProvider.getStringList("key")

        assertNotNull(result)
        assertEquals(listOf("firebase"), result)
    }

    @Test
    fun `getStringList - when has only local value then return local value`() {
        every { firebaseProvider.getStringList("key") } returns null
        every { localProvider.getStringList("key") } returns listOf("local")

        val result = configurationProvider.getStringList("key")

        assertNotNull(result)
        assertEquals(listOf("local"), result)
    }

    @Test
    fun `getStringList - when does not have any value then return null`() {
        every { firebaseProvider.getStringList("key") } returns null
        every { localProvider.getStringList("key") } returns null

        val result = configurationProvider.getStringList("key")

        assertNull(result)
    }

    @Test
    fun `getData - when has firebase value then return firebase value`() {
        every { firebaseProvider.getData("key") } returns featureData
        every { localProvider.getData("key") } returns featureData

        val result = configurationProvider.getData("key")

        assertNotNull(result)
        assertEquals(featureData, result)
    }

    @Test
    fun `getData - when has only local value then return local value`() {
        every { firebaseProvider.getData("key") } returns null
        every { localProvider.getData("key") } returns featureData

        val result = configurationProvider.getData("key")

        assertNotNull(result)
        assertEquals(featureData, result)
    }

    @Test
    fun `getData - when does not have any value then return null`() {
        every { firebaseProvider.getData("key") } returns null
        every { localProvider.getData("key") } returns null

        val result = configurationProvider.getData("key")

        assertNull(result)
    }
}
