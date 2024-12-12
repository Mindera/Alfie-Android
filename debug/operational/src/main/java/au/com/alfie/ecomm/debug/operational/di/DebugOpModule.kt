package au.com.alfie.ecomm.debug.operational.di

import au.com.alfie.ecomm.core.navigation.NestedNavGraph
import au.com.alfie.ecomm.debug.analytics.AnalyticsLogger
import au.com.alfie.ecomm.debug.interceptor.DebugInterceptors
import au.com.alfie.ecomm.debug.operational.analytics.AnalyticsLoggerOp
import au.com.alfie.ecomm.debug.operational.interceptor.DebugInterceptorsOp
import au.com.alfie.ecomm.debug.operational.runner.DebugComposeRunnerOp
import au.com.alfie.ecomm.debug.operational.runner.DebugRunnerOp
import au.com.alfie.ecomm.debug.operational.runner.DebugSuspendRunnerOp
import au.com.alfie.ecomm.debug.operational.view.DebugNestedNavGraph
import au.com.alfie.ecomm.debug.operational.view.DebugViewContentOp
import au.com.alfie.ecomm.debug.runner.DebugComposeRunner
import au.com.alfie.ecomm.debug.runner.DebugRunner
import au.com.alfie.ecomm.debug.runner.DebugSuspendRunner
import au.com.alfie.ecomm.debug.view.DebugViewContent
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
