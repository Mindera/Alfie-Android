package com.mindera.alfie.data

import com.mindera.alfie.data.shared.mapper.toDomain
import com.mindera.alfie.graphql.fragment.ColorInfo
import com.mindera.alfie.repository.product.model.Color

internal fun ColorInfo.toDomain() = Color(
    id = id,
    name = name,
    swatch = swatch?.imageInfo?.toDomain(),
    media = media?.mapNotNull { it?.mediaInfo?.toDomain() }
)