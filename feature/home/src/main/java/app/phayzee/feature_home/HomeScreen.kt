package app.phayzee.feature_home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.phayzee.core_ui.components.EmptyState
import app.phayzee.core_ui.components.ErrorState
import app.phayzee.core_ui.components.LoadingIndicator
import app.phayzee.feature_home.domain.model.Product
import coil.compose.AsyncImage
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

/**
 * Home Screen - Product List
 *
 * MVI Architecture:
 * - Observes State from ViewModel
 * - Sends Intents on user actions
 * - Reacts to Effects (navigation, toasts)
 *
 * This composable is DUMB - it only renders UI based on state.
 * All logic lives in the ViewModel.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToDetails: (Int) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    // Collect state as lifecycle-aware state
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Collect effects
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is HomeContract.Effect.NavigateToDetails -> {
                    onNavigateToDetails(effect.productId)
                }
                is HomeContract.Effect.ShowToast -> {
                    // In production, show a Snackbar or Toast
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Products") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                // Show loading on initial load (empty products)
                state.isLoading && state.products.isEmpty() -> {
                    LoadingIndicator(message = "Loading products...")
                }

                // Show error if present
                state.shouldShowError -> {
                    ErrorState(
                        message = state.error ?: "Unknown error occurred",
                        onRetry = { viewModel.handleIntent(HomeContract.Intent.RetryClicked) }
                    )
                }

                // Show empty state if no products
                state.shouldShowEmptyState -> {
                    EmptyState(
                        message = "No products available",
                        icon = "🛒",
                        actionText = "Retry",
                        onAction = { viewModel.handleIntent(HomeContract.Intent.Refresh) }
                    )
                }

                // Show product list
                else -> {
                    ProductList(
                        products = state.filteredProducts,
                        searchQuery = state.searchQuery,
                        isRefreshing = state.isRefreshing,
                        onProductClick = { productId ->
                            viewModel.handleIntent(HomeContract.Intent.ProductClicked(productId))
                        },
                        onRefresh = {
                            viewModel.handleIntent(HomeContract.Intent.Refresh)
                        },
                        onSearchQueryChange = { query ->
                            viewModel.handleIntent(HomeContract.Intent.SearchQueryChanged(query))
                        }
                    )
                }
            }
        }
    }
}

/**
 * Product list with pull-to-refresh and search
 */
@Composable
private fun ProductList(
    products: List<Product>,
    searchQuery: String,
    isRefreshing: Boolean,
    onProductClick: (Int) -> Unit,
    onRefresh: () -> Unit,
    onSearchQueryChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Search bar
        SearchBar(
            query = searchQuery,
            onQueryChange = onSearchQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // Swipe-to-refresh list using Accompanist
        val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)

        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = onRefresh,
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = products,
                    key = { it.id }
                ) { product ->
                    ProductCard(
                        product = product,
                        onClick = { onProductClick(product.id) }
                    )
                }
            }
        }
    }
}

/**
 * Search bar composable
 */
@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier,
        placeholder = { Text("Search products...") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search"
            )
        },
        singleLine = true,
        shape = MaterialTheme.shapes.large
    )
}

/**
 * Product card composable
 */
@Composable
private fun ProductCard(
    product: Product,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Product image
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.title,
                modifier = Modifier
                    .size(80.dp)
                    .padding(4.dp),
                contentScale = ContentScale.Fit
            )

            // Product details
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = product.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = product.category,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = product.formattedPrice,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = "⭐ ${product.rating.formattedRating}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}