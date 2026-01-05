package app.phayzee.core_common.extensions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import app.phayzee.core_common.result.Result

/**
 * Extension functions for Flow to simplify Result handling
 */

/**
 * Wraps Flow emissions in Result.Success and handles errors with Result.Error.
 * Also emits Result.Loading at the start.
 *
 * Usage:
 * ```
 * repository.getProducts()
 *     .asResult()
 *     .collect { result ->
 *         when (result) {
 *             is Result.Loading -> showLoading()
 *             is Result.Success -> showData(result.data)
 *             is Result.Error -> showError(result.message)
 *         }
 *     }
 * ```
 */
fun <T> Flow<T>.asResult(): Flow<Result<T>> {
    return this
        .map<T, Result<T>> { Result.Success(it) }
        .onStart { emit(Result.Loading) }
        .catch { emit(Result.Error(Exception(it))) }
}

/**
 * Maps Flow<T> to Flow<Result<T>> without loading state
 */
fun <T> Flow<T>.asResultWithoutLoading(): Flow<Result<T>> {
    return this
        .map<T, Result<T>> { Result.Success(it) }
        .catch { emit(Result.Error(Exception(it))) }
}

/**
 * String Extensions
 */

/**
 * Capitalizes the first letter of each word
 */
fun String.capitalizeWords(): String {
    return split(" ").joinToString(" ") { word ->
        word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }
}

/**
 * Checks if string is a valid email format
 */
fun String.isValidEmail(): Boolean {
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
    return matches(emailRegex)
}

/**
 * Truncates string to max length and adds ellipsis
 */
fun String.truncate(maxLength: Int, ellipsis: String = "..."): String {
    return if (length <= maxLength) this
    else take(maxLength - ellipsis.length) + ellipsis
}

/**
 * Collection Extensions
 */

/**
 * Safely gets an element at index, returns null if out of bounds
 */
fun <T> List<T>.getOrNull(index: Int): T? {
    return if (index in indices) this[index] else null
}

/**
 * Returns true if collection is not null and not empty
 */
fun <T> Collection<T>?.isNotNullOrEmpty(): Boolean {
    return this != null && this.isNotEmpty()
}

/**
 * Returns true if collection is null or empty
 */
fun <T> Collection<T>?.isNullOrEmpty(): Boolean {
    return this == null || this.isEmpty()
}