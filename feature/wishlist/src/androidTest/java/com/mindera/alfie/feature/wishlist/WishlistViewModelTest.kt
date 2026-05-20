package com.mindera.alfie.feature.wishlist

import androidx.lifecycle.SavedStateHandle
import com.mindera.alfie.core.navigation.arguments.wishlist.WishlistNavArgs
import com.mindera.alfie.core.navigation.arguments.wishlist.wishlistNavArgs
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@ExtendWith(MockKExtension::class)
class WishlistViewModelTest {

    @RelaxedMockK
    private lateinit var savedStateHandle: SavedStateHandle

    @BeforeEach
    fun setUp() {
        mockkStatic("com.mindera.alfie.feature.wishlist.NavArgsGettersKt")
        every { savedStateHandle.navArgs<WishlistNavArgs>() } returns wishlistNavArgs(launchFromTop = false)
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun getLaunchFromTop(launchFromTop: Boolean) {
        every { savedStateHandle.navArgs<WishlistNavArgs>() } returns wishlistNavArgs(launchFromTop = launchFromTop)
        val viewModel = createViewModel()
        if (launchFromTop) {
            assertTrue(viewModel.launchFromTop)
        } else {
            assertFalse(viewModel.launchFromTop)
        }
    }

    private fun createViewModel() = WishlistViewModel(
        savedStateHandle = savedStateHandle,
        getWishlistUseCase = mockk(),
        wishlistUiFactory = mockk(),
        removeFromWishlist = mockk()
    )
}
