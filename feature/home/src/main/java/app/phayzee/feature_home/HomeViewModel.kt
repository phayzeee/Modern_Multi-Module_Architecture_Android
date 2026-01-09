package app.phayzee.feature_home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.phayzee.feature_home.domain.usecase.ObserveProductsUseCase
import app.phayzee.feature_home.domain.usecase.RefreshProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import app.phayzee.core_common.result.Result

/**
 * ViewModel for Home Screen following MVI pattern.
 *
 * Responsibilities:
 * - Handle user intents
 * - Update UI state
 * - Execute use cases
 * - Trigger side effects
 *
 * Does NOT:
 * - Contain UI logic
 * - Know about Android framework (except lifecycle)
 * - Directly access repositories or data sources
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val observeProductsUseCase: ObserveProductsUseCase,
    private val refreshProductsUseCase: RefreshProductsUseCase
) : ViewModel() {

    // State: Mutable internally, immutable externally
    private val _state = MutableStateFlow(HomeContract.State())
    val state: StateFlow<HomeContract.State> = _state.asStateFlow()

    // Effects: One-time events using Channel
    private val _effect = Channel<HomeContract.Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        // Start observing products immediately
        observeProducts()
        // Trigger initial refresh to fetch data
        handleIntent(HomeContract.Intent.Refresh)
    }

    /**
     * Main entry point for all user actions.
     * Routes intents to appropriate handlers.
     */
    fun handleIntent(intent: HomeContract.Intent) {
        when (intent) {
            is HomeContract.Intent.Refresh -> refreshProducts()
            is HomeContract.Intent.ProductClicked -> navigateToDetails(intent.productId)
            is HomeContract.Intent.SearchQueryChanged -> updateSearchQuery(intent.query)
            is HomeContract.Intent.ErrorDismissed -> dismissError()
            is HomeContract.Intent.RetryClicked -> retryAfterError()
        }
    }

    /**
     * Observes products from repository (via use case).
     *
     * This Flow automatically updates when:
     * - Data is fetched from API
     * - Database changes
     * - Cache is updated
     *
     * The UI just reacts to state changes.
     */
    private fun observeProducts() {
        viewModelScope.launch {
            observeProductsUseCase(Unit).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _state.update { it.copy(isLoading = true, error = null) }
                    }
                    is Result.Success -> {
                        _state.update {
                            it.copy(
                                products = result.data,
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
     * Refreshes products from API.
     *
     * Sets isRefreshing flag for pull-to-refresh UI.
     * Doesn't set isLoading to avoid replacing content with loading spinner.
     */
    private fun refreshProducts() {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true, error = null) }

            when (val result = refreshProductsUseCase(true)) {
                is Result.Success -> {
                    // Products are automatically updated via observeProducts()
                    // Just clear the refreshing flag
                    _state.update { it.copy(isRefreshing = false) }
                }
                is Result.Error -> {
                    _state.update {
                        it.copy(
                            isRefreshing = false,
                            error = result.message
                        )
                    }
                    sendEffect(HomeContract.Effect.ShowToast("Failed to refresh products"))
                }
                is Result.Loading -> {
                    // Should not happen in UseCase, but handle it
                }
            }
        }
    }

    /**
     * Navigates to product details.
     * This is a side effect, not a state change.
     */
    private fun navigateToDetails(productId: Int) {
        sendEffect(HomeContract.Effect.NavigateToDetails(productId))
    }

    /**
     * Updates search query in state.
     * filteredProducts is automatically recomputed.
     */
    private fun updateSearchQuery(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }

    /**
     * Clears error message from state
     */
    private fun dismissError() {
        _state.update { it.copy(error = null) }
    }

    /**
     * Retries after an error by refreshing products
     */
    private fun retryAfterError() {
        _state.update { it.copy(error = null) }
        refreshProducts()
    }

    /**
     * Helper to send side effects
     */
    private fun sendEffect(effect: HomeContract.Effect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}