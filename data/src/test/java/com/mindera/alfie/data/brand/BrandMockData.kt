package com.mindera.alfie.data.brand

import com.mindera.alfie.graphql.BrandsQuery
import com.mindera.alfie.graphql.fragment.BrandInfo

internal val brandData = BrandsQuery.Data(
    brands = listOf(
        BrandsQuery.Brand(
            __typename = "",
            brandInfo = BrandInfo(
                id = "123",
                name = "QWER",
                slug = "qwer"
            )
        ),
        BrandsQuery.Brand(
            __typename = "",
            brandInfo = BrandInfo(
                id = "234",
                name = "ASDF",
                slug = "asdf"
            )
        )
    )
)
