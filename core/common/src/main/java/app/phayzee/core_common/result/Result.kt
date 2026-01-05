package app.phayzee.core_common.result

/**
 * A sealed class representing the result of an operation.
 * Provides type-safe error handling and eliminates the need for try-catch blocks.
 *
 * Usage:
 * ```
 * suspend fun fetchData(): Result<Data> {
 *     return try {
 *         val data = api.getData()
 *         Result.Success(data)
 *     } catch (e: Exception) {
 *         Result.Error(e)
 *     }
 * }
 * ```
 */
sealed class Result<out T> {

    /**
     * Success state with data of type [T]
     */
    data class Success<T>(val data: T) : Result<T>()

    /**
     * Error state with exception details
     */
    data class Error(
        val exception: Throwable,
        val message: String = exception.localizedMessage ?: "An unknown error occurred"
    ) : Result<Nothing>()

    /**
     * Loading state - useful for UI to show progress indicators
     */
    data object Loading : Result<Nothing>()

    /**
     * Returns true if this is a Success result
     */
    val isSuccess: Boolean
        get() = this is Success

    /**
     * Returns true if this is an Error result
     */
    val isError: Boolean
        get() = this is Error

    /**
     * Returns true if this is a Loading result
     */
    val isLoading: Boolean
        get() = this is Loading

    /**
     * Returns data if Success, null otherwise
     */
    fun getOrNull(): T? = when (this) {
        is Success -> data
        else -> null
    }

    /**
     * Returns data if Success, throws exception if Error, null if Loading
     */
    fun getOrThrow(): T = when (this) {
        is Success -> data
        is Error -> throw exception
        is Loading -> throw IllegalStateException("Cannot get data from Loading state")
    }

    companion object {
        /**
         * Wraps a block of code that may throw an exception into a Result
         */
        inline fun <T> runCatching(block: () -> T): Result<T> {
            return try {
                Success(block())
            } catch (e: Exception) {
                Error(e)
            }
        }
    }
}

/**
 * Extension function to map Success data to another type
 */
inline fun <T, R> Result<T>.map(transform: (T) -> R): Result<R> {
    return when (this) {
        is Result.Success -> Result.Success(transform(data))
        is Result.Error -> Result.Error(exception, message)
        is Result.Loading -> Result.Loading
    }
}

/**
 * Extension function to execute code on Success
 */
inline fun <T> Result<T>.onSuccess(action: (T) -> Unit): Result<T> {
    if (this is Result.Success) {
        action(data)
    }
    return this
}

/**
 * Extension function to execute code on Error
 */
inline fun <T> Result<T>.onError(action: (Throwable) -> Unit): Result<T> {
    if (this is Result.Error) {
        action(exception)
    }
    return this
}

/**
 * Extension function to execute code on Loading
 */
inline fun <T> Result<T>.onLoading(action: () -> Unit): Result<T> {
    if (this is Result.Loading) {
        action()
    }
    return this
}