package app.phayzee.core_network.api

import app.phayzee.core_network.model.ProductDto
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Retrofit API interface for product-related endpoints.
 * Uses FakeStore API for demonstration purposes.
 *
 * In production, replace with your actual API endpoints.
 */
interface ProductApi {

    /**
     * Fetches all products from the API
     *
     * @return List of products as DTOs (Data Transfer Objects)
     */
    @GET("products")
    suspend fun getProducts(): List<ProductDto>

    /**
     * Fetches a single product by ID
     *
     * @param id Product identifier
     * @return Product details as DTO
     */
    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Int): ProductDto

    /**
     * Fetches products by category
     *
     * @param category Category name (e.g., "electronics", "jewelery")
     * @return List of products in the specified category
     */
    @GET("products/category/{category}")
    suspend fun getProductsByCategory(@Path("category") category: String): List<ProductDto>

    /**
     * Fetches all available categories
     *
     * @return List of category names
     */
    @GET("products/categories")
    suspend fun getCategories(): List<String>
}