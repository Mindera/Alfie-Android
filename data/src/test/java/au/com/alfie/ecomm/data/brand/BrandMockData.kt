package au.com.alfie.ecomm.data.brand

import au.com.alfie.ecomm.graphql.BrandsQuery
import au.com.alfie.ecomm.graphql.fragment.BrandInfo

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
