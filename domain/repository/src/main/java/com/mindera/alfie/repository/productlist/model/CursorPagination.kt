package com.mindera.alfie.repository.productlist.model

data class CursorPagination(
    val endCursor: String?,
    val hasNextPage: Boolean,
    val totalCount: Int
)
