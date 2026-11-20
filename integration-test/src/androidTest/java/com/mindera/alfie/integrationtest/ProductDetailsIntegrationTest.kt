package com.mindera.alfie.integrationtest

import com.mindera.alfie.core.testseed.SeedAnchors
import com.mindera.alfie.graphql.bff.GetProductDetailsQuery
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * `productDetails` against the Mock BFF: happy path + variants reconstructed from `options[]`.
 *
 * Pinned to [SeedAnchors.Products.default], a product with Color × Size variants.
 */
internal class ProductDetailsIntegrationTest : BffIntegrationTest() {

    @Test
    fun productDetails_happyPath_resolvesProductWithConsistentVariants() = withBff { client ->
        val slug = SeedAnchors.Products.default

        val data = client.executeOrFail(GetProductDetailsQuery(handle = slug))
        val product = requireNotNull(data.productDetails) { "No productDetails for handle '$slug'" }.productFragment

        assertTrue(product.id.isNotBlank(), "Product id should not be blank")
        assertEquals(slug, product.slug, "Returned product slug should match the requested handle")
        assertTrue(product.name.isNotBlank(), "Product name should not be blank")

        val variants = product.variants.orEmpty().filterNotNull()
        assertTrue(variants.size > 1, "Expected several variants for '$slug'")

        // Variants are reconstructed from options[]: every variant option name must be a declared option.
        val optionNames = product.options.orEmpty().filterNotNull().map { it.name }.toSet()
        variants.forEach { variant ->
            variant.productVariantFragment.optionValues.forEach { optionValue ->
                assertTrue(
                    optionNames.contains(optionValue.name),
                    "Variant option '${optionValue.name}' is not among product options $optionNames"
                )
            }
        }
    }
}
