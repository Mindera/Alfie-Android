package au.com.alfie.ecomm.data.search.mapper

import au.com.alfie.ecomm.data.database.search.model.RecentSearchEntity
import au.com.alfie.ecomm.data.database.search.model.RecentSearchEntity.Type.BRAND
import au.com.alfie.ecomm.data.database.search.model.RecentSearchEntity.Type.QUERY
import au.com.alfie.ecomm.repository.search.model.RecentSearch
import java.lang.System.currentTimeMillis

internal fun RecentSearch.toEntity() = when (this) {
    is RecentSearch.Query -> RecentSearchEntity(
        searchTerm = searchTerm,
        searchTimestamp = currentTimeMillis(),
        type = QUERY
    )
    is RecentSearch.Brand -> RecentSearchEntity(
        searchTerm = searchTerm,
        searchTimestamp = currentTimeMillis(),
        type = BRAND,
        slug = slug
    )
}

internal fun List<RecentSearchEntity>.toDomain(): List<RecentSearch> = map {
    it.toDomain()
}

private fun RecentSearchEntity.toDomain() = when (type) {
    QUERY -> RecentSearch.Query(searchTerm = searchTerm)
    BRAND -> RecentSearch.Brand(
        searchTerm = searchTerm,
        slug = slug.orEmpty()
    )
}
