package com.hing.githubuser.domain

interface Repository<I, O> {
    suspend operator fun invoke(input: I): O
}
