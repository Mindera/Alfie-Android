package com.mindera.alfie.integrationtest

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.mindera.alfie.graphql.bff.GetProductDetailsQuery
import com.mindera.alfie.graphql.bff.ProductListQuery
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/** `productDetails` against the local BFF: happy path + variants reconstructed from `options[]`. */
internal class ProductDetailsIntegrationTest : BffIntegrationTest() {

    @Test
    fun productDetails_happyPath_resolvesProductWithConsistentVariants() = withBff { client ->
        // Use a real handle from the product list so the test is self-sufficient.
        val slug = firstProductSlug(client) ?: return@withBff

        val data = client.executeOrFail(GetProductDetailsQuery(handle = slug, platform = PLATFORM))
        val product = requireNotNull(data.productDetails) { "No productDetails for handle '$slug'" }.productFragment

        assertTrue(product.id.isNotBlank(), "Product id should not be blank")
        assertEquals(slug, product.slug, "Returned product slug should match the requested handle")
        assertTrue(product.name.isNotBlank(), "Product name should not be blank")

        val variants = product.variants.orEmpty().filterNotNull()
        assertTrue(variants.isNotEmpty(), "Expected at least one variant for '$slug'")

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

    private suspend fun firstProductSlug(client: ApolloClient): String? =
        client.executeOrFail(
            ProductListQuery(collectionHandle = DEFAULT_COLLECTION_HANDLE, limit = Optional.present(1))
        ).productList.productListResponseFragment
            .products.firstOrNull()?.productListEntryFragment?.slug
}
