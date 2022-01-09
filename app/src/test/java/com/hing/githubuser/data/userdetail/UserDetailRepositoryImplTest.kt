package com.hing.githubuser.data.userdetail

import com.hing.githubuser.data.RemoteDataSource
import com.hing.githubuser.domain.userdetail.UserDetailBusinessCases
import com.hing.githubuser.domain.userdetail.UserDetailData
import com.hing.githubuser.domain.userdetail.UserDetailRepository
import com.hing.githubuser.domain.userdetail.UserDetailRequest
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class UserDetailRepositoryImplTest {

    private lateinit var repository: UserDetailRepository
    private lateinit var remoteDataSource: RemoteDataSource<UserDetailRequest, UserDetailResponse>
    private lateinit var mapper: UserDetailMapper

    @Before
    fun setup() {
        remoteDataSource = mockk()
        mapper = mockk()

        coEvery { remoteDataSource.start(any()) } returns responses
        coEvery { mapper.map(any()) } returns UserDetailBusinessCases.Success(
            UserDetailData(
                ID,
                NAME,
                NAME,
                AVATAR_URL,
                null,
                0,
                0,
                0,
                URL
            )
        )

        repository = UserDetailRepositoryImpl(remoteDataSource, mapper)
    }

    @Test
    fun `should return the correct BusinessCases when invoke is called`() = runBlocking {
        val result = repository(UserDetailRequest("Hing"))

        Assert.assertTrue(result is UserDetailBusinessCases.Success)
    }

    private companion object {
        const val ID = 1L
        const val NAME = "Hing Tang"
        const val AVATAR_URL = "avatar-url"
        const val URL = "url"

        val responses = UserDetailResponse(
            NAME, ID, "NODE-ID", AVATAR_URL, "GRAVATAR-ID",
            URL, URL, URL, URL, URL, URL, URL, URL, URL, URL, URL, "TYPE", true, null,
            "COMPANY", "BLOG", null, null, false, null, NAME,
            0, 0, 0, 0, "CREATE-AT", "UPDATE-AT"
        )
    }
}
