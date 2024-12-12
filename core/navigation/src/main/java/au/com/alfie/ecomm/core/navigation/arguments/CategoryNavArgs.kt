package au.com.alfie.ecomm.core.navigation.arguments

import au.com.alfie.ecomm.core.commons.string.StringResource

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
