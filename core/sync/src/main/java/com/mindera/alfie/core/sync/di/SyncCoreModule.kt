package com.mindera.alfie.core.sync.di

import com.mindera.alfie.core.commons.util.sync.SyncStatusMonitor
import com.mindera.alfie.core.sync.status.WorkManagerSyncStatusMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface SyncCoreModule {

    @Binds
    fun bindsSyncStatusMonitor(syncStatusMonitor: WorkManagerSyncStatusMonitor): SyncStatusMonitor
}
