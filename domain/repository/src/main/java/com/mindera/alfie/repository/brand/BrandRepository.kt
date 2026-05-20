package com.mindera.alfie.repository.brand

import com.mindera.alfie.repository.result.RepositoryResult
import com.mindera.alfie.repository.shared.model.Brand

interface BrandRepository {

    suspend fun getBrands(): RepositoryResult<List<Brand>>
}
