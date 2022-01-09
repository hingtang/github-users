package com.hing.githubuser.data.getusers

import com.hing.githubuser.data.RemoteDataSource
import com.hing.githubuser.domain.search.GetUsersRepository
import com.hing.githubuser.domain.search.GetUsersRequest
import com.hing.githubuser.domain.search.UserBusinessCases
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GetUsersRepositoryImplTest {

    private lateinit var repository: GetUsersRepository
    private lateinit var remoteDataSource: RemoteDataSource<GetUsersRequest, List<UserResponse>>
    private lateinit var mapper: GetUsersMapper

    @Before
    fun setup() {
        remoteDataSource = mockk()
        mapper = mockk()

        coEvery { remoteDataSource.start(any()) } returns emptyList()
        coEvery { mapper.map(any()) } returns UserBusinessCases.EmptyList

        repository = GetUsersRepositoryImpl(remoteDataSource, mapper)
    }

    @Test
    fun `should return the correct BusinessCases when invoke is called`() = runBlocking {
        val result = repository(GetUsersRequest(0, 30))

        Assert.assertTrue(result is UserBusinessCases.EmptyList)
    }
}
