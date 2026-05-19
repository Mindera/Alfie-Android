package com.mindera.alfie.feature.startup.loader

import com.google.firebase.Firebase
import com.google.firebase.appdistribution.InterruptionLevel
import com.google.firebase.appdistribution.appDistribution
import com.mindera.alfie.feature.startup.R
import javax.inject.Inject

internal class FeedbackLoader @Inject constructor() : StartUpLoader {

    override suspend fun load(): Result<Unit> {
        Firebase.appDistribution.showFeedbackNotification(
            R.string.feedback_notification,
            InterruptionLevel.DEFAULT
        )
        return Result.success(Unit)
    }
}
