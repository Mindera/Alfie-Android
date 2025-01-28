package au.com.alfie.ecomm.data

import au.com.alfie.ecomm.data.shared.mapper.toDomain
import au.com.alfie.ecomm.graphql.fragment.ColorInfo
import au.com.alfie.ecomm.repository.product.model.Color

internal fun ColorInfo.toDomain() = Color(
    id = id,
    name = name,
    swatch = swatch?.imageInfo?.toDomain(),
    media = media?.mapNotNull { it?.mediaInfo?.toDomain() }
)