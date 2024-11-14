package hr.foi.air.baufind.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFF0000),
    secondary = Color(0xFFFFEB3B),
    tertiary = Color(0xFF2196F3),
    background = Color(0xFF180C16),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color(0xFFFFFBFE),
    onSecondary = Color(0xFFFFFBFE),
    onTertiary = Color(0xFFFFFBFE),
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF2482E5),
    secondary = Color(0xFF7EBDFF),
    tertiary = Color(0xFF00519B),
    background = Color(0xFFFAFAFA),
    primaryContainer = Color(0xFFE8E8E8),
    secondaryContainer = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFE3E3),
    onSecondary = Color(0xFF3F51B5),
    onTertiary = Color(0xFF00BCD4),
    onBackground = Color(0xFF1C1B1F),
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