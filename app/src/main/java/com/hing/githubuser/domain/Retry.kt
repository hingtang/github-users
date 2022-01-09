package com.hing.githubuser.domain

import kotlin.math.pow
import kotlin.random.Random
import kotlinx.coroutines.delay

val DEFAULT_DELAY_TIME: (Long) -> Long = { 500L }
val EXPONENTIAL_BACKOFF_DELAY_TIME: (Long) -> Long = { currAttempt ->
    ((2.0).pow(currAttempt.toInt()) * 1000L + (500 * Random.nextFloat())).toLong()
}

data class Retry(
    val predicate: suspend (Throwable) -> Boolean,
    val maxAttempt: Int,
    val delayTime: (Long) -> Long
) {

    @FlowStateConfigMarker
    class Config(
        var predicate: suspend (Throwable) -> Boolean = { false },
        var maxAttempt: Int = 0,
        var delayTime: (Long) -> Long = DEFAULT_DELAY_TIME
    )

    suspend operator fun invoke(throwable: Throwable, attempt: Long) =
        (predicate(throwable) && attempt < maxAttempt)
            .also { shouldRetry ->
                if (shouldRetry)
                    delay(delayTime(attempt))
            }
}

fun retryOf(block: Retry.Config.() -> Unit): Retry =
    with(Retry.Config().apply(block)) {
        Retry(predicate, maxAttempt, delayTime)
    }

val DefaultRetry = retryOf {
    predicate = {
        (it is TechnicalException && it.code == 500) ||
            (it is UnknownException && it.code in listOf(500, 503))
    }
    maxAttempt = 3
    delayTime = EXPONENTIAL_BACKOFF_DELAY_TIME
}

val NoRetry = retryOf {
    predicate = { false }
    maxAttempt = 0
    delayTime = { 0L }
}
