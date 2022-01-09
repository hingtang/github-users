package com.hing.githubuser.data

import com.hing.githubuser.data.getusers.UserResponse
import com.hing.githubuser.data.search.SearchUserResponse
import com.hing.githubuser.data.userdetail.UserDetailResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApi {
    @GET("users")
    suspend fun getUsers(
        @Query("since") since: Int,
        @Query("per_page") perPage: Int
    ): List<UserResponse>

    @GET("search/users")
    suspend fun searchUsers(
        @Query("q") keywords: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): SearchUserResponse

    @GET("users/{username}")
    suspend fun getUserDetail(
        @Path("username") userName: String
    ): UserDetailResponse
}
