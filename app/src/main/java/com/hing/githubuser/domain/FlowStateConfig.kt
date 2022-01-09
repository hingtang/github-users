package com.hing.githubuser.domain

import kotlinx.coroutines.flow.Flow

@DslMarker
annotation class FlowStateConfigMarker

@FlowStateConfigMarker
open class FlowStateConfig<T>(
    var retry: Retry = NoRetry,
    var flow: Flow<T>
)

@FlowStateConfigMarker
class FlowStateOfSuspendFuncConfig<T> {
    var retry: Retry = NoRetry
    lateinit var suspendFunc: suspend () -> T
}
