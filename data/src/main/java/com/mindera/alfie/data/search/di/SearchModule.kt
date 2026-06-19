package com.mindera.alfie.data.search.di

import com.mindera.alfie.data.search.repository.SearchRepositoryImpl
import com.mindera.alfie.repository.search.SearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class SearchModule {

    @Binds
    abstract fun bindSearchRepository(searchRepositoryImpl: SearchRepositoryImpl): SearchRepository
}
