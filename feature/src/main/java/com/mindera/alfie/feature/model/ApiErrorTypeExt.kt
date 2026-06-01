package com.mindera.alfie.feature.model

import androidx.annotation.StringRes
import com.mindera.alfie.feature.R as FeatureR

@StringRes
fun ApiErrorType.toStringRes(
    @StringRes throttledRes: Int = FeatureR.string.error_throttled,
    @StringRes serverRes: Int = FeatureR.string.error_server,
    @StringRes networkRes: Int = FeatureR.string.error_network,
    @StringRes notFoundRes: Int = FeatureR.string.error_not_found,
    @StringRes genericRes: Int = FeatureR.string.error_generic
): Int = when (this) {
    ApiErrorType.Throttled -> throttledRes
    ApiErrorType.Server -> serverRes
    ApiErrorType.Network -> networkRes
    ApiErrorType.NotFound -> notFoundRes
    ApiErrorType.Generic -> genericRes
}
