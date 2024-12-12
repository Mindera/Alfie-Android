package au.com.alfie.ecomm.domain.usecase.brand

import au.com.alfie.ecomm.domain.UseCaseInteractor
import au.com.alfie.ecomm.domain.UseCaseResult
import au.com.alfie.ecomm.repository.brand.BrandRepository
import au.com.alfie.ecomm.repository.shared.model.Brand
import javax.inject.Inject

class GetBrandsUseCase @Inject constructor(
    private val brandRepository: BrandRepository
) : UseCaseInteractor {

    suspend operator fun invoke(): UseCaseResult<List<Brand>> = run(brandRepository.getBrands())
}
