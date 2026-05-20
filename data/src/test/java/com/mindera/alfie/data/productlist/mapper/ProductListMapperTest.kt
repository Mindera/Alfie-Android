package com.mindera.alfie.data.productlist.mapper

import com.mindera.alfie.data.productlist.productList
import com.mindera.alfie.data.productlist.productListData
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ProductListMapperTest {

    @Test
    fun testProductListMapper() = runTest {
        val result = productListData.productListing?.toDomain()

        assertEquals(productList, result)
    }
}
