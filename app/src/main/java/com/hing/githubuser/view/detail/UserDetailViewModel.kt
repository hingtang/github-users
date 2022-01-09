package com.hing.githubuser.view.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hing.githubuser.di.IODispatcher
import com.hing.githubuser.domain.BusinessState
import com.hing.githubuser.domain.collectInScope
import com.hing.githubuser.domain.userdetail.UserDetailBusinessCases
import com.hing.githubuser.domain.userdetail.UserDetailRequest
import com.hing.githubuser.domain.userdetail.UserDetailUseCase
import com.hing.githubuser.view.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val userDetailUseCase: UserDetailUseCase
) : ViewModel() {

    private var job: Job? = null
    private var userDetailLiveData = MutableLiveData<UserDetailUiState>()

    val liveData: LiveData<UserDetailUiState>
        get() = userDetailLiveData

    fun getUserDetail(username: String): Job? {
        job?.cancel()

        postLoading()

        job = userDetailUseCase(
            UserDetailRequest(username)
        ).flowOn(ioDispatcher)
            .collectInScope(viewModelScope) {
                when (it) {
                    is BusinessState.BusinessCase -> {
                        when (it.data) {
                            is UserDetailBusinessCases.Success -> {
                                postSuccess(UserDetailState.SuccessState(it.data.userDetail))
                            }
                            else -> {
                                postError()
                            }
                        }
                    }
                    is BusinessState.GenericCase -> postError()
                }
            }
        return job
    }

    private fun postError() {
        userDetailLiveData.postValue(UiState.Failed(UserDetailState.FailedState))
    }

    private fun postLoading() {
        userDetailLiveData.postValue(UiState.Loading(UserDetailState.LoadingState))
    }

    private fun postSuccess(userDetail: UserDetailState.SuccessState) {
        userDetailLiveData.postValue(UiState.Success(userDetail))
    }
}
