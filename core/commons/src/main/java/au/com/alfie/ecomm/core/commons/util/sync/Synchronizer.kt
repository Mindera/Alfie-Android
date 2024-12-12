package au.com.alfie.ecomm.core.commons.util.sync

import timber.log.Timber

interface Synchronizer {

    suspend fun Syncable.sync() = this@sync.syncWith(this@Synchronizer)

    suspend fun <T> syncWithResult(block: suspend () -> T): Result<T> = runCatching {
        block()
    }.onFailure {
        Timber.e(it)
    }
}

suspend fun <T> Synchronizer.updateCache(
    remote: suspend () -> List<T>,
    update: suspend (List<T>) -> Unit
) = syncWithResult {
    val data = remote()
    if (data.isEmpty()) return@syncWithResult

    update(data)
}

suspend fun <T> Synchronizer.updateCacheElement(
    remote: suspend () -> T,
    update: suspend (T) -> Unit
) = syncWithResult {
    update(remote())
}
