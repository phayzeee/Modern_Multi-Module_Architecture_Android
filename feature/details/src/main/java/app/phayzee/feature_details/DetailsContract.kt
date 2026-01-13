package app.phayzee.feature_details

import app.phayzee.feature_home.domain.model.Product


/**
 * MVI Contract for Product Details Screen.
 *
 * Follows the same pattern as HomeContract.
 */
object DetailsContract {

    /**
     * UI State - Represents the entire details screen state.
     */
    data class State(
        val product: Product? = null,
        val isLoading: Boolean = false,
        val isRefreshing: Boolean = false,
        val error: String? = null
    ) {
        /**
         * Helper to check if we should show error state
         */
        val shouldShowError: Boolean
            get() = !isLoading && product == null && error != null

        /**
         * Helper to check if we have product data
         */
        val hasProduct: Boolean
            get() = product != null
    }

    /**
     * User Intentions - All possible user actions on details screen.
     */
    sealed interface Intent {
        /**
         * User clicked back button
         */
        data object BackClicked : Intent

        /**
         * User pulled to refresh
         */
        data object Refresh : Intent

        /**
         * User clicked retry after error
         */
        data object RetryClicked : Intent

        /**
         * User dismissed error
         */
        data object ErrorDismissed : Intent

        /**
         * User clicked share button (future enhancement)
         */
        data object ShareClicked : Intent
    }

    /**
     * Side Effects - One-time events.
     */
    sealed interface Effect {
        /**
         * Navigate back to previous screen
         */
        data object NavigateBack : Effect

        /**
         * Show a toast message
         */
        data class ShowToast(val message: String) : Effect

        /**
         * Share product (future enhancement)
         */
        data class ShareProduct(val product: Product) : Effect
    }
}