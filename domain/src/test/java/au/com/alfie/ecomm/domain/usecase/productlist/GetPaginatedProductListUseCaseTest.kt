package au.com.alfie.ecomm.domain.usecase.productlist

import androidx.paging.testing.asSnapshot
import au.com.alfie.ecomm.repository.productlist.ProductListRepository
import au.com.alfie.ecomm.repository.productlist.model.ProductList
import au.com.alfie.ecomm.repository.productlist.model.ProductListEntry
import au.com.alfie.ecomm.repository.result.RepositoryResult
import au.com.alfie.ecomm.repository.shared.model.Pagination
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

@ExtendWith(MockKExtension::class)
class GetPaginatedProductListUseCaseTest {

    @RelaxedMockK
    lateinit var productListRepository: ProductListRepository

    @InjectMockKs
    lateinit var useCase: GetPaginatedProductListUseCase

    @Test
    fun `invoke - returns a paging flow`() = runTest {
        val product = mockk<ProductListEntry>(relaxed = true)
        val products = List(size = 15) { product }
        val pagination = mockk<Pagination>()
        val productList = ProductList(
            pagination = pagination,
            products = products,
            hierarchy = emptyList(),
            title = "Women"
        )
        every { pagination.nextPage } returns null
        every { pagination.previousPage } returns null
        every { pagination.total } returns 15
        coEvery {
            productListRepository.getProductList(any(), any(), any(), any())
        } returns RepositoryResult.Success(productList)

        val result = useCase(
            categoryId = "54684321",
            query = null,
            metadataProvider = { assertEquals(15, it.totalResults) }
        ).asSnapshot()

        assertEquals(products, result)
    }
}
