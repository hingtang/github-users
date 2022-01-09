package com.hing.githubuser.data.userdetail

import com.hing.githubuser.domain.userdetail.UserDetailBusinessCases
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class UserDetailMapperImplTest {

    private lateinit var mapper: UserDetailMapper

    @Before
    fun setUp() {
        mapper = UserDetailMapperImpl()
    }

    @Test
    fun `should return Empty Business Case when response is null`() {
        Assert.assertTrue(mapper.map(null) is UserDetailBusinessCases.EmptyData)
    }

    @Test
    fun `should return Success Business Case when response is not empty`() {
        val actual = mapper.map(responses)
        Assert.assertTrue(actual is UserDetailBusinessCases.Success)

        val userDetail = (actual as UserDetailBusinessCases.Success).userDetail
        Assert.assertEquals(userDetail.id, responses.id)
        Assert.assertEquals(userDetail.name, responses.login)
        Assert.assertEquals(userDetail.avatarUrl, responses.avatarUrl)
        Assert.assertEquals(userDetail.url, responses.url)
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
