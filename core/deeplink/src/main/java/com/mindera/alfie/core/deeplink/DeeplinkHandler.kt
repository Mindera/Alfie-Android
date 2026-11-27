package com.mindera.alfie.core.deeplink

import androidx.compose.runtime.Stable
import com.mindera.alfie.core.deeplink.model.DeeplinkPathSegment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Stable
class DeeplinkHandler @Inject constructor(deeplinks: Set<@JvmSuppressWildcards DeeplinkGroup>) {

    private val _deeplinkResult = MutableSharedFlow<DeeplinkResult>()
    val deeplinkResult = _deeplinkResult.asSharedFlow()

    private val interpreters: List<DeeplinkInterpreter> = deeplinks.map { it.interpreters }.flatten()

    /**
     * Resolves [url] and emits the result to [deeplinkResult]. It waits for a collector first: the flow
     * keeps nothing, and a link that arrives at launch can resolve before the app's navigation is
     * composed and collecting, which would drop it.
     */
    suspend fun handle(url: String) {
        val result = resolve(url)
        _deeplinkResult.subscriptionCount.first { it > 0 }
        _deeplinkResult.emit(result)
    }

    suspend fun resolve(url: String): DeeplinkResult = withContext(Dispatchers.Default) {
        // Pick the interpreter that matches the URL and has more Fixed or Pattern path segments
        interpreters.mapNotNull { interpreter ->
            interpreter.specs.firstNotNullOfOrNull { it.resolve(url) }?.let { instance ->
                interpreter.handle(instance)
                    .takeIf { result -> result !is DeeplinkResult.Unresolved }
                    ?.let { instance to it }
            }
        }.maxByOrNull { (instance, _) ->
            instance.spec.pathSegments.count { it is DeeplinkPathSegment.Fixed || it is DeeplinkPathSegment.Pattern }
        }?.second ?: DeeplinkResult.Unresolved(url)
    }
}
