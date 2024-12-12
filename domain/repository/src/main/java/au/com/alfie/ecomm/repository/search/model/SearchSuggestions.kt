package au.com.alfie.ecomm.repository.search.model

data class SearchSuggestions(
    val keywords: List<SearchSuggestion.Keyword>,
    val brands: List<SearchSuggestion.Brand>,
    val products: List<SearchSuggestion.Product>
) {

    fun isEmpty(): Boolean = keywords.isEmpty() && brands.isEmpty() && products.isEmpty()
}
