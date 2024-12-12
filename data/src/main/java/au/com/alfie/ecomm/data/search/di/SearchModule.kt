package au.com.alfie.ecomm.data.search.di

import au.com.alfie.ecomm.data.search.repository.SearchRepositoryImpl
import au.com.alfie.ecomm.data.search.service.SearchService
import au.com.alfie.ecomm.data.search.service.SearchServiceImpl
import au.com.alfie.ecomm.repository.search.SearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class SearchModule {

    @Binds
    abstract fun bindSearchService(searchServiceImpl: SearchServiceImpl): SearchService

    @Binds
    abstract fun bindSearchRepository(searchRepositoryImpl: SearchRepositoryImpl): SearchRepository
}
