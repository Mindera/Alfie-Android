package au.com.alfie.ecomm.domain.bag

import au.com.alfie.ecomm.domain.UseCaseResult
import au.com.alfie.ecomm.domain.usecase.bag.AddToBagUseCase
import au.com.alfie.ecomm.repository.bag.BagRepository
import au.com.alfie.ecomm.repository.product.model.Product
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
class AddToBagUseCaseTest {
    @RelaxedMockK
    private lateinit var bagRepository: BagRepository

    @InjectMockKs
    lateinit var subject: AddToBagUseCase

    @Test
    fun `get list of products in the bag`() = runTest {
        val mockProduct = mockk<Product>()
        coEvery { bagRepository.addToBag(mockProduct) }

        val expected = UseCaseResult.Success(Unit)

        val result = subject("10")

        assertEquals<Any>(expected, result)
    }
}