package com.hing.githubuser.data.getusers

import com.hing.githubuser.data.RemoteDataSource
import com.hing.githubuser.domain.search.GetUsersRepository
import com.hing.githubuser.domain.search.GetUsersRequest
import com.hing.githubuser.domain.search.UserBusinessCases

class GetUsersRepositoryImpl(
    private val remoteDataSource: RemoteDataSource<GetUsersRequest, List<UserResponse>>,
    private val mapper: GetUsersMapper
) : GetUsersRepository {

    override suspend fun invoke(input: GetUsersRequest): UserBusinessCases {
        val repoResponse = remoteDataSource.start(input)
        return mapper.map(repoResponse)
    }
}
