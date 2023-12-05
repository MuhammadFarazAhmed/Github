package com.example.github.data.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType) -> Boolean = { true }
) =
    flow {
            val data = query().first()

            val flow = if (shouldFetch(data)) {
                emit(ApiResult.Loading(true))

                try {
                    saveFetchResult(fetch())
                    query().map { ApiResult.Success(it) }
                } catch (throwable: Throwable) {
                   query().map { ApiResult.Success(it) }
                }
            } else {
                query().map { ApiResult.Success(it) }
            }

            emitAll(flow)
        }

