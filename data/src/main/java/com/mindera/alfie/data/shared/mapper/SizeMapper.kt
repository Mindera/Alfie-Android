package com.mindera.alfie.data.shared.mapper

import com.mindera.alfie.graphql.fragment.SizeContainer
import com.mindera.alfie.graphql.fragment.SizeGuideInfo
import com.mindera.alfie.graphql.fragment.SizeGuideInfoTree
import com.mindera.alfie.repository.shared.model.Size
import com.mindera.alfie.repository.shared.model.SizeGuide

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
