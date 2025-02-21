package au.com.alfie.ecomm.data.bag.di

import au.com.alfie.ecomm.data.bag.BagRepositoryImpl
import au.com.alfie.ecomm.repository.bag.BagRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class BagModule {

    @Binds
    @Singleton
    abstract fun bindBagRepository(bagRepositoryImpl: BagRepositoryImpl): BagRepository
}
