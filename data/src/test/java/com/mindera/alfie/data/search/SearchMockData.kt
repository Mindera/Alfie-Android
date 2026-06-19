package com.mindera.alfie.data.search

import com.mindera.alfie.data.database.search.model.RecentSearchEntity
import com.mindera.alfie.repository.search.model.RecentSearch

internal val listRecentSearchEntity = listOf(
    RecentSearchEntity(
        searchTerm = "search #1",
        searchTimestamp = 12345L,
        type = RecentSearchEntity.Type.QUERY
    ),
    RecentSearchEntity(
        searchTerm = "search #2",
        searchTimestamp = 123456L,
        type = RecentSearchEntity.Type.BRAND,
        slug = "brand-slug"
    )
)

internal val listRecentSearch = listOf(
    RecentSearch.Query("search #1"),
    RecentSearch.Brand(
        searchTerm = "search #2",
        slug = "brand-slug"
    )
)
