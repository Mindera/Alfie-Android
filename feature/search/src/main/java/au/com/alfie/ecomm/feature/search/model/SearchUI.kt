package au.com.alfie.ecomm.feature.search.model

internal data class SearchUI(
    val searchTerm: String,
    val keywords: List<KeywordSuggestionUI>,
    val brands: List<BrandSuggestionUI>,
    val products: List<ProductSuggestionUI>
)
