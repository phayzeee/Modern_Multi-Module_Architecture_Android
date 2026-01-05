package app.phayzee.core_network.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Data Transfer Object (DTO) for Product API responses.
 * Maps directly to API JSON structure.
 *
 * DTOs should only exist in the data layer and be mapped to domain models
 * before being exposed to the presentation layer.
 */
@JsonClass(generateAdapter = true)
data class ProductDto(
    @Json(name = "id")
    val id: Int,

    @Json(name = "title")
    val title: String,

    @Json(name = "price")
    val price: Double,

    @Json(name = "description")
    val description: String,

    @Json(name = "category")
    val category: String,

    @Json(name = "image")
    val image: String,

    @Json(name = "rating")
    val rating: RatingDto
)

/**
 * Rating information for a product
 */
@JsonClass(generateAdapter = true)
data class RatingDto(
    @Json(name = "rate")
    val rate: Double,

    @Json(name = "count")
    val count: Int
)