package com.hing.githubuser.data

interface RemoteDataSource<I, O> {
    suspend fun start(repoRequest: I): O
}


