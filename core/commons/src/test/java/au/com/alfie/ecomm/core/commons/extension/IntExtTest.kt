package au.com.alfie.ecomm.core.commons.extension

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class IntExtTest {

    @Test
    fun `orZero - given a null int then should return zero`() {
        val number: Int? = null

        val result = number.orZero()

        assertEquals(0, result)
    }

    @Test
    fun `orZero - given a int then should return the int itself`() {
        val number = 9

        val result = number.orZero()

        assertEquals(9, result)
    }
}
