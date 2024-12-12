package au.com.alfie.ecomm.data.brand.di

import au.com.alfie.ecomm.data.brand.repository.BrandRepositoryImpl
import au.com.alfie.ecomm.data.brand.service.BrandService
import au.com.alfie.ecomm.data.brand.service.BrandServiceImpl
import au.com.alfie.ecomm.repository.brand.BrandRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class BrandModule {

    @Binds
    abstract fun bindBrandService(brandServiceImpl: BrandServiceImpl): BrandService

    @Binds
    abstract fun bindBrandRepository(brandRepositoryImpl: BrandRepositoryImpl): BrandRepository
}
