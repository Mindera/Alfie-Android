package com.mindera.alfie.domain.usecase.brand

import com.mindera.alfie.domain.UseCaseInteractor
import com.mindera.alfie.domain.UseCaseResult
import com.mindera.alfie.repository.brand.BrandRepository
import com.mindera.alfie.repository.shared.model.Brand
import javax.inject.Inject

class GetBrandsUseCase @Inject constructor(
    private val brandRepository: BrandRepository
) : UseCaseInteractor {

    suspend operator fun invoke(): UseCaseResult<List<Brand>> = run(brandRepository.getBrands())
}
