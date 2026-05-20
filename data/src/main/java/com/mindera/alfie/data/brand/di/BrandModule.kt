package com.mindera.alfie.data.brand.di

import com.mindera.alfie.data.brand.repository.BrandRepositoryImpl
import com.mindera.alfie.data.brand.service.BrandService
import com.mindera.alfie.data.brand.service.BrandServiceImpl
import com.mindera.alfie.repository.brand.BrandRepository
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
