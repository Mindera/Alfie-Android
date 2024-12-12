package au.com.alfie.ecomm.repository.search.model

sealed class RecentSearch(open val searchTerm: String) {

    data class Query(override val searchTerm: String) : RecentSearch(searchTerm)

    data class Brand(
        override val searchTerm: String,
        val slug: String
    ) : RecentSearch(searchTerm)
}
