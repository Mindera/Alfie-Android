package au.com.alfie.ecomm.core.sync.initializers

import android.content.Context
import androidx.startup.AppInitializer
import androidx.startup.Initializer
import androidx.work.ExistingWorkPolicy.KEEP
import androidx.work.WorkManager
import androidx.work.WorkManagerInitializer
import au.com.alfie.ecomm.core.sync.worker.SyncWorker

object Sync {

    fun initialize(context: Context) {
        AppInitializer.getInstance(context).initializeComponent(SyncInitializer::class.java)
    }
}

class SyncInitializer : Initializer<Sync> {

    companion object {

        internal const val AppSyncWorkName = "AppSyncWorkName"
    }

    override fun create(context: Context): Sync {
        WorkManager.getInstance(context).enqueueUniqueWork(
            /* uniqueWorkName = */ AppSyncWorkName,
            /* existingWorkPolicy = */ KEEP,
            /* work = */ SyncWorker.startUpSyncWork()
        )

        return Sync
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = listOf(WorkManagerInitializer::class.java)
}
