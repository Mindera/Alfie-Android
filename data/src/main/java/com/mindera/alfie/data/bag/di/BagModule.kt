package com.mindera.alfie.data.bag.di

import com.mindera.alfie.data.bag.BagRepositoryImpl
import com.mindera.alfie.repository.bag.BagRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface BagModule {

    @Binds
    @Singleton
    fun bindBagRepository(bagRepositoryImpl: BagRepositoryImpl): BagRepository
}
