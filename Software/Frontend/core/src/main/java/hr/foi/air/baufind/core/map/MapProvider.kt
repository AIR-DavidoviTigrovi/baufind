package hr.foi.air.baufind.core.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import hr.foi.air.baufind.core.map.models.Coordinates

interface MapProvider {
    @Composable
    fun LocationPickerMapScreen(
        modifier: Modifier,
        coordinates: Coordinates,
        onCoordinatesChanged: (Coordinates) -> Unit
    )

    @Composable
    fun LocationShowMapScreen(
        modifier: Modifier,
        coordinates: Coordinates,
        location: String
    )
}