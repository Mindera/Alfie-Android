package au.com.alfie.ecomm.feature.bag

import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.Test
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
class BagUiFactoryTest {

    @InjectMockKs
    private lateinit var uiFactory: BagUiFactory

    @Test
    fun `map wishlist to ui xSmall`() = runTest {
        val result = uiFactory(products)

        assertEquals(result, bagProductUi)
    }
}