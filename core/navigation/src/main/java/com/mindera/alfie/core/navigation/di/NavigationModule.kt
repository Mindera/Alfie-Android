package com.mindera.alfie.core.navigation.di

import com.mindera.alfie.core.navigation.NestedNavGraph
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.ElementsIntoSet

@Module
@InstallIn(SingletonComponent::class)
internal object NavigationModule {

    @Provides
    @ElementsIntoSet
    fun provideEmptyNestedNavGraph(): Set<NestedNavGraph> = emptySet()
}
