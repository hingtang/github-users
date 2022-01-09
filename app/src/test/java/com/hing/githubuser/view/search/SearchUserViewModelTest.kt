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

    lateinit var viewModel: SearchUserViewModel
    lateinit var getUsersUseCase: GetUsersUseCase
    lateinit var getUsersRepository: GetUsersRepository

    @Before
    fun setup() {
        getUsersRepository = mockk()
        getUsersUseCase = mockk()

        viewModel = SearchUserViewModel(testDispatcher, getUsersUseCase)
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

    private companion object {
        val users = listOf(
            UserData(
                1, "Hing", "avatar", "url"
            )
        )
    }
}
