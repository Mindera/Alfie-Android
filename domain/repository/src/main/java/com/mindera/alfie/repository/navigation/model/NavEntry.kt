package com.mindera.alfie.repository.navigation.model

data class NavEntry(
    val id: Int,
    val title: String,
    val type: NavItemType,
    val url: String?,
    val items: List<NavEntry> = emptyList()
)
