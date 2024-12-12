package au.com.alfie.ecomm.core.sync.status

import android.content.Context
import androidx.lifecycle.asFlow
import androidx.lifecycle.map
import androidx.work.WorkInfo
import androidx.work.WorkInfo.State.RUNNING
import androidx.work.WorkManager
import au.com.alfie.ecomm.core.commons.util.sync.SyncStatusMonitor
import au.com.alfie.ecomm.core.sync.initializers.SyncInitializer.Companion.AppSyncWorkName
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import javax.inject.Inject

private fun List<WorkInfo>.anyRunning(): Boolean = any { it.state == RUNNING }

class WorkManagerSyncStatusMonitor @Inject constructor(
    @ApplicationContext context: Context
) : SyncStatusMonitor {

    override val isSyncing: Flow<Boolean> =
        WorkManager.getInstance(context).getWorkInfosForUniqueWorkLiveData(AppSyncWorkName)
            .map { it.anyRunning() }
            .asFlow()
            .conflate()
}
