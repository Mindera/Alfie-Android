package au.com.alfie.ecomm.data.shared.mapper

import au.com.alfie.ecomm.graphql.fragment.AttributesInfo
import au.com.alfie.ecomm.graphql.fragment.BrandInfo
import au.com.alfie.ecomm.graphql.fragment.HierarchyItemInfo
import au.com.alfie.ecomm.repository.shared.model.Attribute
import au.com.alfie.ecomm.repository.shared.model.Brand
import au.com.alfie.ecomm.repository.shared.model.HierarchyItem

internal fun AttributesInfo.toDomain() = Attribute(
    key = key,
    value = value
)

internal fun HierarchyItemInfo.toDomain() = HierarchyItem(
    categoryId = categoryId,
    name = name,
    slug = slug,
    parent = null
)

internal fun BrandInfo.toDomain() = Brand(
    id = id,
    name = name,
    slug = slug
)
