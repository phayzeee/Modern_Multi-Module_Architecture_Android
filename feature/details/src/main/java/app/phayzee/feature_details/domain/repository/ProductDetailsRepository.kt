package app.phayzee.feature_details.domain.repository


import app.phayzee.core_common.result.Result
import app.phayzee.feature_home.domain.model.Product
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Product Details operations.
 *
 * Note: We reuse the Product domain model from feature:home
 * to maintain consistency across features.
 */
interface ProductDetailsRepository {

    /**
     * Observes a single product by ID from the local database.
     * Returns a Flow that emits whenever the product data changes.
     *
     * @param productId Product identifier
     * @return Flow of Result containing the product
     */
    fun observeProductById(productId: Int): Flow<Result<Product>>

    /**
     * Fetches fresh product details from the API and updates cache.
     *
     * @param productId Product identifier
     * @return Result of the operation
     */
    suspend fun refreshProductDetails(productId: Int): Result<Unit>
}