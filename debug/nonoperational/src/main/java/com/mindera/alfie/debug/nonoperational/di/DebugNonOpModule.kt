package com.mindera.alfie.debug.nonoperational.di

import com.mindera.alfie.debug.analytics.AnalyticsLogger
import com.mindera.alfie.debug.interceptor.DebugInterceptors
import com.mindera.alfie.debug.nonoperational.analytics.AnalyticsLoggerNonOp
import com.mindera.alfie.debug.nonoperational.interceptor.DebugInterceptorsNonOp
import com.mindera.alfie.debug.nonoperational.runner.DebugComposeRunnerNonOp
import com.mindera.alfie.debug.nonoperational.runner.DebugRunnerNonOp
import com.mindera.alfie.debug.nonoperational.runner.DebugSuspendRunnerNonOp
import com.mindera.alfie.debug.nonoperational.view.DebugViewContentNonOp
import com.mindera.alfie.debug.runner.DebugComposeRunner
import com.mindera.alfie.debug.runner.DebugRunner
import com.mindera.alfie.debug.runner.DebugSuspendRunner
import com.mindera.alfie.debug.view.DebugViewContent
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal interface DebugNonOpModule {

    @Binds
    fun provideDebugViewContent(debugViewContentNonOp: DebugViewContentNonOp): DebugViewContent

    @Binds
    fun provideDebugRunner(debugRunnerNonOp: DebugRunnerNonOp): DebugRunner

    @Binds
    fun provideDebugSuspendRunner(debugSuspendRunnerNonOp: DebugSuspendRunnerNonOp): DebugSuspendRunner

    @Binds
    fun provideDebugComposeRunner(debugComposeRunnerNonOp: DebugComposeRunnerNonOp): DebugComposeRunner

    @Binds
    @Singleton
    fun provideDebugInterceptors(debugInterceptorsNonOp: DebugInterceptorsNonOp): DebugInterceptors

    @Binds
    @Singleton
    fun provideAnalyticsLogger(analyticsLoggerNonOp: AnalyticsLoggerNonOp): AnalyticsLogger
}
