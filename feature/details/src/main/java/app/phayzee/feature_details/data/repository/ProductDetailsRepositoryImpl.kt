package app.phayzee.feature_details.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import app.phayzee.core_common.result.Result
import app.phayzee.core_database.dao.ProductDao
import app.phayzee.core_network.api.ProductApi
import app.phayzee.feature_details.domain.repository.ProductDetailsRepository
import app.phayzee.feature_home.data.mapper.toDomainModel
import app.phayzee.feature_home.data.mapper.toEntity
import app.phayzee.feature_home.domain.model.Product
import javax.inject.Inject

/**
 * Implementation of ProductDetailsRepository.
 *
 * Strategy:
 * - Observe product from database (offline-first)
 * - Refresh from API on demand
 * - Update database, which triggers observer
 */
class ProductDetailsRepositoryImpl @Inject constructor(
    private val productApi: ProductApi,
    private val productDao: ProductDao
) : ProductDetailsRepository {

    /**
     * Observes a single product from local database.
     * Emits whenever the product is updated.
     */
    override fun observeProductById(productId: Int): Flow<Result<Product>> {
        return productDao.observeProductById(productId)
            .map { entity ->
                if (entity != null) {
                    entity.toDomainModel()
                } else {
                    null
                }
            }
            .map { product ->
                if (product != null) {
                    Result.Success(product)
                } else {
                    Result.Error(
                        Exception("Product not found"),
                        "Product with ID $productId not found"
                    )
                }
            }
    }

    /**
     * Refreshes product details from API and updates cache.
     */
    override suspend fun refreshProductDetails(productId: Int): Result<Unit> {
        return try {
            // Fetch from API
            val dto = productApi.getProductById(productId)

            // Convert and save to database
            val product = dto.toDomainModel()
            val entity = product.toEntity()
            productDao.insertProduct(entity)

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e, "Failed to refresh product details: ${e.localizedMessage}")
        }
    }
}