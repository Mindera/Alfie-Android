package au.com.alfie.ecomm.data.database.di

import android.content.Context
import androidx.room.Room
import au.com.alfie.ecomm.data.database.FeatureToggleDatabase
import au.com.alfie.ecomm.data.database.InMemoryDatabase
import au.com.alfie.ecomm.data.database.PersistentDatabase
import au.com.alfie.ecomm.data.database.navigation.NavigationEntryDao
import au.com.alfie.ecomm.data.database.search.FeatureToggleDao
import au.com.alfie.ecomm.data.database.search.RecentSearchDao
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
