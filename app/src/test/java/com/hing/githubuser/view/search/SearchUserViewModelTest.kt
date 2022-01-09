package com.hing.githubuser.view.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.hing.githubuser.domain.BusinessState
import com.hing.githubuser.domain.search.*
import com.hing.githubuser.view.UiState
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SearchUserViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    private lateinit var viewModel: SearchUserViewModel
    private lateinit var getUsersUseCase: GetUsersUseCase
    private lateinit var searchUserUseCase: SearchUserUseCase
    private lateinit var getUsersRepository: GetUsersRepository
    private lateinit var searchUserRepository: SearchUserRepository

    @Before
    fun setup() {
        getUsersRepository = mockk()
        searchUserRepository = mockk()
        getUsersUseCase = mockk()
        searchUserUseCase = mockk()

        viewModel = SearchUserViewModel(testDispatcher, getUsersUseCase, searchUserUseCase)
    }

    @Test
    fun `searchUsers should return the correct value when success`() {
        coEvery {
            searchUserUseCase(
                SearchUserRequest(
                    KEYWORD,
                    0,
                    30
                )
            )
        } returns MutableStateFlow(
            BusinessState.BusinessCase<UserBusinessCases>(
                UserBusinessCases.Success(
                    users
                )
            )
        )

        runBlocking { viewModel.searchUser(KEYWORD) }

        viewModel.liveData.observeForever {
            it is UiState.Success
            val firstUser = (it as UiState.Success).data.users.first()
            val expectedUser = users.first()
            firstUser.id shouldBe expectedUser.id
            firstUser.name shouldBe expectedUser.name
            firstUser.avatarUrl shouldBe expectedUser.avatarUrl
            firstUser.url shouldBe expectedUser.url
        }

        viewModel.isLoadMore.value shouldBe false
    }

    @Test
    fun `searchUsers should return the correct value when empty`() {
        coEvery {
            searchUserUseCase(
                SearchUserRequest(
                    KEYWORD,
                    0,
                    30
                )
            )
        } returns MutableStateFlow(
            BusinessState.BusinessCase<UserBusinessCases>(
                UserBusinessCases.EmptyList
            )
        )

        runBlocking { viewModel.searchUser(KEYWORD) }

        viewModel.liveData.observeForever {
            it is UiState.Failed
        }

        viewModel.isLoadMore.value shouldBe false
    }

    @Test
    fun `searchUsers should update isLoadMore value when page is not a zero`() {
        val page = 1
        coEvery {
            searchUserUseCase(
                SearchUserRequest(
                    KEYWORD,
                    page,
                    30
                )
            )
        } returns MutableStateFlow(
            BusinessState.BusinessCase<UserBusinessCases>(
                UserBusinessCases.Success(
                    users
                )
            )
        )

        runBlocking { viewModel.searchUser(KEYWORD, page) }

        viewModel.isLoadMore.value shouldBe true
    }

    @Test
    fun `getUsers should return the correct value when success`() {
        coEvery {
            getUsersUseCase(
                GetUsersRequest(
                    0,
                    30
                )
            )
        } returns MutableStateFlow(
            BusinessState.BusinessCase<UserBusinessCases>(
                UserBusinessCases.Success(
                    users
                )
            )
        )

        runBlocking { viewModel.getUsers() }

        viewModel.liveData.observeForever {
            it is UiState.Success
            val firstUser = (it as UiState.Success).data.users.first()
            val expectedUser = users.first()
            firstUser.id shouldBe expectedUser.id
            firstUser.name shouldBe expectedUser.name
            firstUser.avatarUrl shouldBe expectedUser.avatarUrl
            firstUser.url shouldBe expectedUser.url
        }

        viewModel.isLoadMore.value shouldBe false
    }

    @Test
    fun `getUsers should return the correct value when empty`() {
        coEvery {
            getUsersUseCase(
                GetUsersRequest(
                    0,
                    30
                )
            )
        } returns MutableStateFlow(
            BusinessState.BusinessCase<UserBusinessCases>(
                UserBusinessCases.EmptyList
            )
        )

        runBlocking { viewModel.getUsers() }

        viewModel.liveData.observeForever {
            it is UiState.Failed
        }

        viewModel.isLoadMore.value shouldBe false
    }

    @Test
    fun `getUsers should update isLoadMore when since is not default`() {
        val since = 10
        coEvery {
            getUsersUseCase(
                GetUsersRequest(
                    since,
                    30
                )
            )
        } returns MutableStateFlow(
            BusinessState.BusinessCase<UserBusinessCases>(
                UserBusinessCases.Success(
                    users
                )
            )
        )

        runBlocking { viewModel.getUsers(since) }

        viewModel.isLoadMore.value shouldBe true
    }

    private companion object {
        const val KEYWORD = "Hing"
        val users = listOf(
            UserData(
                1, "Hing", "avatar", "url"
            )
        )
    }
}
