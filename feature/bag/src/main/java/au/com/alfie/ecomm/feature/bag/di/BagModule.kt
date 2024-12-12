package au.com.alfie.ecomm.feature.bag.di

import au.com.alfie.ecomm.core.deeplink.DeeplinkGroup
import au.com.alfie.ecomm.feature.bag.BagDeeplinks
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@InstallIn(SingletonComponent::class)
@Module
internal interface BagModule {

    @Binds
    @IntoSet
    fun bindBagDeeplinks(bagDeeplinks: BagDeeplinks): DeeplinkGroup
}
