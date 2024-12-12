package au.com.alfie.ecomm.core.navigation.arguments.productlist

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
    }

    @Parcelize
    data class Search(val query: String) : ProductListType
}
