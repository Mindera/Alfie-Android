package com.mindera.alfie.feature.bag.models

import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList

/** Everything the populated Bag screen renders: the grouped lines and their summary. */
@Stable
data class BagContentUi(
    val lines: ImmutableList<BagLineUi>,
    val summary: BagSummaryUi
)
