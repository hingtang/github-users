package com.hing.githubuser.domain

import kotlinx.coroutines.flow.Flow

interface UseCase<I, O> {
    operator fun invoke(input: I): O
}

typealias FlowUseCase<I, O> = UseCase<I, Flow<O>>
