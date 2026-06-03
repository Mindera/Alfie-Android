package com.mindera.alfie.feature.plp.model

/**
 * Represents a single quick-filter chip shown as a horizontally-scrollable row on the PLP.
 *
 * Chips are populated from BFF filter facets. Selecting a chip toggles the filter on/off;
 * multiple chips can be selected simultaneously.
 *
 * TODO: Populate from BFF once ProductListResponse exposes filter facets.
 */
internal data class QuickFilterChipUI(
    val id: String,
    val label: String,
    val isSelected: Boolean
)
