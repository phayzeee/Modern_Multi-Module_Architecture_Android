package app.phayzee.feature_home.domain.model

data class Product(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val imageUrl: String,
    val rating: Rating
) {
    /**
     * Formatted price for display
     */
    val formattedPrice: String
        get() = "$${"%.2f".format(price)}"

    /**
     * Returns true if product is highly rated (>= 4.0)
     */
    val isHighlyRated: Boolean
        get() = rating.value >= 4.0
}

/**
 * Product rating information
 */
data class Rating(
    val value: Double,
    val count: Int
) {
    /**
     * Formatted rating string for display
     */
    val formattedRating: String
        get() = "${"%.1f".format(value)} ($count)"
}