package com.mindera.alfie.data.brand.service

import com.mindera.alfie.graphql.BrandsQuery

internal interface BrandService {

    suspend fun getBrands(): Result<BrandsQuery.Data>
}
