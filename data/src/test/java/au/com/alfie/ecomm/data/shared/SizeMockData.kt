package au.com.alfie.ecomm.data.shared

import au.com.alfie.ecomm.graphql.fragment.SizeContainer
import au.com.alfie.ecomm.graphql.fragment.SizeGuideInfo
import au.com.alfie.ecomm.graphql.fragment.SizeGuideInfoTree
import au.com.alfie.ecomm.graphql.fragment.SizeInfo
import au.com.alfie.ecomm.repository.shared.model.Size
import au.com.alfie.ecomm.repository.shared.model.SizeGuide

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
