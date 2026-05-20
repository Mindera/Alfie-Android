package com.mindera.alfie.core.commons.util.sync

import kotlinx.coroutines.flow.Flow

interface SyncStatusMonitor {

    val isSyncing: Flow<Boolean>
}
