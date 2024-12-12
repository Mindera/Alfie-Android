package au.com.alfie.ecomm.feature.startup

import android.content.Context
import au.com.alfie.ecomm.core.sync.initializers.Sync
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class StartUpCallback @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun onStartUpFinish() {
        Sync.initialize(context)
    }
}
