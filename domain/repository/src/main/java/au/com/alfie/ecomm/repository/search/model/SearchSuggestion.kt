package au.com.alfie.ecomm.repository.search.model

import au.com.alfie.ecomm.repository.product.model.Price
import au.com.alfie.ecomm.repository.shared.model.Media

sealed interface SearchSuggestion {

    data class Keyword(
        val value: String,
        val resultCount: Int
    ) : SearchSuggestion

    data class Brand(
        val name: String,
        val slug: String,
        val resultCount: Int
    ) : SearchSuggestion

    data class Product(
        val id: String,
        val name: String,
        val slug: String,
        val brandName: String,
        val price: Price,
        val media: List<Media.Image>
    ) : SearchSuggestion
}
