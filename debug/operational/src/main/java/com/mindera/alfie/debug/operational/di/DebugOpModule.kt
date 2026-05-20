package com.mindera.alfie.debug.operational.di

import com.mindera.alfie.core.navigation.NestedNavGraph
import com.mindera.alfie.debug.analytics.AnalyticsLogger
import com.mindera.alfie.debug.interceptor.DebugInterceptors
import com.mindera.alfie.debug.operational.analytics.AnalyticsLoggerOp
import com.mindera.alfie.debug.operational.interceptor.DebugInterceptorsOp
import com.mindera.alfie.debug.operational.runner.DebugComposeRunnerOp
import com.mindera.alfie.debug.operational.runner.DebugRunnerOp
import com.mindera.alfie.debug.operational.runner.DebugSuspendRunnerOp
import com.mindera.alfie.debug.operational.view.DebugNestedNavGraph
import com.mindera.alfie.debug.operational.view.DebugViewContentOp
import com.mindera.alfie.debug.runner.DebugComposeRunner
import com.mindera.alfie.debug.runner.DebugRunner
import com.mindera.alfie.debug.runner.DebugSuspendRunner
import com.mindera.alfie.debug.view.DebugViewContent
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal interface DebugOpModule {

    @Binds
    fun provideDebugViewContent(debugViewContentOp: DebugViewContentOp): DebugViewContent

    @Binds
    fun provideDebugRunner(debugRunnerOp: DebugRunnerOp): DebugRunner

    @Binds
    fun provideDebugSuspendRunner(debugSuspendRunnerOp: DebugSuspendRunnerOp): DebugSuspendRunner

    @Binds
    fun provideDebugComposeRunner(debugComposeRunnerOp: DebugComposeRunnerOp): DebugComposeRunner

    @Binds
    @Singleton
    fun provideDebugInterceptors(debugInterceptorsOp: DebugInterceptorsOp): DebugInterceptors

    @Binds
    @IntoSet
    fun provideNestedNavGraph(debugNestedNavGraph: DebugNestedNavGraph): NestedNavGraph

    @Binds
    @Singleton
    fun provideAnalyticsLogger(analyticsLoggerOp: AnalyticsLoggerOp): AnalyticsLogger
}
