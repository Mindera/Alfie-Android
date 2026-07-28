package com.mindera.alfie.feature.shop.delegate

import androidx.lifecycle.ViewModel
import com.mindera.alfie.core.commons.string.StringResource
import com.mindera.alfie.core.navigation.Screen
import com.mindera.alfie.core.test.CoroutineExtension
import com.mindera.alfie.feature.shop.category.model.CategoryEntryUI
import com.mindera.alfie.feature.uievent.UIEventEmitter
import com.mindera.alfie.feature.uievent.UIEventEmitterDelegate
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class, CoroutineExtension::class)
internal class NavigateToEntryDelegateTest {

    @RelaxedMockK
    private lateinit var uiEventEmitterDelegate: UIEventEmitterDelegate

    @InjectMockKs
    private lateinit var delegate: NavigateToEntryDelegate

    @Test
    fun `GIVEN openCategoryEntry WHEN entry is a leaf category THEN navigate to plp`() = runTest {
        val viewModel = TestViewModel(delegate, uiEventEmitterDelegate)

        with(viewModel) {
            openCategoryEntry(entry(path = "women"))
            coVerify { navigateTo(screen = any(Screen.ProductList::class)) }
        }
    }

    @Test
    fun `GIVEN openCategoryEntry WHEN entry has children THEN drill into the sub-category screen`() = runTest {
        val viewModel = TestViewModel(delegate, uiEventEmitterDelegate)

        with(viewModel) {
            openCategoryEntry(entry(path = "women", hasChildren = true))
            coVerify { navigateTo(screen = any(Screen.Category::class)) }
        }
    }

    @Test
    fun `GIVEN openCategoryEntry WHEN the handle is brands THEN it is treated as an ordinary leaf`() = runTest {
        // Pins the absence of special-casing: "brands" resolves through the same path as any other
        // handle until a product requirement says otherwise.
        val viewModel = TestViewModel(delegate, uiEventEmitterDelegate)

        with(viewModel) {
            openCategoryEntry(entry(path = "brands"))
            coVerify { navigateTo(screen = any(Screen.ProductList::class)) }
        }
    }

    private fun entry(path: String, hasChildren: Boolean = false) = CategoryEntryUI(
        id = 1,
        title = StringResource.fromText("title"),
        path = path,
        hasChildren = hasChildren
    )

    private class TestViewModel(
        navigateToEntryDelegate: NavigateToEntryDelegate,
        uiEventEmitterDelegate: UIEventEmitterDelegate
    ) : ViewModel(), NavigateToEntry by navigateToEntryDelegate, UIEventEmitter by uiEventEmitterDelegate
}
