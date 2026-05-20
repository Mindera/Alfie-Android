package com.mindera.alfie.data.brand.repository

import com.mindera.alfie.data.brand.mapper.toDomain
import com.mindera.alfie.data.brand.service.BrandService
import com.mindera.alfie.data.toRepositoryResult
import com.mindera.alfie.repository.brand.BrandRepository
import com.mindera.alfie.repository.result.RepositoryResult
import com.mindera.alfie.repository.shared.model.Brand
import javax.inject.Inject

internal class BrandRepositoryImpl @Inject constructor(
    private val brandService: BrandService
) : BrandRepository {

    override suspend fun getBrands(): RepositoryResult<List<Brand>> =
        brandService.getBrands()
            .mapCatching { it.brands.toDomain() }
            .toRepositoryResult()
}
