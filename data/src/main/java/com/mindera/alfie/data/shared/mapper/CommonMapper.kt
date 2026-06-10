package com.mindera.alfie.data.shared.mapper

import com.mindera.alfie.graphql.fragment.BrandInfo
import com.mindera.alfie.repository.shared.model.Brand

internal fun BrandInfo.toDomain() = Brand(
    id = id,
    name = name,
    slug = slug
)
