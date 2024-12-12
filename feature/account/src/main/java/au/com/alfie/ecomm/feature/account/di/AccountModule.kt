package au.com.alfie.ecomm.feature.account.di

import au.com.alfie.ecomm.core.deeplink.DeeplinkGroup
import au.com.alfie.ecomm.feature.account.AccountDeeplinks
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@InstallIn(SingletonComponent::class)
@Module
internal interface AccountModule {

    @Binds
    @IntoSet
    fun bindAccountDeeplinks(accountDeeplinks: AccountDeeplinks): DeeplinkGroup
}
