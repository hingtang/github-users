package com.hing.githubuser.data.userdetail

import com.hing.githubuser.data.RemoteDataSource
import com.hing.githubuser.domain.userdetail.UserDetailBusinessCases
import com.hing.githubuser.domain.userdetail.UserDetailRepository
import com.hing.githubuser.domain.userdetail.UserDetailRequest

class UserDetailRepositoryImpl(
    private val remoteDataSource: RemoteDataSource<UserDetailRequest, UserDetailResponse>,
    private val mapper: UserDetailMapper
) : UserDetailRepository {

    override suspend fun invoke(input: UserDetailRequest): UserDetailBusinessCases {
        val repoResponse = remoteDataSource.start(input)
        return mapper.map(repoResponse)
    }
}
