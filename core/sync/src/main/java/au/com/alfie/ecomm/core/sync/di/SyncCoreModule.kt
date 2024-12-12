package au.com.alfie.ecomm.core.sync.di

import au.com.alfie.ecomm.core.commons.util.sync.SyncStatusMonitor
import au.com.alfie.ecomm.core.sync.status.WorkManagerSyncStatusMonitor
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
