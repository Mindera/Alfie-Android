package com.mindera.alfie.feature.account.di

import com.mindera.alfie.core.deeplink.DeeplinkGroup
import com.mindera.alfie.feature.account.AccountDeeplinks
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
