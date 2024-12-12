package au.com.alfie.ecomm.data.productlist.mapper

import au.com.alfie.ecomm.data.productlist.productList
import au.com.alfie.ecomm.data.productlist.productListData
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
