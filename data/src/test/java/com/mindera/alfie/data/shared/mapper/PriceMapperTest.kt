package com.mindera.alfie.data.shared.mapper

import com.mindera.alfie.data.shared.price
import com.mindera.alfie.data.shared.priceInfoData
import com.mindera.alfie.data.shared.priceRange
import com.mindera.alfie.data.shared.priceRangeInfoData
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
internal class PriceMapperTest {
    @Test
    fun testPriceInfoMapper() = runTest {
        val expected = price
        val result = priceInfoData.toDomain()

        assertEquals(expected, result)
    }

    @Test
    fun testPriceRangeInfoMapper() = runTest {
        val expected = priceRange
        val result = priceRangeInfoData.toDomain()

        assertEquals(expected, result)
    }
}
