package com.mindera.alfie.debug.operational.analytics.data

internal interface AnalyticsLogDataEmitter {

    fun emit(analyticsLogData: AnalyticsLogData)
}
