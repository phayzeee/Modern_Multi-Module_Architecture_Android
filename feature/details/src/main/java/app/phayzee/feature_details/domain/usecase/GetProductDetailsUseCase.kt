package app.phayzee.feature_details.domain.usecase

import app.phayzee.core_common.base.FlowUseCase
import app.phayzee.core_common.result.Result
import app.phayzee.feature_details.domain.repository.ProductDetailsRepository
import app.phayzee.feature_home.domain.model.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for observing product details.
 *
 * This use case:
 * - Observes a single product from cache
 * - Automatically updates when cache changes
 * - Follows offline-first pattern
 *
 * Single Responsibility: Observing product details
 */
class ObserveProductDetailsUseCase @Inject constructor(
    private val repository: ProductDetailsRepository
) : FlowUseCase<Int, Product>() {

    override fun execute(params: Int): Flow<Result<Product>> {
        return repository.observeProductById(params)
    }
}