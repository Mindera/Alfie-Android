package com.mindera.alfie.core.analytics.params

class EmptyParams : AnalyticsParams {

    override fun params(): Map<String, AnalyticsValues> = mapOf()
}
