package au.com.alfie.ecomm.core.analytics.events

import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
class FirebaseEventExtTest {

    @Test
    fun `WHEN EventKey is SCREEN_NAME THEN toFirebaseEventKey returns correct value`() {
        val expected = "screen_name"
        val eventKey = EventKey.SCREEN_NAME

        val result = eventKey.toFirebaseEventKey()

        assertEquals(result, expected)
    }

    @Test
    fun `WHEN EventKey is ERROR_MESSAGE THEN toFirebaseEventKey returns correct value`() {
        val expected = "error_message"
        val eventKey = EventKey.ERROR_MESSAGE

        val result = eventKey.toFirebaseEventKey()

        assertEquals(result, expected)
    }

    @Test
    fun `WHEN EventErrorValue is GENERIC_ERROR THEN toFirebaseEventKey returns correct value`() {
        val expected = "generic_error"
        val eventKey = EventErrorValue.GENERIC_ERROR

        val result = eventKey.toFirebaseEventErrorValue()

        assertEquals(result, expected)
    }

    @Test
    fun `WHEN EventErrorValue is NETWORK_ERROR THEN toFirebaseEventKey returns correct value`() {
        val expected = "network_error"
        val eventKey = EventErrorValue.NETWORK_ERROR

        val result = eventKey.toFirebaseEventErrorValue()

        assertEquals(result, expected)
    }
}
