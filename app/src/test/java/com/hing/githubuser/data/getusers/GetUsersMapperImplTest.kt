package com.hing.githubuser.data.getusers

import com.hing.githubuser.domain.search.UserBusinessCases
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetUsersMapperImplTest {

    private lateinit var mapper: GetUsersMapper

    @Before
    fun setUp() {
        mapper = GetUsersMapperImpl()
    }

    @Test
    fun `should return Empty Business Case when response is null`() {
        assertTrue(mapper.map(null) is UserBusinessCases.EmptyList)
    }

    @Test
    fun `should return Empty Business Case when response is empty`() {
        assertTrue(mapper.map(emptyList()) is UserBusinessCases.EmptyList)
    }

    @Test
    fun `should return Success Business Case and the same data count when response is not empty`() {
        val actual = mapper.map(responses)
        assertTrue(actual is UserBusinessCases.Success)

        val actualUsers = (actual as UserBusinessCases.Success).users
        assertEquals(actualUsers.size, responses.size)

        val actualFirstUser = actualUsers.first()
        val expectedFirstUser = responses.first()
        assertEquals(actualFirstUser.id, expectedFirstUser.id)
        assertEquals(actualFirstUser.name, expectedFirstUser.login)
        assertEquals(actualFirstUser.avatarUrl, expectedFirstUser.avatarUrl)
        assertEquals(actualFirstUser.url, expectedFirstUser.url)
    }

    private companion object {
        const val ID = 1
        const val NAME = "Hing Tang"
        const val AVATAR_URL = "avatar-url"
        const val URL = "url"

        val responses = listOf(UserResponse(ID, NAME, AVATAR_URL, URL))
    }
}
