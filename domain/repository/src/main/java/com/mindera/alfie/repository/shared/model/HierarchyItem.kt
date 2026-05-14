package com.mindera.alfie.repository.shared.model

data class HierarchyItem(
    val categoryId: String,
    val name: String,
    val parent: HierarchyItem?,
    val slug: String
)
