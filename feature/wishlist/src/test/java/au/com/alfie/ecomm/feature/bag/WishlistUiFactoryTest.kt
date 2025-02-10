package au.com.alfie.ecomm.feature.bag

import au.com.alfie.ecomm.feature.wishlist.WishlistUiFactory
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
class WishlistUiFactoryTest {

    @InjectMockKs
    private lateinit var uiFactory: WishlistUiFactory

    @Test
    fun `map bag to ui xSmall`() = runTest {
        val result = uiFactory(products.first(), {})

        //assertEquals(result, bagProductUi)
    }
}