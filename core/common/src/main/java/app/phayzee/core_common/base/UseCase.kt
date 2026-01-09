package app.phayzee.core_common.base

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import app.phayzee.core_common.result.Result

/**
 * Base class for Use Cases that execute business logic.
 * Follows Single Responsibility Principle - each use case does ONE thing.
 *
 * Type parameters:
 * @param Params Input parameters for the use case
 * @param ReturnType Output type wrapped in Result
 *
 * Example:
 * ```
 * class GetProductsUseCase @Inject constructor(
 *     private val repository: ProductRepository
 * ) : UseCase<Unit, List<Product>>() {
 *
 *     override suspend fun execute(params: Unit): Result<List<Product>> {
 *         return repository.getProducts()
 *     }
 * }
 * ```
 */
abstract class UseCase<in Params, out ReturnType>(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    /**
     * Execute the use case with given parameters.
     * Runs on the IO dispatcher by default.
     */
    suspend operator fun invoke(params: Params): Result<ReturnType> {
        return try {
            withContext(dispatcher) {
                execute(params)
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Override this method to implement the use case logic
     */
    protected abstract suspend fun execute(params: Params): Result<ReturnType>
}

/**
 * Base class for Use Cases that return a Flow (for reactive/streaming data).
 * Useful for observing data changes from database or real-time updates.
 *
 * Example:
 * ```
 * class ObserveProductsUseCase @Inject constructor(
 *     private val repository: ProductRepository
 * ) : FlowUseCase<Unit, List<Product>>() {
 *
 *     override fun execute(params: Unit): Flow<Result<List<Product>>> {
 *         return repository.observeProducts()
 *             .map { Result.Success(it) }
 *     }
 * }
 * ```
 */
abstract class FlowUseCase<in Params, out ReturnType>(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    /**
     * Execute the use case and return a Flow.
     * Automatically handles errors and switches to IO dispatcher.
     */
    operator fun invoke(params: Params): Flow<Result<ReturnType>> {
        return execute(params)
            .catch { e -> emit(Result.Error(Exception(e))) }
            .flowOn(dispatcher)
    }

    /**
     * Override this method to implement the use case logic
     */
    protected abstract fun execute(params: Params): Flow<Result<ReturnType>>
}