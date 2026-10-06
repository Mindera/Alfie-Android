package com.mindera.alfie.repository.product

import com.mindera.alfie.repository.product.model.BarcodeMatch
import com.mindera.alfie.repository.product.model.Product
import com.mindera.alfie.repository.productlist.model.ProductListEntry
import com.mindera.alfie.repository.result.RepositoryResult

interface ProductRepository {

    suspend fun getProduct(handle: String): RepositoryResult<Product>

    /**
     * Resolves a scanned barcode to a product.
     *
     * A code the catalogue does not carry is the common case rather than a fault, so it comes back
     * as [RepositoryResult.Error] with [com.mindera.alfie.repository.result.ErrorType.RESOURCE_NOT_FOUND]
     * — distinguishable from a transport or server failure.
     */
    suspend fun getProductByBarcode(barcode: String): RepositoryResult<BarcodeMatch>

    suspend fun getRelatedProducts(handle: String, limit: Int): RepositoryResult<List<ProductListEntry>>
}
