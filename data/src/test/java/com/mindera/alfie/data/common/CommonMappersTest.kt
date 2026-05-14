package com.mindera.alfie.data.common

import com.mindera.alfie.data.toDomain
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