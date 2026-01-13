package app.phayzee.feature_details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.phayzee.core_ui.components.ErrorState
import app.phayzee.core_ui.components.LoadingIndicator
import app.phayzee.feature_home.domain.model.Product
import coil.compose.AsyncImage

/**
 * Product Details Screen.
 *
 * MVI Architecture:
 * - Observes State from ViewModel
 * - Sends Intents on user actions
 * - Reacts to Effects (navigation, toasts)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    onNavigateBack: () -> Unit,
    viewModel: DetailsViewModel = hiltViewModel()
) {
    // Collect state
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Collect effects
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is DetailsContract.Effect.NavigateBack -> {
                    onNavigateBack()
                }
                is DetailsContract.Effect.ShowToast -> {
                    // In production, show a Snackbar
                }
                is DetailsContract.Effect.ShareProduct -> {
                    // In production, share the product
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Product Details") },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.handleIntent(DetailsContract.Intent.BackClicked) }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    if (state.hasProduct) {
                        IconButton(
                            onClick = { viewModel.handleIntent(DetailsContract.Intent.ShareClicked) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share"
                            )
                        }
                    }
                },
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
                // Show loading on initial load
                state.isLoading && state.product == null -> {
                    LoadingIndicator(message = "Loading product...")
                }

                // Show error if present
                state.shouldShowError -> {
                    ErrorState(
                        message = state.error ?: "Unknown error occurred",
                        onRetry = { viewModel.handleIntent(DetailsContract.Intent.RetryClicked) }
                    )
                }

                // Show product details
                state.hasProduct -> {
                    ProductDetails(
                        product = state.product!!,
                        isRefreshing = state.isRefreshing,
                        onRefresh = { viewModel.handleIntent(DetailsContract.Intent.Refresh) }
                    )
                }
            }
        }
    }
}

/**
 * Product details content (scrollable)
 */
@Composable
private fun ProductDetails(
    product: Product,
    isRefreshing: Boolean,
    onRefresh: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Refresh indicator at top if refreshing
        if (isRefreshing) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            }
        }
        // Product Image
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }

        // Product Title
        Text(
            text = product.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        // Category and Rating Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Category chip
            Surface(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = product.category.uppercase(),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }

            // Rating
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Rating",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = product.rating.formattedRating,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Price
        Text(
            text = product.formattedPrice,
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )

        // Divider
        HorizontalDivider()

        // Description
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Description",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = product.description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Add to cart button (placeholder for future)
        Button(
            onClick = { /* Future: Add to cart */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(
                text = "Add to Cart",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}