package au.com.alfie.ecomm.feature.shop.brand

import app.cash.turbine.test
import au.com.alfie.ecomm.core.commons.dispatcher.DispatcherProvider
import au.com.alfie.ecomm.core.test.CoroutineExtension
import au.com.alfie.ecomm.domain.UseCaseResult
import au.com.alfie.ecomm.domain.usecase.brand.GetBrandsUseCase
import au.com.alfie.ecomm.feature.shop.brand.factory.BrandUIStateFactory
import au.com.alfie.ecomm.feature.shop.brand.model.BrandEntryUI
import au.com.alfie.ecomm.feature.shop.brand.model.BrandEvent
import au.com.alfie.ecomm.feature.shop.brand.model.BrandUIState
import au.com.alfie.ecomm.feature.shop.brandUiState
import au.com.alfie.ecomm.feature.shop.brands
import au.com.alfie.ecomm.feature.shop.delegate.NavigateToEntryDelegate
import au.com.alfie.ecomm.feature.uievent.UIEventEmitterDelegate
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class, CoroutineExtension::class)
internal class BrandsViewModelTest {

    @RelaxedMockK
    private lateinit var dispatcher: DispatcherProvider

    @RelaxedMockK
    private lateinit var getBrandsUseCase: GetBrandsUseCase

    @RelaxedMockK
    private lateinit var uiFactory: BrandUIStateFactory

    @RelaxedMockK
    private lateinit var navigateToEntryDelegate: NavigateToEntryDelegate

    @BeforeEach
    fun setup() {
        coEvery { dispatcher.io() } returns Dispatchers.Default
    }

    @Test
    fun `GIVEN OnBrandSearchEvent WHEN searchTerm is filled THEN state is updated with filtered list`() = runTest {
        val expectedBrandUiState = BrandUIState.Data(
            entries = persistentListOf(
                BrandEntryUI.Divider(character = 'A'),
                BrandEntryUI.Entry(
                    id = "123",
                    name = "Adidas",
                    slug = "adidas"
                ),
                BrandEntryUI.Entry(
                    id = "234",
                    name = "Ardidas",
                    slug = "ardidas"
                ),
                BrandEntryUI.Divider(character = 'B'),
                BrandEntryUI.Entry(
                    id = "456",
                    name = "Batidas",
                    slug = "batidas"
                )
            ),
            isLoading = false
        )
        coEvery { getBrandsUseCase() } returns UseCaseResult.Success(brands)
        coEvery { uiFactory(any()) } returns brandUiState
        coEvery {
            uiFactory.filterBySearchTerm(
                entries = brandUiState.entries,
                searchTerm = "idas"
            )
        } returns expectedBrandUiState

        val event = BrandEvent.OnBrandSearchEvent(searchTerm = "idas")

        val viewModel = buildViewModel()

        viewModel.state.test {
            // First result will be the unfiltered data, we should ignore that
            awaitItem()

            viewModel.handleEvent(event)
            val result = awaitItem()
            assertEquals(expectedBrandUiState, result)
        }
    }

    private fun buildViewModel() = BrandsViewModel(
        getBrandsUseCase = getBrandsUseCase,
        uiFactory = uiFactory,
        navigateToEntryDelegate = navigateToEntryDelegate,
        uiEventEmitterDelegate = UIEventEmitterDelegate()
    )
}
