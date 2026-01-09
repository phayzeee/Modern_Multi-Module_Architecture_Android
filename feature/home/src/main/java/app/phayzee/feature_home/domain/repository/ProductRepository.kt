package app.phayzee.feature_home.domain.repository

import app.phayzee.feature_home.domain.model.Product
import app.phayzee.core_common.result.Result
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Product operations.
 *
 * This is the contract defined by the domain layer.
 * The data layer will provide the implementation.
 *
 * Benefits of this approach:
 * - Domain layer doesn't depend on data layer implementation
 * - Easy to swap implementations (mock for testing, different APIs, etc.)
 * - Follows Dependency Inversion Principle (SOLID)
 */
interface ProductRepository {

    /**
     * Observes products from the local database.
     * Returns a Flow that emits whenever data changes.
     *
     * Offline-first: Always returns cached data immediately,
     * even while fetching fresh data from network.
     *
     * @return Flow of Result containing list of products
     */
    fun observeProducts(): Flow<Result<List<Product>>>

    /**
     * Fetches fresh products from the API and updates the cache.
     *
     * This method:
     * 1. Fetches from API
     * 2. Saves to database
     * 3. Returns the result
     *
     * The UI doesn't need to call this explicitly if using observeProducts(),
     * as the database Flow will automatically emit the new data.
     *
     * @param forceRefresh If true, bypasses any caching logic
     * @return Result of the operation
     */
    suspend fun refreshProducts(forceRefresh: Boolean = false): Result<Unit>

    /**
     * Gets a single product by ID from cache.
     *
     * @param productId Product identifier
     * @return Result containing the product or error
     */
    suspend fun getProductById(productId: Int): Result<Product>
}