package au.com.alfie.ecomm.core.commons.util.sync

import kotlinx.coroutines.flow.Flow

interface SyncStatusMonitor {

    val isSyncing: Flow<Boolean>
}
