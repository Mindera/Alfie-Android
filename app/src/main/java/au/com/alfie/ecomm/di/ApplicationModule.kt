package au.com.alfie.ecomm.di

import android.content.Context
import au.com.alfie.ecomm.BuildConfig
import au.com.alfie.ecomm.R
import au.com.alfie.ecomm.core.environment.model.BuildConfiguration
import au.com.alfie.ecomm.core.environment.model.Environment
import au.com.alfie.ecomm.core.environment.model.Environments
import au.com.alfie.ecomm.core.navigation.DirectionProvider
import au.com.alfie.ecomm.navigation.DirectionProviderImpl
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
                dev = Environment.Dev(graphQLUrl = BuildConfig.DevGraphQL, webUrl = BuildConfig.DevWeb),
                preProd = Environment.PreProd(graphQLUrl = BuildConfig.PreProdGraphQL, webUrl = BuildConfig.PreProdWeb),
                prod = Environment.Prod(graphQLUrl = BuildConfig.ProdGraphQL, webUrl = BuildConfig.ProdWeb)
            )
        )
    }

    @Binds
    abstract fun bindDirectionProvider(directionProviderImpl: DirectionProviderImpl): DirectionProvider
}
