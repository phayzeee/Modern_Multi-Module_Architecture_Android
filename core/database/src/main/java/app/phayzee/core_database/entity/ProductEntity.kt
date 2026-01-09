package app.phayzee.core_database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity representing a Product in the local database.
 *
 * This is the database representation - separate from:
 * - Domain models (used in business logic)
 * - DTOs (used for API responses)
 * - UI models (used in presentation layer)
 *
 * Separating these concerns allows each layer to evolve independently.
 */
@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "price")
    val price: Double,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "category")
    val category: String,

    @ColumnInfo(name = "image_url")
    val imageUrl: String,

    @ColumnInfo(name = "rating")
    val rating: Double,

    @ColumnInfo(name = "rating_count")
    val ratingCount: Int,

    /**
     * Timestamp when this product was last fetched from the API.
     * Useful for implementing cache invalidation strategies.
     */
    @ColumnInfo(name = "last_updated")
    val lastUpdated: Long = System.currentTimeMillis()
)