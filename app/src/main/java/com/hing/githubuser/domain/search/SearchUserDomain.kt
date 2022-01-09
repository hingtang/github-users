package com.hing.githubuser.domain.search

import com.hing.githubuser.domain.BusinessState
import com.hing.githubuser.domain.FlowUseCase
import com.hing.githubuser.domain.Repository
import com.hing.githubuser.domain.businessFlowOf
import kotlinx.coroutines.flow.Flow

// Use case interface
interface SearchUserUseCase : FlowUseCase<SearchUserRequest, BusinessState<UserBusinessCases>>

// Use case implementation
class SearchUserUseCaseImpl(
    private val repository: SearchUserRepository
) : SearchUserUseCase {
    override fun invoke(input: SearchUserRequest): Flow<BusinessState<UserBusinessCases>> {
        return businessFlowOf { repository(input) }
    }
}

interface SearchUserRepository : Repository<SearchUserRequest, UserBusinessCases>

data class SearchUserRequest(val keywords: String, val page: Int, val perPage: Int)
