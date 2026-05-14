package com.mindera.alfie.data.shared.mapper

import com.mindera.alfie.data.shared.size
import com.mindera.alfie.data.shared.sizeContainerData
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
internal class SizeMapperTest {
    @Test
    fun testSizeContainerMapper() = runTest {
        val expected = size
        val result = sizeContainerData.toDomain()

        assertEquals(expected, result)
    }
}
