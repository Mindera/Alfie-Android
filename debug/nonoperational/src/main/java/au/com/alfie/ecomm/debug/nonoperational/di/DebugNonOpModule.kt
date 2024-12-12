package au.com.alfie.ecomm.debug.nonoperational.di

import au.com.alfie.ecomm.debug.analytics.AnalyticsLogger
import au.com.alfie.ecomm.debug.interceptor.DebugInterceptors
import au.com.alfie.ecomm.debug.nonoperational.analytics.AnalyticsLoggerNonOp
import au.com.alfie.ecomm.debug.nonoperational.interceptor.DebugInterceptorsNonOp
import au.com.alfie.ecomm.debug.nonoperational.runner.DebugComposeRunnerNonOp
import au.com.alfie.ecomm.debug.nonoperational.runner.DebugRunnerNonOp
import au.com.alfie.ecomm.debug.nonoperational.runner.DebugSuspendRunnerNonOp
import au.com.alfie.ecomm.debug.nonoperational.view.DebugViewContentNonOp
import au.com.alfie.ecomm.debug.runner.DebugComposeRunner
import au.com.alfie.ecomm.debug.runner.DebugRunner
import au.com.alfie.ecomm.debug.runner.DebugSuspendRunner
import au.com.alfie.ecomm.debug.view.DebugViewContent
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
