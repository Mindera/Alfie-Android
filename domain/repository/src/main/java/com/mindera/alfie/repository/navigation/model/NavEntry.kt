package com.mindera.alfie.repository.navigation.model

data class NavEntry(
    val id: Int,
    val title: String,
    val type: NavItemType,
    val url: String?,
    /**
     * Whether this entry drills into a sub-category list. Consumers must read this rather than
     * inspecting [items]: the repository resolves children by parent id on demand, so [items] is
     * empty on everything it returns.
     */
    val hasChildren: Boolean = false,
    val items: List<NavEntry> = emptyList()
)
