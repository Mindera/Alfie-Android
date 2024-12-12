package au.com.alfie.ecomm.data.productlist.di

import au.com.alfie.ecomm.data.productlist.repository.ProductListRepositoryImpl
import au.com.alfie.ecomm.data.productlist.service.ProductListService
import au.com.alfie.ecomm.data.productlist.service.ProductListServiceImpl
import au.com.alfie.ecomm.repository.productlist.ProductListRepository
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
