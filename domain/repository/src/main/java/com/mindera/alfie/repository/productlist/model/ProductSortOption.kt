package com.mindera.alfie.repository.productlist.model

enum class ProductSortOption {
    /** Maps to BFF ProductSortEnum.NEWEST */
    RECOMMENDED,

    /** Maps to BFF ProductSortEnum.RECENTLY_UPDATED */
    MOST_RECENT,

    /** Maps to BFF ProductSortEnum.PRICE_ASC */
    LOWEST_PRICE,

    /** Maps to BFF ProductSortEnum.PRICE_DESC */
    HIGHEST_PRICE
}
