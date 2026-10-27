package com.mindera.alfie.core.navigation.arguments.productlist

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed interface ProductListType : Parcelable {

    sealed interface Category : ProductListType {

        @Parcelize
        data class Slug(val slug: String) : Category

        @Parcelize
        data class Id(val id: String) : Category
    }

    sealed interface Brand : ProductListType {

        @Parcelize
        data class Slug(val slug: String) : Brand

        @Parcelize
        data class Id(val id: String) : Brand

        /**
         * A brand from a BFF search suggestion.
         *
         * @param key opaque, platform-resolved brand key the BFF filters on via `brandNames`.
         * @param name the brand's display name.
         */
        @Parcelize
        data class Key(
            val key: String,
            val name: String
        ) : Brand
    }

    @Parcelize
    data class Search(val query: String) : ProductListType
}
