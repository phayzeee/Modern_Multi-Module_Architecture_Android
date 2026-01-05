import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Convention plugin for feature modules.
 * Extends AndroidLibraryConventionPlugin with feature-specific setup.
 */
class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                // Apply base library convention first
                apply("modernarch.android.library")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("com.google.devtools.ksp")
                apply("com.google.dagger.hilt.android")
            }

            // Enable Compose
            extensions.configure<com.android.build.gradle.LibraryExtension>("android") {
                buildFeatures {
                    compose = true
                }
            }

            // Feature modules get these dependencies by default
            dependencies {
                // Core dependencies
                add("implementation", project(":core:common"))
                add("implementation", project(":core:ui"))
                add("implementation", project(":core:network"))
                add("implementation", project(":core:database"))

                // Hilt
                add("implementation", "com.google.dagger:hilt-android:2.54")
                add("ksp", "com.google.dagger:hilt-compiler:2.54")

                // Compose
                val composeBom = platform("androidx.compose:compose-bom:2024.09.00")
                add("implementation", composeBom)
                add("implementation", "androidx.compose.ui:ui")
                add("implementation", "androidx.compose.material3:material3")
                add("implementation", "androidx.lifecycle:lifecycle-viewmodel-compose:2.10.0")
                add("implementation", "androidx.lifecycle:lifecycle-runtime-compose:2.10.0")
                add("implementation", "androidx.hilt:hilt-navigation-compose:1.2.0")

                // Coroutines
                add("implementation", "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")

                // Testing
                add("testImplementation", "io.mockk:mockk:1.13.14")
                add("testImplementation", "app.cash.turbine:turbine:1.2.0")
                add("testImplementation", "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.9.0")
            }
        }
    }
}