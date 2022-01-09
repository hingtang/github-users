package com.hing.githubuser.data.search

import com.hing.githubuser.data.Mapper
import com.hing.githubuser.data.extensions.mapToBusinessModel
import com.hing.githubuser.domain.GenericBusinessCase
import com.hing.githubuser.domain.search.UserBusinessCases

// Repo to business mapper
interface SearchUserMapper : Mapper<SearchUserResponse, UserBusinessCases>

class SearchUserMapperImpl : SearchUserMapper {
    override fun map(it: SearchUserResponse?): UserBusinessCases {
        try {
            it?.users?.let { users ->
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
