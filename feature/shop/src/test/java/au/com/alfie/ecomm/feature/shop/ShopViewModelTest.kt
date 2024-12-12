package au.com.alfie.ecomm.feature.shop

import app.cash.turbine.test
import au.com.alfie.ecomm.core.deeplink.DeeplinkHandler
import au.com.alfie.ecomm.core.test.CoroutineExtension
import au.com.alfie.ecomm.domain.UseCaseResult
import au.com.alfie.ecomm.domain.usecase.navigation.GetRootNavEntriesUseCase
import au.com.alfie.ecomm.feature.shop.delegate.NavigateToEntryDelegate
import au.com.alfie.ecomm.feature.shop.model.ShopUI
import au.com.alfie.ecomm.feature.shop.model.ShopUIState
import au.com.alfie.ecomm.feature.uievent.UIEventEmitterDelegate
import au.com.alfie.ecomm.feature.webview.delegate.WebViewHandlerDelegate
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class, CoroutineExtension::class)
class ShopViewModelTest {

    companion object {

        private val initShopUi = ShopUI(servicesUrl = "$BASE_URL/${ShopUIFactory.SERVICES_WEB_URL}")
    }

    @RelaxedMockK
    private lateinit var getRootNavEntriesUseCase: GetRootNavEntriesUseCase

    @RelaxedMockK
    private lateinit var shopUIFactory: ShopUIFactory

    @RelaxedMockK
    private lateinit var deeplinkHandler: DeeplinkHandler

    @RelaxedMockK
    private lateinit var navigateToEntryDelegate: NavigateToEntryDelegate

    @RelaxedMockK
    private lateinit var webViewHandlerDelegate: WebViewHandlerDelegate

    @BeforeEach
    fun setUp() {
        coEvery { getRootNavEntriesUseCase() } returns UseCaseResult.Success(navEntries)
        coEvery { shopUIFactory.invoke() } returns shopUi
        mockkStatic("au.com.alfie.ecomm.feature.shop.NavArgsGettersKt")
    }

    @Test
    fun `init - state is successfully defined with the uiFactory result`() = runTest {
        coEvery { shopUIFactory.invoke() } returns initShopUi
        val expected = ShopUIState.Data(initShopUi)

        val viewModel = buildViewModel()

        viewModel.state.test {
            val result = awaitItem()
            assertEquals(expected, result)

            cancelAndConsumeRemainingEvents()
        }
    }

    private fun buildViewModel() = ShopViewModel(
        uiFactory = shopUIFactory,
        deeplinkHandler = deeplinkHandler,
        navigateToEntryDelegate = navigateToEntryDelegate,
        uiEventEmitterDelegate = UIEventEmitterDelegate(),
        webViewHandlerDelegate = webViewHandlerDelegate
    )
}
