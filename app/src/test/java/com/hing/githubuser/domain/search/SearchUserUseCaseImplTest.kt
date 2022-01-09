package com.hing.githubuser.domain.search

import com.hing.githubuser.domain.BusinessState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class SearchUserUseCaseImplTest {

    private lateinit var useCase: SearchUserUseCase
    private lateinit var repository: SearchUserRepository

    @Before
    fun setup() {
        repository = mockk()
        useCase = SearchUserUseCaseImpl(repository)
    }

    @Test
    fun `should return correct State when useCase invoke`() = runBlocking {
        coEvery { repository(request) } returns UserBusinessCases.Success(users)

        val result = useCase(request).first()
        Assert.assertTrue(result is BusinessState.BusinessCase)

        val businessResult = result as BusinessState.BusinessCase
        Assert.assertTrue(businessResult.data is UserBusinessCases.Success)

        val successResult = businessResult.data as UserBusinessCases.Success
        Assert.assertEquals(users.size, successResult.users.size)

        val firstUser = users.first()
        val firstResult = successResult.users.first()
        Assert.assertEquals(firstUser.id, firstResult.id)
        Assert.assertEquals(firstUser.name, firstResult.name)
        Assert.assertEquals(firstUser.avatarUrl, firstResult.avatarUrl)
        Assert.assertEquals(firstUser.url, firstResult.url)
    }

    private companion object {
        val request = SearchUserRequest("Hing", 0, 30)
        val users = listOf(
            UserData(
                1, "Hing", "avatar", "url"
            )
        )
    }
}
