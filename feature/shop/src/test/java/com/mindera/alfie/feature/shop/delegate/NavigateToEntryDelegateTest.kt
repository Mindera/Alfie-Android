package com.mindera.alfie.feature.shop.delegate

import androidx.lifecycle.ViewModel
import com.mindera.alfie.core.commons.string.StringResource
import com.mindera.alfie.core.deeplink.DeeplinkHandler
import com.mindera.alfie.core.environment.EnvironmentManager
import com.mindera.alfie.core.environment.model.Environment
import com.mindera.alfie.core.navigation.Screen
import com.mindera.alfie.core.test.CoroutineExtension
import com.mindera.alfie.feature.shop.category.model.CategoryEntryUI
import com.mindera.alfie.feature.uievent.UIEventEmitter
import com.mindera.alfie.feature.uievent.UIEventEmitterDelegate
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class, CoroutineExtension::class)
internal class NavigateToEntryDelegateTest {

    companion object {
        private const val WEB_URL = "https://www.alfie.com"
    }

    @RelaxedMockK
    private lateinit var deeplinkHandler: DeeplinkHandler

    @RelaxedMockK
    private lateinit var uiEventEmitterDelegate: UIEventEmitterDelegate

    @RelaxedMockK
    private lateinit var environmentManager: EnvironmentManager

    @InjectMockKs
    private lateinit var delegate: NavigateToEntryDelegate

    @BeforeEach
    fun setup() {
        coEvery { environmentManager.current() } returns Environment.Dev(
            graphQLUrl = "",
            webUrl = WEB_URL
        )
    }

    @Test
    fun `GIVEN openCategoryEntry WHEN entry is brands THEN navigate to Brands shop screen`() = runTest {
        coEvery { uiEventEmitterDelegate.uiEvent }
        coJustRun { deeplinkHandler.handle(any()) }

        val path = "brands"
        val entry = CategoryEntryUI(
            id = 1,
            title = StringResource.fromText("brands"),
            path = path
        )

        val viewModel = TestViewModel(delegate, uiEventEmitterDelegate)

        with(viewModel) {
            openCategoryEntry(entry)
            coVerify { deeplinkHandler.handle("$WEB_URL/$path") }
        }
    }

    @Test
    fun `GIVEN openCategoryEntry WHEN entry is a leaf category THEN navigate to plp`() = runTest {
        val entry = CategoryEntryUI(
            id = 1,
            title = StringResource.fromText("title"),
            path = "women"
        )
        val viewModel = TestViewModel(delegate, uiEventEmitterDelegate)

        with(viewModel) {
            openCategoryEntry(entry)
            coVerify(exactly = 0) { deeplinkHandler.handle(any()) }
            coVerify { navigateTo(screen = any(Screen.ProductList::class)) }
        }
    }

    @Test
    fun `GIVEN openCategoryEntry WHEN entry has children THEN drill into the sub-category screen`() = runTest {
        val entry = CategoryEntryUI(
            id = 7,
            title = StringResource.fromText("Women"),
            path = "women",
            hasChildren = true
        )
        val viewModel = TestViewModel(delegate, uiEventEmitterDelegate)

        with(viewModel) {
            openCategoryEntry(entry)
            coVerify(exactly = 0) { deeplinkHandler.handle(any()) }
            coVerify { navigateTo(screen = any(Screen.Category::class)) }
        }
    }

    @Test
    fun `GIVEN openCategoryEntry WHEN entry is brands AND has children THEN the brands link wins`() = runTest {
        coJustRun { deeplinkHandler.handle(any()) }

        val path = "brands"
        val entry = CategoryEntryUI(
            id = 1,
            title = StringResource.fromText("brands"),
            path = path,
            hasChildren = true
        )
        val viewModel = TestViewModel(delegate, uiEventEmitterDelegate)

        with(viewModel) {
            openCategoryEntry(entry)
            coVerify { deeplinkHandler.handle("$WEB_URL/$path") }
        }
    }

    private class TestViewModel(
        navigateToEntryDelegate: NavigateToEntryDelegate,
        uiEventEmitterDelegate: UIEventEmitterDelegate
    ) : ViewModel(), NavigateToEntry by navigateToEntryDelegate, UIEventEmitter by uiEventEmitterDelegate
}
