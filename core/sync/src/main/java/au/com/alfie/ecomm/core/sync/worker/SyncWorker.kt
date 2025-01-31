package au.com.alfie.ecomm.core.sync.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType.CONNECTED
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST
import androidx.work.WorkerParameters
import au.com.alfie.ecomm.core.commons.util.sync.Synchronizer
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

@HiltWorker
internal class SyncWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), Synchronizer {

    companion object {

        fun startUpSyncWork(): OneTimeWorkRequest {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(CONNECTED)
                .build()

            return OneTimeWorkRequestBuilder<DelegatingWorker>()
                .setExpedited(RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .setConstraints(constraints)
                .setInputData(SyncWorker::class.delegatedData())
                .build()
        }
    }

    override suspend fun doWork(): Result = withContext(IO) {
        val syncedSuccessfully = awaitAll(
            async { true } // TODO Do some work here eventually
        ).all { it }

        if (syncedSuccessfully) {
            Result.success()
        } else {
            Result.retry()
        }
    }
}
