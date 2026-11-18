package com.mindera.alfie.feature.pdp

import com.mindera.alfie.core.deeplink.DeeplinkHandler
import com.mindera.alfie.core.deeplink.DeeplinkResult
import com.mindera.alfie.core.navigation.arguments.ProductDetailsNavArgs
import com.mindera.alfie.core.navigation.arguments.productDetailsNavArgs
import com.mindera.alfie.feature.pdp.destinations.ProductDetailsScreenDestination
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Resolves URLs through the app's real [DeeplinkHandler], which parses them with `android.net.Uri`,
 * so this runs under Robolectric (JUnit4, executed via the junit-vintage engine).
 */
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, sdk = [34])
class ProductDetailsDeeplinksTest {

    private val deeplinkHandler = DeeplinkHandler(setOf(ProductDetailsDeeplinks()))

    @Test
    fun `product link with a numeric handle opens the PDP`() = runTest {
        val result = deeplinkHandler.resolve("https://www.alfie.com/product/346123285")

        assertOpensProduct(result, productDetailsNavArgs(handle = "346123285"))
    }

    @Test
    fun `product link with a slug handle opens the PDP`() = runTest {
        val result = deeplinkHandler.resolve("https://www.alfie.com/product/papinelle-coco-organic-cotton-pj-346123285")

        assertOpensProduct(result, productDetailsNavArgs(handle = "papinelle-coco-organic-cotton-pj-346123285"))
    }

    // A shared link names one variant; the PDP opens on it so the shopper lands on what they were sent.
    @Test
    fun `product link with a variantId opens the PDP on that variant`() = runTest {
        val result = deeplinkHandler.resolve("https://www.alfie.com/product/papinelle-coco-pj?variantId=v3")

        assertOpensProduct(result, productDetailsNavArgs(handle = "papinelle-coco-pj", variantId = "v3"))
    }

    @Test
    fun `product link without a variantId opens the PDP with no variant preselected`() = runTest {
        val result = deeplinkHandler.resolve("https://www.alfie.com/product/papinelle-coco-pj")

        assertOpensProduct(result, productDetailsNavArgs(handle = "papinelle-coco-pj", variantId = null))
    }

    @Test
    fun `product link with only unrelated query parameters opens the PDP with no variant preselected`() = runTest {
        val result = deeplinkHandler.resolve("https://www.alfie.com/product/papinelle-coco-pj?utm_source=share")

        assertOpensProduct(result, productDetailsNavArgs(handle = "papinelle-coco-pj", variantId = null))
    }

    private fun assertOpensProduct(result: DeeplinkResult, expected: ProductDetailsNavArgs) {
        assertTrue("expected NavigateTo, got $result", result is DeeplinkResult.NavigateTo)
        assertEquals(ProductDetailsScreenDestination(expected), (result as DeeplinkResult.NavigateTo).direction)
    }
}
