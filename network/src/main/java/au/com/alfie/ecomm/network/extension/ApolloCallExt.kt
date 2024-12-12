package au.com.alfie.ecomm.network.extension

import com.apollographql.apollo3.ApolloCall
import com.apollographql.apollo3.api.Operation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun <T : Operation.Data> ApolloCall<T>.unwrap(): Result<T> = withContext(Dispatchers.IO) {
    runCatching {
        val response = execute()
        return@withContext when {
            response.hasErrors().not() && response.exception == null && response.data != null -> {
                Result.success(response.dataAssertNoErrors)
            }

            else -> Result.failure(response.toException())
        }
    }
}
