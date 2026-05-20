package com.mindera.alfie.data.brand.mapper

import com.mindera.alfie.data.shared.mapper.toDomain
import com.mindera.alfie.graphql.BrandsQuery
import com.mindera.alfie.repository.shared.model.Brand

internal fun List<BrandsQuery.Brand>.toDomain(): List<Brand> = map { it.brandInfo.toDomain() }
