package com.mindera.alfie.feature.plp

import com.mindera.alfie.core.navigation.arguments.productlist.ProductListType
import com.mindera.alfie.repository.productlist.model.ProductListQuerySource
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class ProductListTypeMapperTest {

    @Test
    fun `toQuerySource - WHEN type is a brand key THEN lists the brand by key and name`() {
        val type = ProductListType.Brand.Key(key = "935", name = "Kashmere")

        assertEquals(ProductListQuerySource.Brand(key = "935", name = "Kashmere"), type.toQuerySource())
    }

    @Test
    fun `displayTitle - WHEN type is a brand key THEN shows the brand name, not the opaque key`() {
        val type = ProductListType.Brand.Key(key = "935", name = "Kashmere")

        assertEquals("Kashmere", type.displayTitle)
    }
}
