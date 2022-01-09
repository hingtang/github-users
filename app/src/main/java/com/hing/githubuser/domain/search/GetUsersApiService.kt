package com.hing.githubuser.domain.search

import com.hing.githubuser.data.GitHubApi
import com.hing.githubuser.data.RemoteDataSource
import com.hing.githubuser.data.getusers.UserResponse

class GetUsersApiService(private val gitHubApi: GitHubApi) :
    RemoteDataSource<GetUsersRequest, List<UserResponse>> {
    override suspend fun start(repoRequest: GetUsersRequest): List<UserResponse> {
        return gitHubApi.getUsers(repoRequest.since, repoRequest.perPage)
    }
}
