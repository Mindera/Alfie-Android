package com.mindera.alfie.data.product.service

import com.mindera.alfie.graphql.bff.GetProductDetailsQuery
import com.mindera.alfie.graphql.bff.GetRelatedProductsQuery
import com.mindera.alfie.graphql.bff.ProductByBarcodeQuery

internal interface ProductService {

    suspend fun getProduct(handle: String): Result<GetProductDetailsQuery.Data>

    suspend fun getProductByBarcode(barcode: String): Result<ProductByBarcodeQuery.Data>

    suspend fun getRelatedProducts(handle: String, limit: Int): Result<GetRelatedProductsQuery.Data>
}
