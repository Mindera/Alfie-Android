package com.mindera.alfie.core.analytics.params

import android.os.Bundle
import com.mindera.alfie.core.analytics.params.AnalyticsValues.IntValues
import com.mindera.alfie.core.analytics.params.AnalyticsValues.StringValues

internal fun AnalyticsParams.toBundle(): Bundle {
    val bundle = Bundle()

    for ((key, value) in this.params()) {
        when (value) {
            is IntValues -> bundle.putInt(key, value.int)
            is StringValues -> bundle.putString(key, value.string)
        }
    }

    return bundle
}

operator fun AnalyticsParams.plus(extraParams: Map<String, AnalyticsValues>): AnalyticsParams {
    val subjectParams = params()

    return object : AnalyticsParams {
        override fun params(): Map<String, AnalyticsValues> = subjectParams + extraParams
    }
}
