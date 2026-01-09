package app.phayzee.feature_home.di

import app.phayzee.feature_home.data.repository.ProductRepositoryImpl
import app.phayzee.feature_home.domain.repository.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for Home feature.
 *
 * Provides:
 * - Repository implementation binding
 *
 * Note: Use cases are automatically injectable via @Inject constructor,
 * so they don't need to be provided here.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class HomeModule {

    /**
     * Binds the repository implementation to its interface.
     *
     * This tells Hilt:
     * "When someone requests ProductRepository, give them ProductRepositoryImpl"
     *
     * @Binds is more efficient than @Provides for interface → implementation bindings.
     */
    @Binds
    @Singleton
    abstract fun bindProductRepository(
        impl: ProductRepositoryImpl
    ): ProductRepository
}