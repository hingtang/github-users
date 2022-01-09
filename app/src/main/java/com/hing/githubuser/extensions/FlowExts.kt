package com.hing.githubuser.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

fun <T> flowOf(block: suspend () -> T) = flow {
    emit(block())
}

inline fun <T> Flow<T>.collectInScope(
    scope: CoroutineScope,
    crossinline action: suspend (value: T) -> Unit
): Job = scope.launch {
    collect(action)
}
