package com.mindera.alfie.feature.bag.models

/**
 * Stock notice attached to a bag line — Figma "Inline Message" states (node `673:90047`).
 *
 * Kept as a type rather than a formatted string so the factory stays free of Android resources and
 * the copy (including the low-stock plural) is resolved where a composition context exists.
 */
sealed interface BagItemNotice {

    /** Fewer than the low-stock threshold left. */
    data class LowStock(val remaining: Int) : BagItemNotice

    /** The variant is out of stock; the row also renders dimmed. */
    data object Unavailable : BagItemNotice
}
