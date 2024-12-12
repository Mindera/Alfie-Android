package au.com.alfie.ecomm.data.datastore.user

import androidx.datastore.core.CorruptionException
import au.com.alfie.ecomm.data.datastore.userPreferencesProto
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.ByteArrayInputStream
import kotlin.test.assertEquals

class UserPreferencesProtoSerializerTest {

    private val serializer = UserPreferencesProtoSerializer()

    @Test
    fun `defaultValue - empty preferences by default`() {
        val expected = userPreferencesProto { }

        assertEquals(expected, serializer.defaultValue)
    }

    @Test
    fun `readFrom - fails when the input is not valid`() = runTest {
        assertThrows<CorruptionException> {
            serializer.readFrom(ByteArrayInputStream(byteArrayOf(0)))
        }
    }
}
