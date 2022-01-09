package com.hing.githubuser.domain.search

import com.hing.githubuser.data.GitHubApi
import com.hing.githubuser.data.RemoteDataSource
import com.hing.githubuser.data.search.SearchUserResponse

class SearchUserApiService(private val gitHubApi: GitHubApi) :
    RemoteDataSource<SearchUserRequest, SearchUserResponse> {
    override suspend fun start(repoRequest: SearchUserRequest): SearchUserResponse {
        return gitHubApi.searchUsers(repoRequest.keywords, repoRequest.page, repoRequest.perPage)
    }
}
