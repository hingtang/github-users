package com.hing.githubuser.domain

sealed class GenericBusinessCase : Throwable() {
    data class UnknownError(val throws: Throwable?) : GenericBusinessCase()
    data class MapperToBusinessError(val ex: Throwable) : GenericBusinessCase()
    data class ApiError(val errorCode: String?, val errorMessage: String?) : GenericBusinessCase()
}
