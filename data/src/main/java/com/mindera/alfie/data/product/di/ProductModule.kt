package com.mindera.alfie.data.product.di

import com.mindera.alfie.data.product.repository.ProductRepositoryImpl
import com.mindera.alfie.data.product.service.ProductService
import com.mindera.alfie.data.product.service.ProductServiceImpl
import com.mindera.alfie.repository.product.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class ProductModule {

    @Binds
    abstract fun bindProductService(productServiceImpl: ProductServiceImpl): ProductService

    @Binds
    abstract fun bindProductRepository(productRepositoryImpl: ProductRepositoryImpl): ProductRepository
}
