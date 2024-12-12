package au.com.alfie.ecomm.core.commons.extension

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class DoubleExtTest {

    @Test
    fun `orZero - given a null double then should return zero`() {
        val number: Double? = null

        val result = number.orZero()

        assertEquals(0.0, result)
    }

    @Test
    fun `orZero - given a double then should return the double itself`() {
        val number = 9.0

        val result = number.orZero()

        assertEquals(9.0, result)
    }
}
