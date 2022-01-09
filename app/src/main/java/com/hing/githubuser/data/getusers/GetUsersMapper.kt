package com.hing.githubuser.data.getusers

import com.hing.githubuser.data.Mapper
import com.hing.githubuser.data.extensions.mapToBusinessModel
import com.hing.githubuser.domain.GenericBusinessCase
import com.hing.githubuser.domain.search.UserBusinessCases

// Repo to business mapper
interface GetUsersMapper : Mapper<List<UserResponse>, UserBusinessCases>

class GetUsersMapperImpl : GetUsersMapper {
    override fun map(it: List<UserResponse>?): UserBusinessCases {
        try {
            it?.let { users ->
                return if (users.isEmpty()) {
                    UserBusinessCases.EmptyList
                } else {
                    UserBusinessCases.Success(users.mapToBusinessModel())
                }
            }
            return UserBusinessCases.EmptyList
        } catch (ex: Exception) {
            throw GenericBusinessCase.MapperToBusinessError(ex)
        }
    }
}
