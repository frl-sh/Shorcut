package sharif.feryal.shortcut.task.core.extension

import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import sharif.feryal.shortcut.task.core.models.Either
import kotlin.coroutines.resume

private const val ERROR_RESPONSE_CANCEL = 699
private const val ERROR_RESPONSE_UNKNOWN = 599

suspend fun <T, R> Call<T>.awaitEither(
    map: (T) -> R
): Either<R> = enqueue(map)

private suspend fun <T, R> Call<T>.enqueue(
    map: (T) -> R,
    onSuccess: ((response: Response<T>) -> Unit)? = null,
    onFailure: ((throwable: Throwable, errorCode: Int) -> Unit)? = null,
    onCanceled: (() -> Unit)? = null
): Either<R> = suspendCancellableCoroutine { continuation ->
    try {
        enqueue(object : Callback<T> {
            override fun onFailure(call: Call<T>, throwable: Throwable) {
                errorHappened(ERROR_RESPONSE_UNKNOWN, throwable)
            }

            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    try {
                        onSuccess?.invoke(response)
                        continuation.resume(Either.Success(map(response.body()!!)))
                    } catch (throwable: Throwable) {
                        errorHappened(response.code(), throwable)
                    }
                } else {
                    errorHappened(response.code(), HttpException(response))
                }
            }

            private fun errorHappened(errorCode: Int, throwable: Throwable) {
                onFailure?.invoke(throwable, errorCode)
                continuation.resume(Either.Failure(throwable))
            }
        })
    } catch (throwable: Throwable) {
        onFailure?.invoke(throwable, ERROR_RESPONSE_UNKNOWN)
        continuation.resume(Either.Failure(throwable))
    }

    continuation.invokeOnCancellation { error ->
        error?.let { throwable ->
            onFailure?.invoke(throwable, ERROR_RESPONSE_CANCEL)
        }
        onCanceled?.invoke()
        cancel()
    }
}