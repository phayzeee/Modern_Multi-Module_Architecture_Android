package app.phayzee.feature_home.domain.usecase

import app.phayzee.core_common.base.FlowUseCase
import app.phayzee.core_common.base.UseCase
import app.phayzee.feature_home.domain.model.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import app.phayzee.core_common.result.Result
import app.phayzee.feature_home.domain.repository.ProductRepository

/**
 * Use case for observing products from the repository.
 *
 * This use case:
 * - Returns cached products immediately (offline-first)
 * - Automatically updates when cache changes
 * - Perfect for reactive UI that always shows data
 *
 * Single Responsibility: Observing product list
 */
class ObserveProductsUseCase @Inject constructor(
    private val repository: ProductRepository
) : FlowUseCase<Unit, List<Product>>() {

    override fun execute(params: Unit): Flow<Result<List<Product>>> {
        return repository.observeProducts()
    }
}

/**
 * Use case for refreshing products from the API.
 *
 * This use case:
 * - Fetches fresh data from network
 * - Updates the local cache
 * - Triggers ObserveProductsUseCase to emit new data
 *
 * Single Responsibility: Refreshing product data
 */
class RefreshProductsUseCase @Inject constructor(
    private val repository: ProductRepository
) : UseCase<Boolean, Unit>() {

    override suspend fun execute(params: Boolean): Result<Unit> {
        return repository.refreshProducts(forceRefresh = params)
    }
}

/**
 * Use case for getting a single product by ID.
 *
 * Single Responsibility: Fetching individual product details
 */
class GetProductByIdUseCase @Inject constructor(
    private val repository: ProductRepository
) : UseCase<Int, Product>() {

    override suspend fun execute(params: Int): Result<Product> {
        return repository.getProductById(params)
    }
}