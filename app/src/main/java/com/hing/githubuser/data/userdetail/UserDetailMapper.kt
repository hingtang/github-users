package com.hing.githubuser.data.userdetail

import com.hing.githubuser.data.Mapper
import com.hing.githubuser.domain.GenericBusinessCase
import com.hing.githubuser.domain.userdetail.UserDetailBusinessCases
import com.hing.githubuser.domain.userdetail.UserDetailData

// Repo to business mapper
interface UserDetailMapper : Mapper<UserDetailResponse, UserDetailBusinessCases>

class UserDetailMapperImpl : UserDetailMapper {
    override fun map(it: UserDetailResponse?): UserDetailBusinessCases {
        try {
            it?.let { userDetail ->
                return UserDetailBusinessCases.Success(mapToBusinessModel(userDetail))
            }
            return UserDetailBusinessCases.EmptyData
        } catch (ex: Exception) {
            throw GenericBusinessCase.MapperToBusinessError(ex)
        }
    }

    private fun mapToBusinessModel(userDetail: UserDetailResponse): UserDetailData {
        return UserDetailData(
            userDetail.id,
            userDetail.login,
            userDetail.name,
            userDetail.avatarUrl,
            userDetail.bio,
            userDetail.publicRepos,
            userDetail.followers,
            userDetail.following,
            userDetail.htmlUrl
        )
    }
}
