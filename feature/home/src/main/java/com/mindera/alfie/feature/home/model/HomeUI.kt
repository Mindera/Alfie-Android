package com.mindera.alfie.feature.home.model

import com.mindera.alfie.designsystem.component.highlights.HighlightsItem
import kotlinx.collections.immutable.ImmutableList

internal data class HomeUI(
    val userName: String?,
    val membershipDate: String?,
    val highlights: ImmutableList<HighlightsItem>
)
