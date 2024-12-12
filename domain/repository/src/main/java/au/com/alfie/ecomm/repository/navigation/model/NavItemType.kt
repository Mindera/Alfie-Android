package au.com.alfie.ecomm.repository.navigation.model

enum class NavItemType {
    HOME,
    LISTING,
    PRODUCT,
    SEARCH,
    PAGE,
    EXTERNAL_HTTP,
    UNKNOWN;

    companion object {

        fun from(value: String): NavItemType =
            entries.firstOrNull { it.name == value } ?: UNKNOWN
    }
}
