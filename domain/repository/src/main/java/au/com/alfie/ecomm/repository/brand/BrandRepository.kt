package au.com.alfie.ecomm.repository.brand

import au.com.alfie.ecomm.repository.result.RepositoryResult
import au.com.alfie.ecomm.repository.shared.model.Brand

interface BrandRepository {

    suspend fun getBrands(): RepositoryResult<List<Brand>>
}
