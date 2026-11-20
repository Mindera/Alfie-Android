// HAND-WRITTEN STAND-IN. Mindera/Alfie-BFF#155 publishes the anchors as JSON
// (`src/mock/fixtures/seed-anchors.json` in Alfie-BFF). Until the generator in
// Mindera/Alfie-Android#102 writes this file from that JSON, keep it a mirror of it, one entry per
// anchor, and change it only when the BFF does.
// Leaf names are exactly the BFF's, as listed in its `docs/mock-platform.md`; groups are PascalCase.

package com.mindera.alfie.core.testseed

/**
 * The Mock BFF's **seed anchors**: the only catalogue values a test may pin to.
 *
 * Mirrors `src/mock/fixtures/seed-anchors.json` in Alfie-BFF. What each anchor promises is in that
 * repo's `docs/mock-platform.md`. Anything else in the catalogue can change at any time.
 */
object SeedAnchors {

    const val menuHandle = "main-menu"
    val menuHandleAliases = listOf("product-category")

    object Collections {
        const val women = "women"
        const val men = "men"
        const val shirts = "shirts"
        const val dresses = "dresses"
        const val sale = "sale"
    }

    object BrandKeys {
        const val alfie = "alfie"
        const val northwind = "northwind"
        const val harbour = "harbour"
        const val ember = "ember"
    }

    object Products {
        const val default = "alfie-oxford-shirt"
        const val allAccordionMetafields = "alfie-oxford-shirt"
        const val outOfStock = "harbour-cashmere-crew-jumper"
        const val singleVariant = "ember-silk-square-scarf"
        const val onSale = "northwind-linen-wrap-dress"
        const val longName = "alfie-relaxed-organic-cotton-twill-utility-overshirt"
        const val noBrand = "essential-crew-neck-tee"
        const val oneImage = "harbour-pleated-midi-skirt"
        const val noMetafieldsNoSizeGuide = "ember-waxed-field-jacket"
    }

    object SearchTerms {
        const val many = "shirt"
        const val few = "dress"
        const val none = "zzz-no-results"
    }

    object Barcode {
        const val value = "2000001000007"
        const val productSlug = "alfie-oxford-shirt"
        const val variantId = "mock-variant-alfie-oxford-shirt-white-xs"
    }

    object InStockVariants {
        const val productSlug = "alfie-oxford-shirt"
        val variantIds = listOf(
            "mock-variant-alfie-oxford-shirt-white-xs",
            "mock-variant-alfie-oxford-shirt-white-s"
        )
    }

    const val unknownBarcode = "0000000000000"

    val relatedProductSlugs = listOf(
        "alfie-poplin-shirt",
        "alfie-linen-camp-shirt",
        "alfie-satin-slip-dress",
        "northwind-wide-leg-trousers",
        "ember-cropped-cardigan",
        "alfie-oxford-button-down-shirt",
        "alfie-slim-chinos",
        "alfie-harrington-jacket"
    )

    object Timestamps {
        const val defaultProductCreatedAt = "2025-03-03T09:30:00Z"
        const val defaultProductUpdatedAt = "2026-02-10T10:30:00Z"
    }
}
