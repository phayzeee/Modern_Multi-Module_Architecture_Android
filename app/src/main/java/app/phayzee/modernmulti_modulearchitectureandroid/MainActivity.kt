package app.phayzee.modernmulti_modulearchitectureandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import app.phayzee.core_ui.theme.ModernArchTheme
import app.phayzee.modernmulti_modulearchitectureandroid.navigation.AppNavigation
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main (and only) Activity for the app.
 *
 * This is a single-Activity architecture using Jetpack Compose Navigation.
 * All screens are Composables, not separate Activities.
 *
 * Benefits:
 * - Shared ViewModels across screens
 * - Smooth transitions
 * - Better state management
 * - Easier to test
 *
 * @AndroidEntryPoint enables Hilt dependency injection in this Activity.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge display (goes under status bar)
        enableEdgeToEdge()

        setContent {
            // Apply app theme
            ModernArchTheme {
                // Surface provides background color
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Start navigation
                    AppNavigation()
                }
            }
        }
    }
}