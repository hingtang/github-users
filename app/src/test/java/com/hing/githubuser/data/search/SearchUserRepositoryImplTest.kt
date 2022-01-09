package com.hing.githubuser.data.search

import com.hing.githubuser.data.RemoteDataSource
import com.hing.githubuser.domain.search.SearchUserRepository
import com.hing.githubuser.domain.search.SearchUserRequest
import com.hing.githubuser.domain.search.UserBusinessCases
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SearchUserRepositoryImplTest {

    private lateinit var repository: SearchUserRepository
    private lateinit var remoteDataSource: RemoteDataSource<SearchUserRequest, SearchUserResponse>
    private lateinit var mapper: SearchUserMapper

    @Before
    fun setup() {
        remoteDataSource = mockk()
        mapper = mockk()

        coEvery { remoteDataSource.start(any()) } returns SearchUserResponse(0, emptyList())
        coEvery { mapper.map(any()) } returns UserBusinessCases.EmptyList

        repository = SearchUserRepositoryImpl(remoteDataSource, mapper)
    }

    @Test
    fun `should return the correct BusinessCases when invoke is called`() = runBlocking {
        val result = repository(SearchUserRequest("Hing", 0, 30))

        assertTrue(result is UserBusinessCases.EmptyList)
    }
}
