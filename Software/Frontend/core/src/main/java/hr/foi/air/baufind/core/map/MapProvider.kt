package hr.foi.air.baufind.core.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

interface MapProvider {
    @Composable
    fun MapScreen(
        modifier: Modifier = Modifier
    )
}