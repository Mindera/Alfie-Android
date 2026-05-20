package com.mindera.alfie.core.navigation.arguments

import com.mindera.alfie.core.commons.string.StringResource

fun categoryNavArgs(
    id: Int,
    title: StringResource
): CategoryNavArgs = CategoryNavArgs(
    id = id,
    title = title
)

data class CategoryNavArgs(
    val id: Int,
    val title: StringResource
)
