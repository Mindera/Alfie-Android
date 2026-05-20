package com.mindera.alfie.data.shared

import com.mindera.alfie.graphql.fragment.AttributesInfo
import com.mindera.alfie.graphql.fragment.HierarchyItemInfo
import com.mindera.alfie.repository.shared.model.Attribute
import com.mindera.alfie.repository.shared.model.HierarchyItem

internal val attributesInfoData = AttributesInfo(
    key = "key",
    value = "value"
)

internal val hierarchyItemInfoData = HierarchyItemInfo(
    categoryId = "1",
    name = "women",
    slug = "/women"
)

internal val attribute = Attribute(
    key = "key",
    value = "value"
)

internal val hierarchy = HierarchyItem(
    categoryId = "1",
    name = "women",
    slug = "/women",
    parent = null
)
