package com.mindera.alfie.debug.operational.view.screen

import android.os.Build
import com.mindera.alfie.core.commons.dispatcher.DispatcherProvider
import com.mindera.alfie.core.commons.string.StringResource
import com.mindera.alfie.core.environment.model.BuildConfiguration
import com.mindera.alfie.debug.operational.BuildConfig
import com.mindera.alfie.debug.operational.R
import com.mindera.alfie.debug.operational.view.destinations.AnalyticsLogScreenDestination
import com.mindera.alfie.debug.operational.view.destinations.CatalogScreenDestination
import com.mindera.alfie.debug.operational.view.destinations.DeeplinkScreenDestination
import com.mindera.alfie.debug.operational.view.destinations.EnvironmentScreenDestination
import com.mindera.alfie.debug.operational.view.destinations.FeatureToggleScreenDestination
import com.mindera.alfie.debug.operational.view.destinations.LogcatBottomSheetDestination
import com.mindera.alfie.debug.operational.view.screen.model.DebugScreenEvent
import com.mindera.alfie.debug.operational.view.screen.model.DebugScreenOpUI
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class DebugScreenOpFactory @Inject constructor(
    private val buildConfiguration: BuildConfiguration,
    private val dispatcher: DispatcherProvider
) {

    suspend operator fun invoke() = withContext(dispatcher.default()) {
        buildList {
            debugFeatures()
            logs()
            buildInfo()
            deviceInfo()
        }
    }

    private fun MutableList<DebugScreenOpUI>.debugFeatures() {
        add(DebugScreenOpUI.Header(StringResource.fromId(R.string.debug_screen_debug_features_label)))
        add(
            DebugScreenOpUI.NavigationItem(
                title = StringResource.fromId(R.string.debug_screen_catalog),
                direction = CatalogScreenDestination
            )
        )
        add(
            DebugScreenOpUI.NavigationItem(
                title = StringResource.fromId(R.string.environment_screen_title),
                direction = EnvironmentScreenDestination
            )
        )
        add(
            DebugScreenOpUI.NavigationItem(
                title = StringResource.fromId(R.string.deeplink_screen_title),
                direction = DeeplinkScreenDestination
            )
        )
        add(
            DebugScreenOpUI.NavigationItem(
                title = StringResource.fromId(R.string.debug_screen_feature_toggle),
                direction = FeatureToggleScreenDestination
            )
        )
        add(
            DebugScreenOpUI.EventItem(
                title = StringResource.fromId(R.string.debug_screen_feedback_label),
                event = DebugScreenEvent.OpenFeedback
            )
        )
    }

    private fun MutableList<DebugScreenOpUI>.logs() {
        add(DebugScreenOpUI.Header(StringResource.fromId(R.string.debug_screen_logs_header)))
        add(
            DebugScreenOpUI.NavigationItem(
                title = StringResource.fromId(R.string.debug_screen_logcat_label),
                direction = LogcatBottomSheetDestination
            )
        )
        add(
            DebugScreenOpUI.NavigationItem(
                title = StringResource.fromId(R.string.debug_screen_analytics_logs_label),
                direction = AnalyticsLogScreenDestination
            )
        )
    }

    private fun MutableList<DebugScreenOpUI>.buildInfo() {
        add(DebugScreenOpUI.Header(StringResource.fromId(R.string.debug_screen_build_info_header)))
        add(
            DebugScreenOpUI.TextItem(
                title = StringResource.fromId(R.string.debug_screen_version_label),
                value = StringResource.fromText(buildConfiguration.versionName)
            )
        )
        add(
            DebugScreenOpUI.TextItem(
                title = StringResource.fromId(R.string.debug_screen_application_id_label),
                value = StringResource.fromText(buildConfiguration.applicationId)
            )
        )
        add(
            DebugScreenOpUI.TextItem(
                title = StringResource.fromId(R.string.debug_screen_branch_label),
                value = StringResource.fromText(BuildConfig.GIT_BRANCH)
            )
        )
        add(
            DebugScreenOpUI.TextItem(
                title = StringResource.fromId(R.string.debug_screen_commit_label),
                value = StringResource.fromText(BuildConfig.GIT_COMMIT)
            )
        )
    }

    private fun MutableList<DebugScreenOpUI>.deviceInfo() {
        add(DebugScreenOpUI.Header(StringResource.fromId(R.string.debug_screen_device_info_header)))
        add(
            DebugScreenOpUI.TextItem(
                title = StringResource.fromId(R.string.debug_screen_device_label),
                value = StringResource.fromText(Build.DEVICE)
            )
        )
        add(
            DebugScreenOpUI.TextItem(
                title = StringResource.fromId(R.string.debug_screen_model_label),
                value = StringResource.fromText(Build.MODEL)
            )
        )
        add(
            DebugScreenOpUI.TextItem(
                title = StringResource.fromId(R.string.debug_screen_manufacturer_label),
                value = StringResource.fromText(Build.MANUFACTURER)
            )
        )
        add(
            DebugScreenOpUI.TextItem(
                title = StringResource.fromId(R.string.debug_screen_android_sdk_label),
                value = StringResource.fromText(Build.VERSION.SDK_INT.toString())
            )
        )
        add(
            DebugScreenOpUI.TextItem(
                title = StringResource.fromId(R.string.debug_screen_resolution_label),
                value = StringResource.EMPTY
            )
        )
        add(
            DebugScreenOpUI.TextItem(
                title = StringResource.fromId(R.string.debug_screen_density_label),
                value = StringResource.EMPTY
            )
        )
    }
}
