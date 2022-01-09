package com.hing.githubuser.domain.search

import com.hing.githubuser.domain.BusinessState
import com.hing.githubuser.domain.FlowUseCase
import com.hing.githubuser.domain.Repository
import com.hing.githubuser.domain.businessFlowOf
import kotlinx.coroutines.flow.Flow

// Use case interface
interface GetUsersUseCase : FlowUseCase<GetUsersRequest, BusinessState<UserBusinessCases>>

// Use case implementation
class GetUsersUseCaseImpl(
    private val repository: GetUsersRepository
) : GetUsersUseCase {
    override fun invoke(input: GetUsersRequest): Flow<BusinessState<UserBusinessCases>> {
        return businessFlowOf { repository(input) }
    }
}

// Business Cases
sealed class UserBusinessCases {
    object EmptyList : UserBusinessCases()
    data class Success(val users: List<UserData>) :
        UserBusinessCases()
}

// Business data
data class UserData(
    val id: Int,
    val name: String,
    val avatarUrl: String,
    val url: String
)

interface GetUsersRepository : Repository<GetUsersRequest, UserBusinessCases>

data class GetUsersRequest(val since: Int, val perPage: Int)
