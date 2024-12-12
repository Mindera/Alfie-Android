package au.com.alfie.ecomm.feature.webview.di

import au.com.alfie.ecomm.core.deeplink.DeeplinkGroup
import au.com.alfie.ecomm.feature.webview.WebViewDeeplinks
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@InstallIn(SingletonComponent::class)
@Module
internal interface WebViewModule {

    @Binds
    @IntoSet
    fun bindWebViewDeeplinks(webViewDeeplinks: WebViewDeeplinks): DeeplinkGroup
}
