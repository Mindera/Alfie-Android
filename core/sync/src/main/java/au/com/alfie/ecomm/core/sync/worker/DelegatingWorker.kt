package au.com.alfie.ecomm.core.sync.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import au.com.alfie.ecomm.core.sync.di.HiltWorkerFactoryEntryPoint
import au.com.alfie.ecomm.core.sync.worker.DelegatingWorker.Companion.WORKER_CLASS_NAME
import dagger.hilt.android.EntryPointAccessors
import kotlin.reflect.KClass

internal class DelegatingWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    companion object {

        internal const val WORKER_CLASS_NAME = "WORKER_CLASS_NAME"
    }

    private val workerClassName = workerParams.inputData.getString(WORKER_CLASS_NAME).orEmpty()

    private val delegateWorker = EntryPointAccessors
        .fromApplication<HiltWorkerFactoryEntryPoint>(appContext)
        .hiltWorkerFactory()
        .createWorker(appContext, workerClassName, workerParams) as? CoroutineWorker
        ?: throw IllegalArgumentException("Unable to find appropriate worker")

    override suspend fun getForegroundInfo(): ForegroundInfo = delegateWorker.getForegroundInfo()

    override suspend fun doWork(): Result = delegateWorker.doWork()
}

internal fun KClass<out CoroutineWorker>.delegatedData() =
    Data.Builder()
        .putString(WORKER_CLASS_NAME, qualifiedName)
        .build()
