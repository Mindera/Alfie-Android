package com.mindera.alfie.data.shared

import com.mindera.alfie.graphql.fragment.SizeContainer
import com.mindera.alfie.graphql.fragment.SizeGuideInfo
import com.mindera.alfie.graphql.fragment.SizeGuideInfoTree
import com.mindera.alfie.graphql.fragment.SizeInfo
import com.mindera.alfie.repository.shared.model.Size
import com.mindera.alfie.repository.shared.model.SizeGuide

internal val sizeContainerData = SizeContainer(
    __typename = "",
    sizeGuide = SizeContainer.SizeGuide(
        __typename = "",
        SizeGuideInfoTree(
            __typename = "",
            sizes = listOf(
                SizeGuideInfoTree.Size(
                    __typename = "",
                    sizeGuide = SizeGuideInfoTree.SizeGuide(
                        __typename = "",
                        sizeGuideInfo = SizeGuideInfo(
                            id = "id",
                            name = "name",
                            description = null
                        )
                    ),
                    sizeInfo = SizeInfo(
                        id = "25927",
                        value = "10 AU",
                        scale = null,
                        description = "10 AU"
                    )
                )
            ),
            sizeGuideInfo = SizeGuideInfo(
                id = "id",
                name = "name",
                description = null
            )
        )
    ),
    sizeInfo = SizeInfo(
        id = "25927",
        value = "10 AU",
        scale = null,
        description = "10 AU"
    )
)

internal val size = Size(
    id = "25927",
    value = "10 AU",
    scale = null,
    description = "10 AU",
    sizeGuide = SizeGuide(
        id = "id",
        name = "name",
        description = null,
        sizes = listOf(
            Size(
                id = "25927",
                value = "10 AU",
                scale = null,
                description = "10 AU",
                sizeGuide = SizeGuide(
                    id = "id",
                    name = "name",
                    description = null,
                    sizes = emptyList()
                )
            )
        )
    )
)
