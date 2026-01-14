package app.phayzee.feature_home.data.mapper

import app.phayzee.core_database.entity.ProductEntity
import app.phayzee.core_network.model.ProductDto
import app.phayzee.feature_home.domain.model.Product
import app.phayzee.feature_home.domain.model.Rating

/**
 * Mapper functions to convert between different data representations.
 *
 * Data Flow:
 * API (DTO) → Domain Model → Database Entity
 *           ↓
 *        UI Layer
 *
 * Why separate representations?
 * - API can change without affecting business logic
 * - Database schema can evolve independently
 * - Domain model stays clean and focused on business needs
 */

/**
 * Converts API DTO to Domain Model
 */
fun ProductDto.toDomainModel(): Product {
    return Product(
        id = id,
        title = title,
        price = price,
        description = description,
        category = category,
        imageUrl = image,
        rating = Rating(
            value = rating.rate,
            count = rating.count
        )
    )
}

/**
 * Converts Domain Model to Database Entity
 */
fun Product.toEntity(): ProductEntity {
    return ProductEntity(
        id = id,
        title = title,
        price = price,
        description = description,
        category = category,
        imageUrl = imageUrl,
        rating = rating.value,
        ratingCount = rating.count,
        lastUpdated = System.currentTimeMillis()
    )
}

/**
 * Converts Database Entity to Domain Model
 */
fun ProductEntity.toDomainModel(): Product {
    return Product(
        id = id,
        title = title,
        price = price,
        description = description,
        category = category,
        imageUrl = imageUrl,
        rating = Rating(
            value = rating,
            count = ratingCount
        )
    )
}

/**
 * Extension functions for lists
 */
fun List<ProductDto>.dtoToDomainModels(): List<Product> = map { it.toDomainModel() }
fun List<Product>.toEntities(): List<ProductEntity> = map { it.toEntity() }
fun List<ProductEntity>.entityToDomainModels(): List<Product> = map { it.toDomainModel() }