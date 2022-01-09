package com.hing.githubuser.domain.userdetail

import com.hing.githubuser.data.GitHubApi
import com.hing.githubuser.data.RemoteDataSource
import com.hing.githubuser.data.userdetail.UserDetailResponse

class UserDetailApiService(private val gitHubApi: GitHubApi) :
    RemoteDataSource<UserDetailRequest, UserDetailResponse> {
    override suspend fun start(repoRequest: UserDetailRequest): UserDetailResponse {
        return gitHubApi.getUserDetail(repoRequest.username)
    }
}
