package au.com.alfie.ecomm.feature.shop.delegate

import androidx.lifecycle.ViewModel
import au.com.alfie.ecomm.core.commons.string.StringResource
import au.com.alfie.ecomm.core.deeplink.DeeplinkHandler
import au.com.alfie.ecomm.core.environment.EnvironmentManager
import au.com.alfie.ecomm.core.environment.model.Environment
import au.com.alfie.ecomm.core.navigation.Screen
import au.com.alfie.ecomm.core.test.CoroutineExtension
import au.com.alfie.ecomm.domain.usecase.navigation.GetNavEntriesByParentIdUseCase
import au.com.alfie.ecomm.feature.shop.brand.model.BrandEntryUI
import au.com.alfie.ecomm.feature.shop.category.model.CategoryEntryUI
import au.com.alfie.ecomm.feature.shop.navEntries
import au.com.alfie.ecomm.feature.uievent.UIEventEmitter
import au.com.alfie.ecomm.feature.uievent.UIEventEmitterDelegate
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
    private lateinit var getNavEntriesByParentIdUseCase: GetNavEntriesByParentIdUseCase

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
    fun `GIVEN openCategoryEntry WHEN entry has child entries THEN navigate to category`() = runTest {
        coEvery { getNavEntriesByParentIdUseCase(any()) } returns navEntries
        val entry = CategoryEntryUI(
            id = 1,
            title = StringResource.fromText("title"),
            path = "https://url.com"
        )
        val viewModel = TestViewModel(delegate, uiEventEmitterDelegate)

        with(viewModel) {
            openCategoryEntry(entry)

            coVerify(exactly = 0) { deeplinkHandler.handle(any()) }
            coVerify { navigateTo(screen = any(Screen.Category::class)) }
        }
    }

    @Test
    fun `GIVEN openCategoryEntry WHEN entry is brands THEN navigate to Brands shop screen`() = runTest {
        coEvery { getNavEntriesByParentIdUseCase(any()) } returns emptyList()
        coEvery { uiEventEmitterDelegate.uiEvent }
        coJustRun { deeplinkHandler.handle(any()) }

        val path = "/brands"
        val entry = CategoryEntryUI(
            id = 1,
            title = StringResource.fromText("brands"),
            path = path
        )

        val viewModel = TestViewModel(delegate, uiEventEmitterDelegate)

        with(viewModel) {
            openCategoryEntry(entry)
            coVerify { deeplinkHandler.handle("$WEB_URL$path") }
        }
    }

    @Test
    fun `GIVEN openCategoryEntry WHEN entry is childless THEN navigate to plp`() = runTest {
        coEvery { getNavEntriesByParentIdUseCase(any()) } returns emptyList()
        val path = "/plp/path"
        val entry = CategoryEntryUI(
            id = 1,
            title = StringResource.fromText("title"),
            path = path
        )
        val viewModel = TestViewModel(delegate, uiEventEmitterDelegate)

        with(viewModel) {
            openCategoryEntry(entry)
            coVerify { deeplinkHandler.handle("$WEB_URL$path") }
        }
    }

    @Test
    fun `GIVEN openBrandEntry THEN navigate to plp`() = runTest {
        val slug = "slug"
        val entry = BrandEntryUI.Entry(
            id = "123",
            name = "Brand",
            slug = slug
        )

        val viewModel = TestViewModel(delegate, uiEventEmitterDelegate)

        with(viewModel) {
            openBrandEntry(entry)
            coVerify { navigateTo(screen = any(Screen.ProductList::class)) }
        }
    }

    private class TestViewModel(
        navigateToEntryDelegate: NavigateToEntryDelegate,
        uiEventEmitterDelegate: UIEventEmitterDelegate
    ) : ViewModel(), NavigateToEntry by navigateToEntryDelegate, UIEventEmitter by uiEventEmitterDelegate
}
