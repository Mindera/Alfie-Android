package au.com.alfie.ecomm.core.analytics.params

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class AnalyticsValuesTest {

    @Test
    fun `toString - given IntValues then parse it properly`() {
        val subject = AnalyticsValues.IntValues(12)

        val result = subject.toString()

        assertEquals("12", result)
    }

    @Test
    fun `toString - given StringValues then parse it properly`() {
        val subject = AnalyticsValues.StringValues("value")

        val result = subject.toString()

        assertEquals("value", result)
    }
}
