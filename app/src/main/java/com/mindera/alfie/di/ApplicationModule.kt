package com.mindera.alfie.di

import android.content.Context
import com.mindera.alfie.BuildConfig
import com.mindera.alfie.R
import com.mindera.alfie.core.environment.model.BuildConfiguration
import com.mindera.alfie.core.environment.model.Environment
import com.mindera.alfie.core.environment.model.Environments
import com.mindera.alfie.core.navigation.DirectionProvider
import com.mindera.alfie.navigation.DirectionProviderImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class ApplicationModule {

    companion object {

        @Provides
        fun provideBuildConfiguration(
            @ApplicationContext context: Context
        ) = BuildConfiguration(
            appName = context.getString(R.string.app_name),
            versionName = BuildConfig.VERSION_NAME,
            applicationId = BuildConfig.APPLICATION_ID,
            debug = BuildConfig.DEBUG,
            environments = Environments(
                dev = Environment.Dev(graphQLUrl = BuildConfig.DevGraphQL, legacyGraphQLUrl = BuildConfig.DevLegacyGraphQL, webUrl = BuildConfig.DevWeb),
                preProd = Environment.PreProd(graphQLUrl = BuildConfig.PreProdGraphQL, legacyGraphQLUrl = BuildConfig.PreProdLegacyGraphQL, webUrl = BuildConfig.PreProdWeb),
                prod = Environment.Prod(graphQLUrl = BuildConfig.ProdGraphQL, legacyGraphQLUrl = BuildConfig.ProdLegacyGraphQL, webUrl = BuildConfig.ProdWeb)
            )
        )
    }

    @Binds
    abstract fun bindDirectionProvider(directionProviderImpl: DirectionProviderImpl): DirectionProvider
}
