package com.mindera.alfie.data.product.repository

import com.mindera.alfie.data.product.mapper.toDomain
import com.mindera.alfie.data.product.service.ProductService
import com.mindera.alfie.data.productlist.mapper.toDomain
import com.mindera.alfie.data.toRepositoryResult
import com.mindera.alfie.repository.product.ProductRepository
import com.mindera.alfie.repository.product.model.BarcodeMatch
import com.mindera.alfie.repository.product.model.Product
import com.mindera.alfie.repository.productlist.model.ProductListEntry
import com.mindera.alfie.repository.result.ErrorResult
import com.mindera.alfie.repository.result.ErrorType
import com.mindera.alfie.repository.result.RepositoryResult
import com.mindera.alfie.repository.result.flatMap
import javax.inject.Inject

internal class ProductRepositoryImpl @Inject constructor(
    private val productService: ProductService
) : ProductRepository {

    override suspend fun getProduct(handle: String): RepositoryResult<Product> =
        productService.getProduct(handle = handle)
            .mapCatching { data ->
                data.productDetails?.productFragment?.toDomain()
                    ?: error("productDetails was null for handle=$handle")
            }
            .toRepositoryResult()

    /**
     * `productByBarcode` answers null for a code the catalogue does not carry, which is an
     * outcome rather than a fault — it is folded into [ErrorType.RESOURCE_NOT_FOUND] here so the
     * caller can tell "nothing scanned to a product" apart from a request that actually failed.
     */
    override suspend fun getProductByBarcode(barcode: String): RepositoryResult<BarcodeMatch> =
        productService.getProductByBarcode(barcode = barcode)
            .toRepositoryResult()
            .flatMap { data ->
                data.productByBarcode
                    ?.let { RepositoryResult.Success(it.toDomain()) }
                    ?: RepositoryResult.Error(ErrorResult(type = ErrorType.RESOURCE_NOT_FOUND))
            }

    override suspend fun getRelatedProducts(
        handle: String,
        limit: Int
    ): RepositoryResult<List<ProductListEntry>> =
        productService.getRelatedProducts(handle = handle, limit = limit)
            .mapCatching { data ->
                data.relatedProducts.map { it.productListEntryFragment.toDomain() }
            }
            .toRepositoryResult()
}
