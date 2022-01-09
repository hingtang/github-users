package com.hing.githubuser.data.search

import com.hing.githubuser.data.getusers.UserResponse
import com.hing.githubuser.domain.search.UserBusinessCases
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class SearchUserMapperImplTest {

    private lateinit var mapper: SearchUserMapper

    @Before
    fun setUp() {
        mapper = SearchUserMapperImpl()
    }

    @Test
    fun `should return Empty Business Case when response is null`() {
        Assert.assertTrue(mapper.map(null) is UserBusinessCases.EmptyList)
    }

    @Test
    fun `should return Empty Business Case when response is empty`() {
        Assert.assertTrue(
            mapper.map(
                SearchUserResponse(
                    0,
                    emptyList()
                )
            ) is UserBusinessCases.EmptyList
        )
    }

    @Test
    fun `should return Success Business Case and the same data count when response is not empty`() {
        val actual = mapper.map(responses)
        Assert.assertTrue(actual is UserBusinessCases.Success)

        val actualUsers = (actual as UserBusinessCases.Success).users
        Assert.assertEquals(actualUsers.size, responses.users.size)

        val actualFirstUser = actualUsers.first()
        val expectedFirstUser = responses.users.first()
        Assert.assertEquals(actualFirstUser.id, expectedFirstUser.id)
        Assert.assertEquals(actualFirstUser.name, expectedFirstUser.login)
        Assert.assertEquals(actualFirstUser.avatarUrl, expectedFirstUser.avatarUrl)
        Assert.assertEquals(actualFirstUser.url, expectedFirstUser.url)
    }

    private companion object {
        const val ID = 1
        const val NAME = "Hing Tang"
        const val AVATAR_URL = "avatar-url"
        const val URL = "url"

        val responses = SearchUserResponse(1, listOf(UserResponse(ID, NAME, AVATAR_URL, URL)))
    }
}
