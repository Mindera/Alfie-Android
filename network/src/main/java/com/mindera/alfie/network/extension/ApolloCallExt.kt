package com.mindera.alfie.network.extension

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Operation
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
