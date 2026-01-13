package app.phayzee.feature_details.di

import app.phayzee.feature_details.data.repository.ProductDetailsRepositoryImpl
import app.phayzee.feature_details.domain.repository.ProductDetailsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for Details feature.
 *
 * Binds the repository implementation to its interface.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class DetailsModule {

    /**
     * Binds ProductDetailsRepository implementation.
     */
    @Binds
    @Singleton
    abstract fun bindProductDetailsRepository(
        impl: ProductDetailsRepositoryImpl
    ): ProductDetailsRepository
}