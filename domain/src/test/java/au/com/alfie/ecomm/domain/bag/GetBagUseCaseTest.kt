package au.com.alfie.ecomm.domain.bag

import au.com.alfie.ecomm.domain.UseCaseResult
import au.com.alfie.ecomm.domain.usecase.bag.GetBagUseCase
import au.com.alfie.ecomm.repository.bag.BagRepository
import au.com.alfie.ecomm.repository.product.model.Product
import au.com.alfie.ecomm.repository.result.RepositoryResult
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
class GetBagUseCaseTest {
    @RelaxedMockK
    private lateinit var bagRepository: BagRepository

    @InjectMockKs
    lateinit var subject: GetBagUseCase

    @Test
    fun `get list of products in the bag`() = runTest {
        val mockBag = mockk<List<Product>>()
        coEvery { bagRepository.getBag() } returns RepositoryResult.Success(mockBag)

        val expected = UseCaseResult.Success(mockBag)

        val result = subject()

        assertEquals(expected, result)
    }
}