package sharif.feryal.shortcut.task.core.models

sealed class Either<out V> {

    data class Success<V>(val value: V) : Either<V>()

    data class Failure(val error: Throwable) : Either<Nothing>()
}

/**
 * Applies `onSuccess` if this is a [Either.Success] or `onFailure` if this is a [Either.Failure].
 *
 * @param onSuccess the function to apply if this is a [Either.Success]
 * @param onFailure the function to apply if this is a [Either.Failure]
 * @return the results of applying the function
 */
inline fun <R, T> Either<T>.fold(
    onSuccess: (value: T) -> R,
    onFailure: (failure: Throwable) -> R
): R {
    return when (this) {
        is Either.Success<T> -> onSuccess(value)
        is Either.Failure -> onFailure(error)
    }
}