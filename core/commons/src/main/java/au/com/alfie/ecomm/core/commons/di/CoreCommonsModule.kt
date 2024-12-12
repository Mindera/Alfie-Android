package au.com.alfie.ecomm.core.commons.di

import android.content.Context
import au.com.alfie.ecomm.core.commons.R
import au.com.alfie.ecomm.core.commons.dispatcher.DefaultDispatcherProvider
import au.com.alfie.ecomm.core.commons.dispatcher.DispatcherProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CoreCommonsModule {

    companion object {
        const val BASE_URL = "base_url"

        @Provides
        @Singleton
        @SingletonIoScope
        fun provideSingletonIoScope(dispatcherProvider: DispatcherProvider): CoroutineScope =
            CoroutineScope(SupervisorJob() + dispatcherProvider.io())

        @Provides
        @Named(BASE_URL)
        fun provideBaseUrl(@ApplicationContext context: Context): String = context.getString(R.string.base_url)
    }

    @Binds
    internal abstract fun bindDispatcherProvider(dispatcherProvider: DefaultDispatcherProvider): DispatcherProvider
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SingletonIoScope
