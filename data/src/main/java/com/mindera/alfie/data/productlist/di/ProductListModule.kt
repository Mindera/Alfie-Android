package com.mindera.alfie.data.productlist.di

import com.mindera.alfie.data.productlist.repository.ProductListRepositoryImpl
import com.mindera.alfie.data.productlist.service.ProductListService
import com.mindera.alfie.data.productlist.service.ProductListServiceImpl
import com.mindera.alfie.repository.productlist.ProductListRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class ProductListModule {

    @Binds
    abstract fun bindProductListService(
        productListServiceImpl: ProductListServiceImpl
    ): ProductListService

    @Binds
    abstract fun bindProductListRepository(
        productListRepositoryImpl: ProductListRepositoryImpl
    ): ProductListRepository
}
