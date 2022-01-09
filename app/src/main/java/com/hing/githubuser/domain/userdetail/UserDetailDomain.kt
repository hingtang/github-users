package com.hing.githubuser.domain.userdetail

import com.hing.githubuser.domain.BusinessState
import com.hing.githubuser.domain.FlowUseCase
import com.hing.githubuser.domain.Repository
import com.hing.githubuser.domain.businessFlowOf
import kotlinx.coroutines.flow.Flow

// Use case interface
interface UserDetailUseCase : FlowUseCase<UserDetailRequest, BusinessState<UserDetailBusinessCases>>

// Use case implementation
class UserDetailUseCaseImpl(
    private val repository: UserDetailRepository
) : UserDetailUseCase {
    override fun invoke(input: UserDetailRequest): Flow<BusinessState<UserDetailBusinessCases>> {
        return businessFlowOf { repository(input) }
    }
}

// Business Cases
sealed class UserDetailBusinessCases {
    object EmptyData : UserDetailBusinessCases()
    data class Success(val userDetail: UserDetailData) : UserDetailBusinessCases()
}

// Business data
data class UserDetailData(
    val id: Long,
    val name: String,
    val displayName: String?,
    val avatarUrl: String,
    val bio: String?,
    val publicRepos: Int,
    val followers: Int,
    val following: Int,
    val url: String
)

interface UserDetailRepository : Repository<UserDetailRequest, UserDetailBusinessCases>

data class UserDetailRequest(val username: String)
