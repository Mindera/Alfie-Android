package au.com.alfie.ecomm.data.shared.mapper

import au.com.alfie.ecomm.graphql.fragment.SizeContainer
import au.com.alfie.ecomm.graphql.fragment.SizeGuideInfo
import au.com.alfie.ecomm.graphql.fragment.SizeGuideInfoTree
import au.com.alfie.ecomm.repository.shared.model.Size
import au.com.alfie.ecomm.repository.shared.model.SizeGuide

internal fun SizeContainer.toDomain() = Size(
    id = sizeInfo.id,
    description = sizeInfo.description,
    scale = sizeInfo.scale,
    value = sizeInfo.value,
    sizeGuide = sizeGuide?.sizeGuideInfoTree?.toDomain()
)

private fun SizeGuideInfoTree.toDomain() = SizeGuide(
    id = sizeGuideInfo.id,
    description = sizeGuideInfo.description,
    name = sizeGuideInfo.name,
    sizes = sizes.map { it.toDomain() }
)

private fun SizeGuideInfoTree.Size.toDomain() = Size(
    id = sizeInfo.id,
    description = sizeInfo.description,
    scale = sizeInfo.scale,
    value = sizeInfo.value,
    sizeGuide = sizeGuide?.sizeGuideInfo?.toDomain()
)

private fun SizeGuideInfo.toDomain() = SizeGuide(
    id = id,
    name = name,
    description = description,
    sizes = emptyList()
)
