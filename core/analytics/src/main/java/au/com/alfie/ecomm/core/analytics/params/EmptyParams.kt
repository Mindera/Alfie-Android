package au.com.alfie.ecomm.core.analytics.params

class EmptyParams : AnalyticsParams {

    override fun params(): Map<String, AnalyticsValues> = mapOf()
}
