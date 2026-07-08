package com.mindera.alfie.feature.shop.category

import com.mindera.alfie.core.commons.string.StringResource
import com.mindera.alfie.core.test.CoroutineExtension
import com.mindera.alfie.feature.shop.category.model.CategoryEntryUI
import com.mindera.alfie.feature.shop.category.model.CategoryEvent
import com.mindera.alfie.feature.shop.delegate.NavigateToEntryDelegate
import com.mindera.alfie.feature.uievent.UIEventEmitterDelegate
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class, CoroutineExtension::class)
internal class CategoryViewModelTest {

    @RelaxedMockK
    private lateinit var navigateToEntryDelegate: NavigateToEntryDelegate

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
        navigateToEntryDelegate = navigateToEntryDelegate,
        uiEventEmitterDelegate = UIEventEmitterDelegate()
    )
}
