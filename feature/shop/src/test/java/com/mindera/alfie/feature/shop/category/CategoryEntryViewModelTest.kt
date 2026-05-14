package com.mindera.alfie.feature.shop.category

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.mindera.alfie.core.commons.string.StringResource
import com.mindera.alfie.core.navigation.arguments.CategoryNavArgs
import com.mindera.alfie.core.navigation.arguments.categoryNavArgs
import com.mindera.alfie.core.test.CoroutineExtension
import com.mindera.alfie.domain.usecase.navigation.GetNavEntriesByParentIdUseCase
import com.mindera.alfie.feature.shop.category.factory.CategoryUIStateFactory
import com.mindera.alfie.feature.shop.category.model.CategoryEntryUI
import com.mindera.alfie.feature.shop.category.model.CategoryEvent
import com.mindera.alfie.feature.shop.categoryUiState
import com.mindera.alfie.feature.shop.delegate.NavigateToEntryDelegate
import com.mindera.alfie.feature.shop.navArgs
import com.mindera.alfie.feature.uievent.UIEventEmitterDelegate
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class, CoroutineExtension::class)
internal class CategoryEntryViewModelTest {

    @RelaxedMockK
    private lateinit var getNavEntriesByParentIdUseCase: GetNavEntriesByParentIdUseCase

    @RelaxedMockK
    private lateinit var categoryUIStateFactory: CategoryUIStateFactory

    @RelaxedMockK
    private lateinit var savedStateHandle: SavedStateHandle

    @RelaxedMockK
    private lateinit var navigateToEntryDelegate: NavigateToEntryDelegate

    @BeforeEach
    fun setUp() {
        mockkStatic("com.mindera.alfie.feature.shop.NavArgsGettersKt")
        every { savedStateHandle.navArgs<CategoryNavArgs>() } returns categoryNavArgs(
            id = 1,
            title = StringResource.fromText("Title")
        )
    }

    @Test
    fun `init - state is successfully defined with the uiFactory result`() = runTest {
        coEvery { categoryUIStateFactory(any(), any()) } returns categoryUiState

        val viewModel = buildViewModel()

        viewModel.state.test {
            val result = awaitItem()
            assertEquals(categoryUiState, result)

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `handleEvent - GIVEN OnEntryClickEvent WHEN destination is WebView THEN should navigate to Webview`() = runTest {
        val event = CategoryEvent.OnEntryClickEvent(mockk<CategoryEntryUI>())

        val viewModel = buildViewModel()

        with(viewModel) {
            handleEvent(event)

            verify { openCategoryEntry(any()) }
        }
    }

    private fun buildViewModel() = CategoryEntryViewModel(
        getNavEntriesByParentIdUseCase = getNavEntriesByParentIdUseCase,
        uiFactory = categoryUIStateFactory,
        savedStateHandle = savedStateHandle,
        navigateToEntryDelegate = navigateToEntryDelegate,
        uiEventEmitterDelegate = UIEventEmitterDelegate()
    )
}
