package au.com.alfie.ecomm.core.configuration.provider

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class LocalProviderTest {

    @Test
    fun `getBoolean - successfully get boolean value`() {
        val provider = LocalProvider("{\"key\": true}")

        val result = provider.getBoolean("key")

        assertNotNull(result)
        assertTrue(result)
    }

    @Test
    fun `getBoolean - when key does not exist then return null`() {
        val provider = LocalProvider("{\"key\": true}")

        val result = provider.getBoolean("other_key")

        assertNull(result)
    }

    @Test
    fun `getString - successfully get string value`() {
        val provider = LocalProvider("{\"key\": \"value\"}")

        val result = provider.getString("key")

        assertNotNull(result)
        assertEquals("value", result)
    }

    @Test
    fun `getString - when key does not exist then return null`() {
        val provider = LocalProvider("{\"key\": \"value\"}")

        val result = provider.getString("other_key")

        assertNull(result)
    }

    @Test
    fun `getStringList - successfully get string list value`() {
        val provider = LocalProvider("{\"key\": \"value1,value2,value3\"}")

        val result = provider.getStringList("key")

        assertNotNull(result)
        assertEquals(listOf("value1", "value2", "value3"), result)
    }

    @Test
    fun `getStringList - when key does not exist then return null`() {
        val provider = LocalProvider("{\"key\": \"value1,value2,value3\"}")

        val result = provider.getString("other_key")

        assertNull(result)
    }
}
