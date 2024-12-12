package au.com.alfie.ecomm.data.shared.mapper

import au.com.alfie.ecomm.data.shared.price
import au.com.alfie.ecomm.data.shared.priceInfoData
import au.com.alfie.ecomm.data.shared.priceRange
import au.com.alfie.ecomm.data.shared.priceRangeInfoData
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
