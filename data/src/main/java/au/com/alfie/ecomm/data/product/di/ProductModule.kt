package au.com.alfie.ecomm.data.product.di

import au.com.alfie.ecomm.data.product.repository.ProductRepositoryImpl
import au.com.alfie.ecomm.data.product.service.ProductService
import au.com.alfie.ecomm.data.product.service.ProductServiceImpl
import au.com.alfie.ecomm.repository.product.ProductRepository
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
