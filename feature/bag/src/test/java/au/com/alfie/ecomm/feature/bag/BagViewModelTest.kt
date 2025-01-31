package au.com.alfie.ecomm.feature.bag

import app.cash.turbine.test
import au.com.alfie.ecomm.core.test.CoroutineExtension
import au.com.alfie.ecomm.domain.UseCaseResult
import au.com.alfie.ecomm.domain.usecase.bag.GetBagUseCase
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.Test
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class, CoroutineExtension::class)
class BagViewModelTest {

    @RelaxedMockK
    private lateinit var getBagUseCase: GetBagUseCase

    @RelaxedMockK
    private lateinit var bagUiFactory: BagUiFactory

    @BeforeEach
    fun setUp() {

    }

    @Test
    fun `WHEN getBagList returns a success THEN update the state with the correct product list`() = runTest {
        coEvery { getBagUseCase() } returns UseCaseResult.Success(products)
        coEvery { bagUiFactory(products) } returns bagProductUi

        val viewModel = buildViewModel()

        viewModel.state.test {
            val result = awaitItem()
            assertEquals(BagUiState.Data.Loaded(bagProductUi), result)

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `WHEN getBagList returns an error THEN update the state with the error state`() = runTest {
        coEvery { getBagUseCase() } returns UseCaseResult.Error(mockk())

        val viewModel = buildViewModel()

        viewModel.state.test {
            val result = awaitItem()
            assertEquals(BagUiState.Error, result)

            cancelAndConsumeRemainingEvents()
        }
    }


    private fun buildViewModel() = BagViewModel(
        getBagUseCase = getBagUseCase,
        bagUiFactory = bagUiFactory,
    )
}