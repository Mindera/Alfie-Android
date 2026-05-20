package com.mindera.alfie.feature.webview.di

import com.mindera.alfie.core.deeplink.DeeplinkGroup
import com.mindera.alfie.feature.webview.WebViewDeeplinks
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
