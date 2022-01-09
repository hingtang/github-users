package com.hing.githubuser.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retryWhen

sealed class BusinessState<out T> {
    data class BusinessCase<out T>(val data: T) : BusinessState<T>()
    data class GenericCase(val genericBusinessCase: GenericBusinessCase) : BusinessState<Nothing>()
}

fun <T> businessFlowOf(suspendFunc: suspend () -> T): Flow<BusinessState<T>> =
    businessFlowOf(
        flowOf { suspendFunc() },
        DefaultRetry
    )

fun <T> businessFlowOf(
    flow: Flow<T>,
    retry: Retry = DefaultRetry
): Flow<BusinessState<T>> = flow
    .retryWhen { cause, attempt -> retry(cause, attempt) }
    .map { BusinessState.BusinessCase(it) }
    .catch<BusinessState<T>> {
        if (it is GenericBusinessCase) {
            emit(BusinessState.GenericCase(it))
        } else {
            emit(BusinessState.GenericCase(GenericBusinessCase.UnknownError(it)))
        }
    }
