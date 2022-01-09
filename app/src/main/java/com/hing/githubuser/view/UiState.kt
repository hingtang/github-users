package com.hing.githubuser.view

sealed class UiState <out T, out E, out L>{
    data class Success<out T>(val data: T) : UiState<T, Nothing, Nothing>()
    data class Failed<out E>(val error: E) : UiState<Nothing, E, Nothing>()
    data class Loading<out L>(val data: L) : UiState<Nothing, Nothing, L>()
}
