package com.hing.githubuser.view.detail

import com.hing.githubuser.domain.userdetail.UserDetailData
import com.hing.githubuser.view.UiState

sealed class UserDetailState {
    // Success
    class SuccessState(userDetailData: UserDetailData) : UserDetailState() {
        val userDetail = userDetailData
    }

    // Loading
    object LoadingState : UserDetailState()

    // Failed
    object FailedState : UserDetailState()
}

typealias UserDetailUiState = UiState<UserDetailState.SuccessState,
        UserDetailState.FailedState,
        UserDetailState.LoadingState>
