package com.mindera.alfie.feature.bag.models

import androidx.compose.runtime.Stable

/**
 * Purchase summary figures shown beneath the bag list. [totalFormatted] is null when the bag offers
 * no currency to format it in, and the total row is then left out rather than drawn against a blank.
 */
@Stable
data class BagSummaryUi(
    val totalFormatted: String?
)
