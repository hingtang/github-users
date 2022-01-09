package com.hing.githubuser.view.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hing.githubuser.di.IODispatcher
import com.hing.githubuser.domain.BusinessState
import com.hing.githubuser.domain.collectInScope
import com.hing.githubuser.domain.search.*
import com.hing.githubuser.view.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@HiltViewModel
class SearchUserViewModel @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val getUsersUseCase: GetUsersUseCase,
    private val searchUserUseCase: SearchUserUseCase
) : ViewModel() {

    private var job: Job? = null
    private var userLiveData = MutableLiveData<UserListUiState>()
    private var loadMore = MutableLiveData<Boolean>()

    val liveData: LiveData<UserListUiState>
        get() = userLiveData

    val isLoadMore: LiveData<Boolean>
        get() = loadMore

    fun searchUser(keywords: String, page: Int = DEFAULT_PAGE): Job? {
        job?.cancel()

        loadMore.value = page != DEFAULT_PAGE
        postLoading()

        job = searchUserUseCase(
            SearchUserRequest(keywords, page, ITEMS_PER_PAGE)
        ).flowOn(ioDispatcher)
            .collectInScope(viewModelScope) {
                when (it) {
                    is BusinessState.BusinessCase -> {
                        when (it.data) {
                            is UserBusinessCases.EmptyList -> {
                                postError(UserListState.FailedState.EmptyData)
                            }
                            is UserBusinessCases.Success -> {
                                postSuccess(UserListState.SuccessState().apply {
                                    users = it.data.users.toMutableList()
                                })
                            }
                        }
                    }
                    is BusinessState.GenericCase -> postError()
                }
            }
        return job
    }

    fun getUsers(since: Int = DEFAULT_USER_ID): Job? {
        job?.cancel()

        loadMore.value = since != DEFAULT_USER_ID
        postLoading()

        job = getUsersUseCase(
            GetUsersRequest(since, ITEMS_PER_PAGE)
        ).flowOn(ioDispatcher)
            .collectInScope(viewModelScope) {
                when (it) {
                    is BusinessState.BusinessCase -> {
                        when (it.data) {
                            is UserBusinessCases.EmptyList -> {
                                postError(UserListState.FailedState.EmptyData)
                            }
                            is UserBusinessCases.Success -> {
                                postSuccess(UserListState.SuccessState().apply {
                                    users.addAll(it.data.users)
                                })
                            }
                        }
                    }
                    is BusinessState.GenericCase -> postError()
                }
            }
        return job
    }

    private fun postError(error: UserListState.FailedState = UserListState.FailedState.Error) {
        userLiveData.postValue(UiState.Failed(error))
    }

    private fun postLoading() {
        userLiveData.postValue(UiState.Loading(UserListState.LoadingState))
    }

    private fun postSuccess(users: UserListState.SuccessState) {
        userLiveData.postValue(UiState.Success(users))
    }

    companion object {
        private const val DEFAULT_USER_ID = 0
        private const val DEFAULT_PAGE = 0
        private const val ITEMS_PER_PAGE = 30
    }
}
