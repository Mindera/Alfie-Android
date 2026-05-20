package com.mindera.alfie.data.shared.mapper

import com.mindera.alfie.graphql.fragment.AttributesInfo
import com.mindera.alfie.graphql.fragment.BrandInfo
import com.mindera.alfie.graphql.fragment.HierarchyItemInfo
import com.mindera.alfie.repository.shared.model.Attribute
import com.mindera.alfie.repository.shared.model.Brand
import com.mindera.alfie.repository.shared.model.HierarchyItem

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
