package com.mindera.alfie.feature.startup.di

import com.mindera.alfie.feature.startup.loader.FeedbackLoader
import com.mindera.alfie.feature.startup.loader.SampleStartUpLoader
import com.mindera.alfie.feature.startup.loader.StartUpLoader
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@InstallIn(SingletonComponent::class)
@Module
internal interface StartUpModule {

    @Binds
    @IntoSet
    fun bindSampleStartUpLoader(sampleStartUpLoader: SampleStartUpLoader): StartUpLoader

    @Binds
    @IntoSet
    fun bindFeedbackLoader(feedbackLoader: FeedbackLoader): StartUpLoader
}
