package com.mindera.alfie.feature.shop.category.model

import com.mindera.alfie.core.commons.string.StringResource

internal data class CategoryEntryUI(
    val id: Int,
    val title: StringResource,
    val path: String
) {

    companion object {

        val EMPTY = CategoryEntryUI(
            id = 0,
            title = StringResource.EMPTY,
            path = ""
        )
    }
}
