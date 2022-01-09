package com.hing.githubuser.data.extensions

import com.hing.githubuser.data.getusers.UserResponse
import com.hing.githubuser.domain.search.UserData

fun List<UserResponse>.mapToBusinessModel(): List<UserData> {
    val listBusinessModel = mutableListOf<UserData>()
    for (model in this) {
        listBusinessModel.add(
            UserData(
                model.id,
                model.login,
                model.avatarUrl,
                model.url
            )
        )
    }
    return listBusinessModel
}
