package com.mindera.alfie.feature.shop.category

import com.mindera.alfie.core.analytics.AnalyticsManager
import com.mindera.alfie.core.commons.string.StringResource
import com.mindera.alfie.core.test.CoroutineExtension
import com.mindera.alfie.domain.UseCaseResult
import com.mindera.alfie.domain.usecase.navigation.GetRootNavEntriesUseCase
import com.mindera.alfie.feature.model.ApiErrorType
import com.mindera.alfie.feature.shop.category.factory.CategoryUIStateFactory
import com.mindera.alfie.feature.shop.category.model.CategoryEntryUI
import com.mindera.alfie.feature.shop.category.model.CategoryEvent
import com.mindera.alfie.feature.shop.category.model.CategoryUIState
import com.mindera.alfie.feature.shop.categoryUiState
import com.mindera.alfie.feature.shop.delegate.NavigateToEntryDelegate
import com.mindera.alfie.feature.shop.navEntries
import com.mindera.alfie.feature.uievent.UIEventEmitterDelegate
import com.mindera.alfie.repository.result.ErrorResult
import com.mindera.alfie.repository.result.ErrorType
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExtendWith(MockKExtension::class, CoroutineExtension::class)
internal class CategoryViewModelTest {

    @RelaxedMockK
    private lateinit var navigateToEntryDelegate: NavigateToEntryDelegate

    @RelaxedMockK
    private lateinit var getRootNavEntriesUseCase: GetRootNavEntriesUseCase

    @RelaxedMockK
    private lateinit var uiFactory: CategoryUIStateFactory

    @RelaxedMockK
    private lateinit var analyticsManager: AnalyticsManager

    @Test
    fun `GIVEN the root entries load WHEN initialised THEN the menu is exposed as data`() = runTest {
        stubSuccess()

        val viewModel = buildViewModel()

        assertEquals(categoryUiState, viewModel.state.value)
        coVerify { getRootNavEntriesUseCase() }
    }

    @Test
    fun `GIVEN the root entries fail WHEN initialised THEN the error is exposed and tracked`() = runTest {
        stubError()

        val viewModel = buildViewModel()

        assertEquals(CategoryUIState.Error(ApiErrorType.Network), viewModel.state.value)
        verify { analyticsManager.trackError(any(), any(), any(), any()) }
    }

    @Test
    fun `GIVEN a previous failure WHEN retrying THEN the entries are requested again`() = runTest {
        stubError()
        val viewModel = buildViewModel()
        stubSuccess()

        viewModel.retry()

        assertEquals(categoryUiState, viewModel.state.value)
        coVerify(exactly = 2) { getRootNavEntriesUseCase() }
    }

    @Test
    fun `GIVEN OnEntryClickEvent WHEN entry path is not empty THEN should open the entry`() = runTest {
        stubSuccess()
        val event = CategoryEvent.OnEntryClickEvent(entry = entry(path = "https://url.com"))

        val viewModel = buildViewModel()

        with(viewModel) {
            handleEvent(event)

            verify { openCategoryEntry(event.entry) }
        }
    }

    @Test
    fun `GIVEN OnEntryClickEvent WHEN entry has children but no path THEN should open the entry`() = runTest {
        stubSuccess()
        // A parent may carry no url of its own; it must still be able to drill down.
        val event = CategoryEvent.OnEntryClickEvent(entry = entry(path = "", hasChildren = true))

        val viewModel = buildViewModel()

        with(viewModel) {
            handleEvent(event)

            verify { openCategoryEntry(event.entry) }
        }
    }

    @Test
    fun `GIVEN OnEntryClickEvent WHEN entry has neither path nor children THEN should not open it`() = runTest {
        stubSuccess()
        val event = CategoryEvent.OnEntryClickEvent(entry = entry(path = ""))

        val viewModel = buildViewModel()

        with(viewModel) {
            handleEvent(event)

            verify(exactly = 0) { openCategoryEntry(event.entry) }
        }
    }

    @Test
    fun `GIVEN the load failed WHEN an entry is clicked THEN should not open the entry`() = runTest {
        stubError()
        val event = CategoryEvent.OnEntryClickEvent(entry = entry(path = "https://url.com"))

        val viewModel = buildViewModel()

        with(viewModel) {
            handleEvent(event)

            assertTrue(viewModel.state.value is CategoryUIState.Error)
            verify(exactly = 0) { openCategoryEntry(event.entry) }
        }
    }

    private fun entry(path: String, hasChildren: Boolean = false) = CategoryEntryUI(
        id = 0,
        title = StringResource.fromText("Title"),
        path = path,
        hasChildren = hasChildren
    )

    private fun stubSuccess() {
        coEvery { getRootNavEntriesUseCase() } returns UseCaseResult.Success(navEntries)
        coEvery { uiFactory(any(), any()) } returns categoryUiState
    }

    private fun stubError() {
        coEvery { getRootNavEntriesUseCase() } returns UseCaseResult.Error(
            ErrorResult(type = ErrorType.NETWORK, errorMessage = "Error", code = "0")
        )
    }

    private fun buildViewModel() = CategoryViewModel(
        getRootNavEntriesUseCase = getRootNavEntriesUseCase,
        uiFactory = uiFactory,
        analyticsManager = analyticsManager,
        navigateToEntryDelegate = navigateToEntryDelegate,
        uiEventEmitterDelegate = UIEventEmitterDelegate()
    )
}
