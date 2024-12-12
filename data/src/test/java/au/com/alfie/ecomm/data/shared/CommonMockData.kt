package au.com.alfie.ecomm.data.shared

import au.com.alfie.ecomm.graphql.fragment.AttributesInfo
import au.com.alfie.ecomm.graphql.fragment.HierarchyItemInfo
import au.com.alfie.ecomm.repository.shared.model.Attribute
import au.com.alfie.ecomm.repository.shared.model.HierarchyItem

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
