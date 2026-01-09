package app.phayzee.feature_home

import app.phayzee.feature_home.domain.model.Product

/**
 * MVI Contract for Home Screen.
 *
 * MVI Pattern:
 * User Action (Intent) → ViewModel processes → Updates State → View renders
 *                                            → Triggers Effect (one-time events)
 *
 * Benefits:
 * - Unidirectional data flow (easy to understand)
 * - Single source of truth (State)
 * - Predictable state changes
 * - Easy to test and debug
 * - Time-travel debugging possible
 */
object HomeContract {

    /**
     * UI State - Represents the entire UI at any point in time.
     *
     * The View should ONLY render based on this state.
     * No business logic in the View layer.
     */
    data class State(
        val products: List<Product> = emptyList(),
        val isLoading: Boolean = false,
        val isRefreshing: Boolean = false,
        val error: String? = null,
        val searchQuery: String = ""
    ) {
        /**
         * Filtered products based on search query.
         * Computed property - not stored state.
         */
        val filteredProducts: List<Product>
            get() = if (searchQuery.isBlank()) {
                products
            } else {
                products.filter { product ->
                    product.title.contains(searchQuery, ignoreCase = true) ||
                            product.category.contains(searchQuery, ignoreCase = true)
                }
            }

        /**
         * Helper to check if we should show empty state
         */
        val shouldShowEmptyState: Boolean
            get() = !isLoading && products.isEmpty() && error == null

        /**
         * Helper to check if we should show error state
         */
        val shouldShowError: Boolean
            get() = !isLoading && error != null
    }

    /**
     * User Intentions - All possible user actions.
     *
     * These are events triggered by user interaction.
     * ViewModels handle these and update state accordingly.
     */
    sealed interface Intent {
        /**
         * User pulled to refresh
         */
        data object Refresh : Intent

        /**
         * User clicked on a product
         */
        data class ProductClicked(val productId: Int) : Intent

        /**
         * User typed in search box
         */
        data class SearchQueryChanged(val query: String) : Intent

        /**
         * User cleared the error
         */
        data object ErrorDismissed : Intent

        /**
         * User clicked retry after error
         */
        data object RetryClicked : Intent
    }

    /**
     * Side Effects - One-time events that don't affect state.
     *
     * Examples: Navigation, showing toasts, etc.
     * Effects are consumed once and don't persist in state.
     *
     * Why separate from State?
     * - Configuration changes (rotation) should NOT re-trigger effects
     * - Navigation should happen once, not every recomposition
     */
    sealed interface Effect {
        /**
         * Navigate to product details screen
         */
        data class NavigateToDetails(val productId: Int) : Effect

        /**
         * Show a toast/snackbar message
         */
        data class ShowToast(val message: String) : Effect
    }
}