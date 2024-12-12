package au.com.alfie.ecomm.core.deeplink

import androidx.compose.runtime.Stable
import au.com.alfie.ecomm.core.deeplink.model.DeeplinkPathSegment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Stable
class DeeplinkHandler @Inject constructor(deeplinks: Set<@JvmSuppressWildcards DeeplinkGroup>) {

    private val _deeplinkResult = MutableSharedFlow<DeeplinkResult>()
    val deeplinkResult = _deeplinkResult.asSharedFlow()

    private val interpreters: List<DeeplinkInterpreter> = deeplinks.map { it.interpreters }.flatten()

    suspend fun handle(url: String) {
        val result = resolve(url)
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
