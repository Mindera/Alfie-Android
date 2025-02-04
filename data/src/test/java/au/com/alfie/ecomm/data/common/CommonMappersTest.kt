package au.com.alfie.ecomm.data.common

import au.com.alfie.ecomm.data.toDomain
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class CommonMappersTest {

    @Test
    fun `test color mapping`() = runTest {
        val result = productInfoColor.colorInfo.toDomain()

        assertEquals(color, result)
    }
}