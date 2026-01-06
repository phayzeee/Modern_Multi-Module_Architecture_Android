package app.phayzee.core_database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import app.phayzee.core_database.converter.Converters
import app.phayzee.core_database.dao.ProductDao
import app.phayzee.core_database.entity.ProductEntity

/**
 * Room Database for the application.
 *
 * IMPORTANT: Must extend RoomDatabase - this is required by Room.
 *
 * - Entities: Define all database tables here
 * - DAOs: Access points for database operations
 * - Version: Increment when schema changes (and provide migrations)
 *
 * Offline-First Strategy:
 * This database acts as the single source of truth for the app.
 * Data flows: API → Database → UI
 */
@Database(
    entities = [
        ProductEntity::class
    ],
    version = 1,
    exportSchema = true // Generates schema JSON for version control
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Provides access to product-related database operations.
     * Room will generate the implementation at compile time.
     */
    abstract fun productDao(): ProductDao
}