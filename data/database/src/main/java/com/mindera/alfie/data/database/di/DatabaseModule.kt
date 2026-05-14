package com.mindera.alfie.data.database.di

import android.content.Context
import androidx.room.Room
import com.mindera.alfie.data.database.FeatureToggleDatabase
import com.mindera.alfie.data.database.InMemoryDatabase
import com.mindera.alfie.data.database.PersistentDatabase
import com.mindera.alfie.data.database.navigation.NavigationEntryDao
import com.mindera.alfie.data.database.search.FeatureToggleDao
import com.mindera.alfie.data.database.search.RecentSearchDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    @Provides
    @Singleton
    fun providePersistentDatabase(@ApplicationContext context: Context): PersistentDatabase =
        Room.databaseBuilder(
            context = context,
            klass = PersistentDatabase::class.java,
            name = "app-database"
        )
            .fallbackToDestructiveMigration() // Todo remove this call before app goes to Production
            .build()

    @Provides
    @Singleton
    fun provideInMemoryDatabase(@ApplicationContext context: Context): InMemoryDatabase =
        Room.inMemoryDatabaseBuilder(
            context = context,
            klass = InMemoryDatabase::class.java
        ).build()

    @Provides
    @Singleton
    fun provideFeatureToggleDatabase(@ApplicationContext context: Context): FeatureToggleDatabase =
        Room.databaseBuilder(
            context = context,
            klass = FeatureToggleDatabase::class.java,
            name = "feature-toggle-database"
        ).build()

    @Provides
    fun provideRecentSearchDao(database: PersistentDatabase): RecentSearchDao = database.recentSearchDao()

    @Provides
    fun provideNavigationEntryDao(database: InMemoryDatabase): NavigationEntryDao = database.navigationEntriesDao()

    @Provides
    fun provideFeatureToggleDao(database: FeatureToggleDatabase): FeatureToggleDao = database.featureToggleDao()
}
