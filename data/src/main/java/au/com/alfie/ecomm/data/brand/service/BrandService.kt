package au.com.alfie.ecomm.data.brand.service

import au.com.alfie.ecomm.graphql.BrandsQuery

internal interface BrandService {

    suspend fun getBrands(): Result<BrandsQuery.Data>
}
