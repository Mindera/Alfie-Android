package au.com.alfie.ecomm.data.brand.repository

import au.com.alfie.ecomm.data.brand.mapper.toDomain
import au.com.alfie.ecomm.data.brand.service.BrandService
import au.com.alfie.ecomm.data.toRepositoryResult
import au.com.alfie.ecomm.repository.brand.BrandRepository
import au.com.alfie.ecomm.repository.result.RepositoryResult
import au.com.alfie.ecomm.repository.shared.model.Brand
import javax.inject.Inject

internal class BrandRepositoryImpl @Inject constructor(
    private val brandService: BrandService
) : BrandRepository {

    override suspend fun getBrands(): RepositoryResult<List<Brand>> =
        brandService.getBrands()
            .mapCatching { it.brands.toDomain() }
            .toRepositoryResult()
}
