package com.hing.githubuser.data.search

import com.hing.githubuser.data.RemoteDataSource
import com.hing.githubuser.domain.search.SearchUserRepository
import com.hing.githubuser.domain.search.SearchUserRequest
import com.hing.githubuser.domain.search.UserBusinessCases

class SearchUserRepositoryImpl(
    private val remoteDataSource: RemoteDataSource<SearchUserRequest, SearchUserResponse>,
    private val mapper: SearchUserMapper
) : SearchUserRepository {

    override suspend fun invoke(input: SearchUserRequest): UserBusinessCases {
        val repoResponse = remoteDataSource.start(input)
        return mapper.map(repoResponse)
    }
}
