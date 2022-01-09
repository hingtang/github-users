package com.hing.githubuser.domain.userdetail

import com.hing.githubuser.domain.BusinessState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class UserDetailUseCaseImplTest {

    private lateinit var useCase: UserDetailUseCase
    private lateinit var repository: UserDetailRepository

    @Before
    fun setup() {
        repository = mockk()
        useCase = UserDetailUseCaseImpl(repository)
    }

    @Test
    fun `should return correct State when useCase invoke`() = runBlocking {
        coEvery { repository(request) } returns UserDetailBusinessCases.Success(userDetailData)

        val result = useCase(request).first()
        Assert.assertTrue(result is BusinessState.BusinessCase)

        val businessResult = result as BusinessState.BusinessCase
        Assert.assertTrue(businessResult.data is UserDetailBusinessCases.Success)

        val successResult = businessResult.data as UserDetailBusinessCases.Success

        val userDetail = successResult.userDetail
        Assert.assertEquals(userDetail.id, userDetailData.id)
        Assert.assertEquals(userDetail.name, userDetailData.name)
        Assert.assertEquals(userDetail.avatarUrl, userDetailData.avatarUrl)
        Assert.assertEquals(userDetail.url, userDetailData.url)
    }

    private companion object {
        const val ID = 1L
        const val NAME = "Hing Tang"
        const val AVATAR_URL = "avatar-url"
        const val URL = "url"

        val request = UserDetailRequest("Hing")
        val userDetailData = UserDetailData(ID, NAME, NAME, AVATAR_URL, null, 1, 1, 1, URL)
    }
}
