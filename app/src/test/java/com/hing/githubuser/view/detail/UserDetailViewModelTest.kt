package com.hing.githubuser.view.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.hing.githubuser.domain.BusinessState
import com.hing.githubuser.domain.userdetail.*
import com.hing.githubuser.view.UiState
import com.hing.githubuser.view.search.MainCoroutineRule
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
class UserDetailViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    private lateinit var viewModel: UserDetailViewModel
    private lateinit var userDetailUseCase: UserDetailUseCase
    private lateinit var repository: UserDetailRepository

    @Before
    fun setup() {
        repository = mockk()
        userDetailUseCase = mockk()

        viewModel = UserDetailViewModel(testDispatcher, userDetailUseCase)
    }

    @Test
    fun `getUserDetail should return the correct value when success`() {
        coEvery { userDetailUseCase(UserDetailRequest(USERNAME)) } returns MutableStateFlow(
            BusinessState.BusinessCase<UserDetailBusinessCases>(
                UserDetailBusinessCases.Success(userDetailData)
            )
        )

        runBlocking { viewModel.getUserDetail(USERNAME) }

        viewModel.liveData.observeForever {
            it is UiState.Success
            val userDetail = (it as UiState.Success).data.userDetail
            userDetail.id shouldBe userDetailData.id
            userDetail.name shouldBe userDetailData.name
            userDetail.avatarUrl shouldBe userDetailData.avatarUrl
            userDetail.url shouldBe userDetailData.url
        }
    }

    @Test
    fun `getUserDetail should return the correct value when empty`() {
        coEvery { userDetailUseCase(UserDetailRequest(USERNAME)) } returns MutableStateFlow(
            BusinessState.BusinessCase<UserDetailBusinessCases>(
                UserDetailBusinessCases.EmptyData
            )
        )

        runBlocking { viewModel.getUserDetail(USERNAME) }

        viewModel.liveData.observeForever {
            it is UiState.Failed
        }
    }

    private companion object {
        const val ID = 1L
        const val NAME = "Hing Tang"
        const val AVATAR_URL = "avatar-url"
        const val URL = "url"
        const val USERNAME = "user-name"

        val request = UserDetailRequest("Hing")
        val userDetailData = UserDetailData(ID, NAME, NAME, AVATAR_URL, null, 1, 1, 1, URL)
    }
}
