package com.mindera.alfie.debug.operational.analytics.data

internal interface AnalyticsLogDataGetter {

    fun get(): List<AnalyticsLogData>
}
