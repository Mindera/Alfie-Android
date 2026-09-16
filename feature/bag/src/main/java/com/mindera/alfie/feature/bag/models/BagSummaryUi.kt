package com.mindera.alfie.feature.bag.models

import androidx.compose.runtime.Stable

/** Purchase summary figures shown beneath the bag list. */
@Stable
data class BagSummaryUi(
    val totalFormatted: String
)
