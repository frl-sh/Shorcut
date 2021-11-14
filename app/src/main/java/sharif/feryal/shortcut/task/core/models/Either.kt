package sharif.feryal.shortcut.task.core.models

sealed class Either<out V> {

    data class Success<V>(val value: V) : Either<V>()

    data class Failure(val error: Throwable) : Either<Nothing>()
}

/**
 * @return [Either.Success.value] or `null`
 */
fun <V> Either<V>.getOrNull(): V? = (this as? Either.Success)?.value

/**
 * @return [Either.Failure.error] or `null`
 */
fun <V> Either<V>.getFailureOrNull(): Throwable? = (this as? Either.Failure)?.error

/**
 * @return true if response is successful, and false otherwise
 */
fun <V> Either<V>.isSuccessFull(): Boolean = this is Either.Success

/**
 * The given function is applied if this is a `Success`.
 */
inline fun <V, V2> Either<V>.map(transform: (V) -> V2): Either<V2> = when (this) {
    is Either.Failure -> this
    is Either.Success -> Either.Success(transform(value))
}

/**
 * Applies `ifSuccess` if this is a [Either.Success] or `ifFailure` if this is a [Either.Failure].
 *
 * @param ifSuccess the function to apply if this is a [Either.Success]
 * @param ifFailure the function to apply if this is a [Either.Failure]
 * @return the results of applying the function
 */
inline fun <R, T> Either<T>.fold(
    ifSuccess: (value: T) -> R,
    ifFailure: (failure: Throwable) -> R
): R {
    return when (this) {
        is Either.Success<T> -> ifSuccess(value)
        is Either.Failure -> ifFailure(error)
    }
}