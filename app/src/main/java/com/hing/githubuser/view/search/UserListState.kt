package com.hing.githubuser.view.search

import com.hing.githubuser.domain.search.UserData
import com.hing.githubuser.view.UiState

sealed class UserListState {
    // Success
    class SuccessState : UserListState() {
        var users = mutableListOf<UserData>()
    }

    // Loading
    object LoadingState : UserListState()

    // Failed
    sealed class FailedState : UserListState() {
        object EmptyData : FailedState()
        object Error : FailedState()
    }
}

typealias UserListUiState = UiState<UserListState.SuccessState,
        UserListState.FailedState,
        UserListState.LoadingState>
