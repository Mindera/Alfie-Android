package com.mindera.alfie.data.datastore.di

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import com.mindera.alfie.data.datastore.DebugPreferencesProto
import com.mindera.alfie.data.datastore.UserPreferencesProto
import com.mindera.alfie.data.datastore.debug.DebugPreferencesDataSource
import com.mindera.alfie.data.datastore.debug.DebugPreferencesDataSourceImpl
import com.mindera.alfie.data.datastore.debug.DebugPreferencesProtoSerializer
import com.mindera.alfie.data.datastore.user.UserPreferencesDataSource
import com.mindera.alfie.data.datastore.user.UserPreferencesDataSourceImpl
import com.mindera.alfie.data.datastore.user.UserPreferencesProtoSerializer
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface DataStoreModule {

    companion object {
        private const val DEBUG_SETTINGS_FILE_NAME = "debug_settings.pb"
        private const val USER_SETTINGS_FILE_NAME = "app_session_settings.pb"

        @Provides
        @Singleton
        fun providesDebugPreferencesDataStore(
            @ApplicationContext context: Context
        ): DataStore<DebugPreferencesProto> {
            val corruptionHandler: (CorruptionException) -> DebugPreferencesProto = {
                DebugPreferencesProto.getDefaultInstance()
            }

            return DataStoreFactory.create(
                serializer = DebugPreferencesProtoSerializer(),
                corruptionHandler = ReplaceFileCorruptionHandler(corruptionHandler)
            ) {
                context.dataStoreFile(DEBUG_SETTINGS_FILE_NAME)
            }
        }

        @Provides
        @Singleton
        fun providesUserPreferencesDataStore(
            @ApplicationContext context: Context
        ): DataStore<UserPreferencesProto> {
            val corruptionHandler: (CorruptionException) -> UserPreferencesProto = {
                UserPreferencesProto.getDefaultInstance()
            }

            return DataStoreFactory.create(
                serializer = UserPreferencesProtoSerializer(),
                corruptionHandler = ReplaceFileCorruptionHandler(corruptionHandler)
            ) {
                context.dataStoreFile(USER_SETTINGS_FILE_NAME)
            }
        }
    }

    @Binds
    fun bindDebugPreferencesDataSource(
        debugPreferencesDataSourceImpl: DebugPreferencesDataSourceImpl
    ): DebugPreferencesDataSource

    @Binds
    fun bindUserPreferencesDataSource(
        userPreferencesDataSourceImpl: UserPreferencesDataSourceImpl
    ): UserPreferencesDataSource
}
