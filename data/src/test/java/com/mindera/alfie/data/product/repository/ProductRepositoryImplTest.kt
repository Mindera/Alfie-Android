package com.mindera.alfie.data.product.repository

import com.mindera.alfie.data.product.service.ProductService
import com.mindera.alfie.graphql.bff.GetProductDetailsQuery
import com.mindera.alfie.graphql.bff.GetRelatedProductsQuery
import com.mindera.alfie.network.exception.GraphNetworkException
import com.mindera.alfie.repository.product.model.Product
import com.mindera.alfie.repository.productlist.model.ProductListEntry
import com.mindera.alfie.repository.result.RepositoryResult
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals
import kotlin.test.assertIs

@ExtendWith(MockKExtension::class)
internal class ProductRepositoryImplTest {

    companion object {
        private const val HANDLE = "camilla-and-marc-patterson-mini-skirt"
    }

    @RelaxedMockK
    lateinit var productService: ProductService

    @InjectMockKs
    lateinit var subject: ProductRepositoryImpl

    @Test
    fun `getProduct - WHEN result is success THEN repository returns success with mapped product`() = runTest {
        val mockData = mockk<GetProductDetailsQuery.Data>(relaxed = true)
        val mockProduct = mockk<GetProductDetailsQuery.ProductDetails>(relaxed = true)
        every { mockData.productDetails } returns mockProduct
        every { mockProduct.productFragment.id } returns "p-1"
        every { mockProduct.productFragment.name } returns "Test Product"
        every { mockProduct.productFragment.slug } returns HANDLE
        every { mockProduct.productFragment.brandName } returns "Test Brand"
        every { mockProduct.productFragment.descriptionHtml } returns null
        every { mockProduct.productFragment.defaultVariantId } returns null
        every { mockProduct.productFragment.images } returns emptyList()
        every { mockProduct.productFragment.variants } returns emptyList()
        coEvery { productService.getProduct(any()) } returns Result.success(mockData)

        val result = subject.getProduct(handle = HANDLE)

        assertIs<RepositoryResult.Success<Product>>(result)
        assertEquals("p-1", result.data.id)
        assertEquals("Test Brand", result.data.brandName)
    }

    @Test
    fun `getProduct - WHEN result is failure THEN repository returns error`() = runTest {
        coEvery { productService.getProduct(any()) } returns Result.failure(
            GraphNetworkException.UnexpectedException(message = "Error")
        )

        val result = subject.getProduct(handle = HANDLE)

        assertIs<RepositoryResult.Error>(result)
    }

    @Test
    fun `getRelatedProducts - WHEN result is success THEN repository returns success with mapped entries`() = runTest {
        val mockData = mockk<GetRelatedProductsQuery.Data>(relaxed = true)
        val mockEntry = mockk<GetRelatedProductsQuery.RelatedProduct>(relaxed = true)
        every { mockData.relatedProducts } returns listOf(mockEntry)
        every { mockEntry.productListEntryFragment.id } returns "r-1"
        every { mockEntry.productListEntryFragment.slug } returns "related-slug"
        every { mockEntry.productListEntryFragment.name } returns "Related Product"
        every { mockEntry.productListEntryFragment.brandName } returns "Related Brand"
        every { mockEntry.productListEntryFragment.primaryImage } returns null
        every { mockEntry.productListEntryFragment.tags } returns null
        coEvery { productService.getRelatedProducts(any(), any()) } returns Result.success(mockData)

        val result = subject.getRelatedProducts(handle = HANDLE, limit = 8)

        assertIs<RepositoryResult.Success<List<ProductListEntry>>>(result)
        assertEquals(1, result.data.size)
        assertEquals("r-1", result.data.first().id)
        assertEquals("related-slug", result.data.first().slug)
    }

    @Test
    fun `getRelatedProducts - WHEN result is failure THEN repository returns error`() = runTest {
        coEvery { productService.getRelatedProducts(any(), any()) } returns Result.failure(
            GraphNetworkException.UnexpectedException(message = "Error")
        )

        val result = subject.getRelatedProducts(handle = HANDLE, limit = 8)

        assertIs<RepositoryResult.Error>(result)
    }

    @Test
    fun `getProduct - WHEN data is null THEN repository returns error`() = runTest {
        val expectedServiceResponse = GetProductDetailsQuery.Data(productDetails = null)
        coEvery { productService.getProduct(any()) } returns Result.success(expectedServiceResponse)

        val result = subject.getProduct(handle = HANDLE)

        assertIs<RepositoryResult.Error>(result)
    }
}
