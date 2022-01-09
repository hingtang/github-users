package com.hing.githubuser.domain

open class Exception : RuntimeException()

data class BusinessException(val code: String, override val message: String) :
    Exception()

data class TechnicalException(val code: Int, override val message: String) :
    Exception()

data class UnknownException(val code: Int = -100, val throwable: Throwable) : Exception()

fun Throwable.code() =
    when (this) {
        is BusinessException -> code
        is TechnicalException -> code.toString()
        else -> "Unknown"
    }
