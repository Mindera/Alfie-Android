package au.com.alfie.ecomm.data.product.mapper

import au.com.alfie.ecomm.data.product.product
import au.com.alfie.ecomm.data.product.productData
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
internal class ProductMapperTest {

    @Test
    fun testProductMapper() = runTest {
        val expected = product
        val result = productData.product?.productInfo?.toDomain()
        assertEquals(expected, result)
    }
}
