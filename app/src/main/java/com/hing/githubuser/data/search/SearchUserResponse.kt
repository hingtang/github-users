package com.hing.githubuser.data.search

import com.google.gson.annotations.SerializedName
import com.hing.githubuser.data.getusers.UserResponse

data class SearchUserResponse(
    @SerializedName("total_count")
    val count: Int,
    @SerializedName("items")
    val users: List<UserResponse>
)
