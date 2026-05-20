package com.mindera.alfie.feature.startup

import android.content.Context
import com.mindera.alfie.core.sync.initializers.Sync
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class StartUpCallback @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun onStartUpFinish() {
        Sync.initialize(context)
    }
}
