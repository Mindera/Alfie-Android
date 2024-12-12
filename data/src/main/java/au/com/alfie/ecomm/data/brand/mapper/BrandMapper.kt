package au.com.alfie.ecomm.data.brand.mapper

import au.com.alfie.ecomm.data.shared.mapper.toDomain
import au.com.alfie.ecomm.graphql.BrandsQuery
import au.com.alfie.ecomm.repository.shared.model.Brand

internal fun List<BrandsQuery.Brand>.toDomain(): List<Brand> = map { it.brandInfo.toDomain() }
