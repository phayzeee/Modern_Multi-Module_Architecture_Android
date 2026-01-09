package app.phayzee.feature_home.data.repository

import app.phayzee.core_common.extensions.asResultWithoutLoading
import app.phayzee.core_database.dao.ProductDao
import app.phayzee.core_network.api.ProductApi
import app.phayzee.feature_home.domain.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import app.phayzee.core_common.result.Result
import app.phayzee.feature_home.data.mapper.toDomainModel
import app.phayzee.feature_home.data.mapper.toDomainModels
import app.phayzee.feature_home.data.mapper.toEntities
import app.phayzee.feature_home.domain.repository.ProductRepository
import javax.inject.Inject

/**
 * Implementation of ProductRepository.
 *
 * Architecture: Offline-First
 *
 * Strategy:
 * 1. Always read from database (single source of truth)
 * 2. Fetch from API in background
 * 3. Update database, which triggers UI update automatically
 *
 * Benefits:
 * - App works offline
 * - Instant data display (no loading spinners for cached data)
 * - Network calls don't block UI
 * - Automatic UI updates when data changes
 */
class ProductRepositoryImpl @Inject constructor(
    private val productApi: ProductApi,
    private val productDao: ProductDao
) : ProductRepository {

    /**
     * Observes products from local database.
     *
     * Flow automatically emits new data when:
     * - Initial subscription
     * - Database is updated (e.g., after API refresh)
     * - Any CRUD operation on products table
     *
     * The UI just observes this and always has the latest data.
     */
    override fun observeProducts(): Flow<Result<List<Product>>> {
        return productDao.observeAllProducts()
            .map { entities -> entities.toDomainModels() }
            .asResultWithoutLoading()
    }

    /**
     * Refreshes products from API and updates cache.
     *
     * Flow:
     * 1. Call API
     * 2. Map DTOs to domain models
     * 3. Map domain models to entities
     * 4. Save to database (triggers observeProducts() to emit)
     *
     * Error Handling:
     * - Network errors are caught and returned as Result.Error
     * - If refresh fails, cached data is still available via observeProducts()
     */
    override suspend fun refreshProducts(forceRefresh: Boolean): Result<Unit> {
        return try {
            // Fetch from API
            val dtos = productApi.getProducts()

            // Convert to domain models
            val products = dtos.toDomainModels()

            // Save to database
            val entities = products.toEntities()
            productDao.insertProducts(entities)

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e, "Failed to refresh products: ${e.localizedMessage}")
        }
    }

    /**
     * Gets a single product by ID from the cache.
     *
     * Note: This is a one-time fetch, not reactive.
     * For reactive behavior, use a Flow-based method.
     */
    override suspend fun getProductById(productId: Int): Result<Product> {
        return try {
            val entity = productDao.getProductById(productId)
            if (entity != null) {
                Result.Success(entity.toDomainModel())
            } else {
                Result.Error(
                    Exception("Product not found"),
                    "Product with ID $productId not found in cache"
                )
            }
        } catch (e: Exception) {
            Result.Error(e, "Failed to get product: ${e.localizedMessage}")
        }
    }
}