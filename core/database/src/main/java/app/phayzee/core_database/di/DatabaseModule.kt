package app.phayzee.core_database.di

import android.content.Context
import androidx.room.Room
import app.phayzee.core_database.AppDatabase
import app.phayzee.core_database.dao.ProductDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module that provides database-related dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    /**
     * Provides the Room database instance.
     * Database is created as a singleton to ensure only one instance exists.
     *
     * Note: AppDatabase extends RoomDatabase, which is required by Room.
     */
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "modern_arch_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    /**
     * Provides ProductDao from the database instance.
     * Room automatically generates the implementation at compile time.
     */
    @Provides
    @Singleton
    fun provideProductDao(database: AppDatabase): ProductDao {
        return database.productDao()
    }
}