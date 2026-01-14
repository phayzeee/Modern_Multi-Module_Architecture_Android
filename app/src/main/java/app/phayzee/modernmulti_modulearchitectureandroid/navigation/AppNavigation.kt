package app.phayzee.modernmulti_modulearchitectureandroid.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import app.phayzee.feature_details.DetailsScreen
import app.phayzee.feature_home.HomeScreen

/**
 * Navigation routes for the app.
 *
 * Using sealed class for type-safety and exhaustive when expressions.
 */
sealed class Screen(val route: String) {
    /**
     * Home screen - Product list
     */
    data object Home : Screen("home")

    /**
     * Details screen - Product details
     * Requires productId as argument
     */
    data object Details : Screen("details/{productId}") {
        /**
         * Creates the route with actual productId
         */
        fun createRoute(productId: Int): String {
            return "details/$productId"
        }

        /**
         * Route pattern for navigation graph
         */
        const val ROUTE_PATTERN = "details/{productId}"

        /**
         * Argument name
         */
        const val ARG_PRODUCT_ID = "productId"
    }
}

/**
 * Main navigation graph for the app.
 *
 * Sets up navigation between:
 * - Home screen (product list)
 * - Details screen (product details)
 *
 * Navigation is handled via NavController, which manages the back stack
 * and handles configuration changes correctly.
 */
@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        // Home Screen
        composable(route = Screen.Home.route) {
            HomeScreen(
                onNavigateToDetails = { productId ->
                    navController.navigate(Screen.Details.createRoute(productId))
                }
            )
        }

        // Details Screen
        composable(
            route = Screen.Details.ROUTE_PATTERN,
            arguments = listOf(
                navArgument(Screen.Details.ARG_PRODUCT_ID) {
                    type = NavType.IntType
                }
            )
        ) {
            DetailsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}