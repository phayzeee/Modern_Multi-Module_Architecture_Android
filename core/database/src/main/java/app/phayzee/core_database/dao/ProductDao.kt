package app.phayzee.core_database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.phayzee.core_database.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for Product operations.
 *
 * Room automatically generates implementations for these methods.
 * Use Flow for reactive queries that automatically update when data changes.
 */
@Dao
interface ProductDao {

    /**
     * Observes all products in the database.
     * Returns a Flow that emits whenever the products table changes.
     *
     * Perfect for offline-first: UI observes this and always shows cached data,
     * even while fetching fresh data from the network.
     */
    @Query("SELECT * FROM products ORDER BY id ASC")
    fun observeAllProducts(): Flow<List<ProductEntity>>

    /**
     * Gets all products as a one-time fetch (not reactive)
     */
    @Query("SELECT * FROM products ORDER BY id ASC")
    suspend fun getAllProducts(): List<ProductEntity>

    /**
     * Observes a single product by ID
     */
    @Query("SELECT * FROM products WHERE id = :productId")
    fun observeProductById(productId: Int): Flow<ProductEntity?>

    /**
     * Gets a single product by ID (one-time fetch)
     */
    @Query("SELECT * FROM products WHERE id = :productId")
    suspend fun getProductById(productId: Int): ProductEntity?

    /**
     * Gets products by category
     */
    @Query("SELECT * FROM products WHERE category = :category ORDER BY id ASC")
    fun observeProductsByCategory(category: String): Flow<List<ProductEntity>>

    /**
     * Inserts a list of products into the database.
     * OnConflictStrategy.REPLACE means if a product with the same ID exists,
     * it will be replaced with the new data (upsert behavior).
     *
     * This is perfect for caching API responses.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductEntity>)

    /**
     * Inserts a single product
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)

    /**
     * Deletes all products from the database.
     * Useful for cache invalidation or user logout.
     */
    @Query("DELETE FROM products")
    suspend fun deleteAllProducts()

    /**
     * Deletes a specific product by ID
     */
    @Query("DELETE FROM products WHERE id = :productId")
    suspend fun deleteProductById(productId: Int)

    /**
     * Gets the count of products in the database.
     * Useful for checking if cache is empty.
     */
    @Query("SELECT COUNT(*) FROM products")
    suspend fun getProductCount(): Int

    /**
     * Checks if data is stale (older than specified time).
     * Returns products that need refreshing.
     *
     * @param timestamp Cutoff time in milliseconds
     */
    @Query("SELECT * FROM products WHERE last_updated < :timestamp")
    suspend fun getStaleProducts(timestamp: Long): List<ProductEntity>
}