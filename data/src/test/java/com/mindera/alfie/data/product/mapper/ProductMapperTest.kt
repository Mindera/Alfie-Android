package com.mindera.alfie.data.product.mapper

import com.mindera.alfie.data.product.product
import com.mindera.alfie.data.product.productData
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
