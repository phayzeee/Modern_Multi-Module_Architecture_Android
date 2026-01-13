package app.phayzee.feature_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.phayzee.feature_details.domain.usecase.ObserveProductDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import app.phayzee.core_common.result.Result
import javax.inject.Inject

/**
 * ViewModel for Product Details Screen.
 *
 * Features:
 * - Observes product details from cache
 * - Handles pull-to-refresh
 * - Error handling with retry
 * - Navigation handling
 *
 * The productId is obtained from navigation arguments via SavedStateHandle.
 */
@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val observeProductDetailsUseCase: ObserveProductDetailsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Get productId from navigation arguments
    private val productId: Int = checkNotNull(savedStateHandle["productId"]) {
        "productId is required for DetailsViewModel"
    }

    // State
    private val _state = MutableStateFlow(DetailsContract.State())
    val state: StateFlow<DetailsContract.State> = _state.asStateFlow()

    // Effects
    private val _effect = Channel<DetailsContract.Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        observeProductDetails()
    }

    /**
     * Main entry point for all user actions.
     */
    fun handleIntent(intent: DetailsContract.Intent) {
        when (intent) {
            is DetailsContract.Intent.BackClicked -> navigateBack()
            is DetailsContract.Intent.Refresh -> refreshProductDetails()
            is DetailsContract.Intent.RetryClicked -> retryAfterError()
            is DetailsContract.Intent.ErrorDismissed -> dismissError()
            is DetailsContract.Intent.ShareClicked -> shareProduct()
        }
    }

    /**
     * Observes product details from repository.
     */
    private fun observeProductDetails() {
        viewModelScope.launch {
            observeProductDetailsUseCase(productId).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _state.update { it.copy(isLoading = true, error = null) }
                    }
                    is Result.Success -> {
                        _state.update {
                            it.copy(
                                product = result.data,
                                isLoading = false,
                                isRefreshing = false,
                                error = null
                            )
                        }
                    }
                    is Result.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                isRefreshing = false,
                                error = result.message
                            )
                        }
                    }
                }
            }
        }
    }

    /**
     * Refreshes product details from API.
     * Note: In this simple implementation, we rely on the home feature
     * to fetch data. In a real app, you might have a separate API call here.
     */
    private fun refreshProductDetails() {
        _state.update { it.copy(isRefreshing = true, error = null) }

        // For now, just simulate refresh
        // In production, call repository.refreshProductDetails(productId)
        viewModelScope.launch {
            // Simulate network delay
            kotlinx.coroutines.delay(500)
            _state.update { it.copy(isRefreshing = false) }
            sendEffect(DetailsContract.Effect.ShowToast("Refresh complete"))
        }
    }

    /**
     * Navigates back to previous screen
     */
    private fun navigateBack() {
        sendEffect(DetailsContract.Effect.NavigateBack)
    }

    /**
     * Retries loading after an error
     */
    private fun retryAfterError() {
        _state.update { it.copy(error = null, isLoading = true) }
        // observeProductDetails is already running, just clear error
    }

    /**
     * Clears error message
     */
    private fun dismissError() {
        _state.update { it.copy(error = null) }
    }

    /**
     * Shares the product (future enhancement)
     */
    private fun shareProduct() {
        state.value.product?.let { product ->
            sendEffect(DetailsContract.Effect.ShareProduct(product))
        }
    }

    /**
     * Helper to send side effects
     */
    private fun sendEffect(effect: DetailsContract.Effect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}