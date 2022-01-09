package com.hing.githubuser.domain.search

import com.hing.githubuser.domain.BusinessState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetUsersUseCaseImplTest {

    private lateinit var useCase: GetUsersUseCase
    private lateinit var repository: GetUsersRepository

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetUsersUseCaseImpl(repository)
    }

    @Test
    fun `should return correct State when usecase invoke`() = runBlocking {
        coEvery { repository(request) } returns UserBusinessCases.Success(users)

        val result = useCase(request).first()
        assertTrue(result is BusinessState.BusinessCase)

        val businessResult = result as BusinessState.BusinessCase
        assertTrue(businessResult.data is UserBusinessCases.Success)

        val successResult = businessResult.data as UserBusinessCases.Success
        assertEquals(users.size, successResult.users.size)

        val firstUser = users.first()
        val firstResult = successResult.users.first()
        assertEquals(firstUser.id, firstResult.id)
        assertEquals(firstUser.name, firstResult.name)
        assertEquals(firstUser.avatarUrl, firstResult.avatarUrl)
        assertEquals(firstUser.url, firstResult.url)
    }

    private companion object {
        val request = GetUsersRequest(0, 30)
        val users = listOf(
            UserData(
                1, "Hing", "avatar", "url"
            )
        )
    }
}
