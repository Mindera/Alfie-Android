package au.com.alfie.ecomm.feature.shop.category

import au.com.alfie.ecomm.core.commons.string.StringResource
import au.com.alfie.ecomm.core.test.CoroutineExtension
import au.com.alfie.ecomm.domain.UseCaseResult
import au.com.alfie.ecomm.domain.usecase.navigation.GetRootNavEntriesUseCase
import au.com.alfie.ecomm.feature.shop.category.factory.CategoryUIStateFactory
import au.com.alfie.ecomm.feature.shop.category.model.CategoryEntryUI
import au.com.alfie.ecomm.feature.shop.category.model.CategoryEvent
import au.com.alfie.ecomm.feature.shop.categoryUiState
import au.com.alfie.ecomm.feature.shop.delegate.NavigateToEntryDelegate
import au.com.alfie.ecomm.feature.shop.navEntries
import au.com.alfie.ecomm.feature.uievent.UIEventEmitterDelegate
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class, CoroutineExtension::class)
internal class CategoryViewModelTest {

    @RelaxedMockK
    private lateinit var getRootNavEntriesUseCase: GetRootNavEntriesUseCase

    @RelaxedMockK
    private lateinit var uiFactory: CategoryUIStateFactory

    @RelaxedMockK
    private lateinit var navigateToEntryDelegate: NavigateToEntryDelegate

    @BeforeEach
    fun setup() {
        coEvery { getRootNavEntriesUseCase() } returns UseCaseResult.Success(navEntries)
        coEvery { uiFactory(any(), any()) } returns categoryUiState
    }

    @Test
    fun `GIVEN OnEntryClickEvent WHEN entry path is not empty THEN should open the entry`() = runTest {
        val event = CategoryEvent.OnEntryClickEvent(
            entry = CategoryEntryUI(
                id = 0,
                title = StringResource.fromText("Title"),
                path = "https://url.com"
            )
        )

        val viewModel = buildViewModel()

        with(viewModel) {
            handleEvent(event)

            verify { openCategoryEntry(event.entry) }
        }
    }

    @Test
    fun `GIVEN OnEntryClickEvent WHEN entry path is empty THEN should not open the entry`() = runTest {
        val event = CategoryEvent.OnEntryClickEvent(
            entry = CategoryEntryUI(
                id = 0,
                title = StringResource.fromText("Title"),
                path = ""
            )
        )

        val viewModel = buildViewModel()

        with(viewModel) {
            handleEvent(event)

            verify(exactly = 0) { openCategoryEntry(event.entry) }
        }
    }

    private fun buildViewModel() = CategoryViewModel(
        getRootNavEntriesUseCase = getRootNavEntriesUseCase,
        uiFactory = uiFactory,
        navigateToEntryDelegate = navigateToEntryDelegate,
        uiEventEmitterDelegate = UIEventEmitterDelegate()
    )
}
