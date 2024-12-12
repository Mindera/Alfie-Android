package au.com.alfie.ecomm.feature.shop.brand.model

internal sealed interface BrandEntryUI {

    data class Entry(
        val id: String,
        val name: String,
        val slug: String
    ) : BrandEntryUI {

        companion object {

            val EMPTY = Entry(
                id = "",
                name = "",
                slug = ""
            )
        }
    }

    data class Divider(val character: Char) : BrandEntryUI
}
