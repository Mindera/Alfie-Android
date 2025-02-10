//package au.com.alfie.ecomm.feature.bag
//
//import app.cash.turbine.test
//import au.com.alfie.ecomm.core.test.CoroutineExtension
//import au.com.alfie.ecomm.domain.UseCaseResult
//import au.com.alfie.ecomm.domain.usecase.bag.GetBagUseCase
//import au.com.alfie.ecomm.feature.wishlist.WishlistUiFactory
//import io.mockk.coEvery
//import io.mockk.impl.annotations.RelaxedMockK
//import io.mockk.junit5.MockKExtension
//import io.mockk.mockk
//import kotlinx.collections.immutable.toImmutableList
//import kotlinx.coroutines.test.runTest
//import org.junit.jupiter.api.extension.ExtendWith
//import kotlin.test.Test
//import kotlin.test.assertEquals
//
//@ExtendWith(MockKExtension::class, CoroutineExtension::class)
//internal class WishlistViewModelTest {
//
//    @RelaxedMockK
//    private lateinit var getwishlistUseCase: GetwishlistUseCase
//
//    @RelaxedMockK
//    private lateinit var wishlistUiFactory: WishlistUiFactory
//
//    @Test
//    fun `WHEN getwishlistList returns a success THEN update the state with the correct product list`() = runTest {
//        coEvery { getwishlistUseCase() } returns UseCaseResult.Success(products)
//        coEvery { wishlistUiFactory(products) } returns wishlistProductUi
//
//        val viewModel = buildViewModel()
//
//        viewModel.state.test {
//            val result = awaitItem()
//            assertEquals(wishlistUiState.Data.Loaded(wishlistProductUi.toImmutableList()), result)
//
//            cancelAndConsumeRemainingEvents()
//        }
//    }
//
//    @Test
//    fun `WHEN getwishlistList returns an error THEN update the state with the error state`() = runTest {
//        coEvery { getWishlistUseCase() } returns UseCaseResult.Error(mockk())
//
//        val viewModel = buildViewModel()
//
//        viewModel.state.test {
//            val result = awaitItem()
//            assertEquals(wishlistUiState.Error, result)
//
//            cancelAndConsumeRemainingEvents()
//        }
//    }
//
//
//    private fun buildViewModel() = WishlistViewModel(
//        getwishlistUseCase = getwishlistUseCase,
//        wishlistUiFactory = wishlistUiFactory,
//    )
//}