package com.mindera.alfie.feature.shop.category.model

import com.mindera.alfie.core.commons.string.StringResource

internal data class CategoryEntryUI(
    val id: Int,
    val title: StringResource,
    val path: String,
    /**
     * Whether tapping this entry drills into a sub-category list rather than opening the listing.
     * Single source of truth for both the row's chevron and the tap branch, so the two cannot
     * disagree.
     */
    val hasChildren: Boolean = false
) {

    companion object {

        val EMPTY = CategoryEntryUI(
            id = 0,
            title = StringResource.EMPTY,
            path = ""
        )
    }
}
