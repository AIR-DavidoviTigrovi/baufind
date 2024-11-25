package hr.foi.air.baufind.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// Dark Theme Color Scheme
private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,                // Main color for dark mode
    onPrimary = DarkOnPrimary,            // Text on primary color
    background = DarkBackground,          // Background color
    surface = DarkSurface,                // Surface color for cards, dialogs, etc.
    onBackground = DarkTextPrimary,       // Main text on the background
    onSurface = DarkTextPrimary,          // Main text on surfaces
    secondary = DarkSecondary,            // Accent color
    onSecondary = DarkOnSecondary,        // Text on secondary color
)

// Light Theme Color Scheme
private val LightColorScheme = lightColorScheme(
    primary = LightPrimary,               // Main color for light mode
    onPrimary = LightOnPrimary,           // Text on primary color
    background = LightBackground,         // Background color
    surface = LightSurface,               // Surface color for cards, dialogs, etc.
    onBackground = LightTextPrimary,      // Main text on the background
    onSurface = LightTextPrimary,         // Main text on surfaces
    secondary = LightSecondary,           // Accent color
    onSecondary = LightOnSecondary,       // Text on secondary color
)

@Composable
fun BaufindTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
