package com.mindera.alfie.repository.search.model

sealed class RecentSearch(open val searchTerm: String) {

    data class Query(override val searchTerm: String) : RecentSearch(searchTerm)

    /**
     * @param searchTerm the brand's display name.
     * @param key the opaque brand key from a search suggestion, used to list the brand again.
     */
    data class Brand(
        override val searchTerm: String,
        val key: String
    ) : RecentSearch(searchTerm)
}
