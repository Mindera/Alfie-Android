package com.mindera.alfie.data.productlist

import com.mindera.alfie.repository.productlist.model.CursorPagination
import com.mindera.alfie.repository.productlist.model.ProductList
import com.mindera.alfie.repository.productlist.model.ProductListEntry
import com.mindera.alfie.repository.productlist.model.ProductListPriceRange
import com.mindera.alfie.repository.shared.model.Media

internal val productList = ProductList(
    products = listOf(
        ProductListEntry(
            id = "123456",
            slug = "123456-product",
            name = "Product name",
            brandName = "Brand",
            productType = "Clothing",
            primaryImage = Media.Image(
                url = "https://www.alfie.com/productimages/thumb/1/image.jpg",
                alt = "Product image"
            ),
            priceRange = ProductListPriceRange(
                minAmount = 100.0,
                maxAmount = 200.0,
                currencyCode = "AUD"
            ),
            inventoryTotal = 10,
            tags = listOf("Best Seller")
        ),
        ProductListEntry(
            id = "654321",
            slug = "654321-product",
            name = "Product 2",
            brandName = null,
            productType = null,
            primaryImage = null,
            priceRange = ProductListPriceRange(
                minAmount = 100.0,
                maxAmount = 100.0,
                currencyCode = "AUD"
            ),
            inventoryTotal = null,
            tags = emptyList()
        )
    ),
    pagination = CursorPagination(
        endCursor = "cursor123",
        hasNextPage = false,
        totalCount = 2
    )
)
