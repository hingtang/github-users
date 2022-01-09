package com.hing.githubuser.data

import com.hing.githubuser.data.search.SearchUserResponse
import com.hing.githubuser.data.getusers.UserResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GitHubApi {
    @GET("users")
    suspend fun getUsers(
        @Query("since") since: Int,
        @Query("per_page") perPage: Int
    ): List<UserResponse>
}
