package au.com.alfie.ecomm.core.navigation.di

import au.com.alfie.ecomm.core.navigation.NestedNavGraph
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
