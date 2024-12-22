package hr.foi.air.baufind.core.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import hr.foi.air.baufind.core.map.models.LocationInformation

interface MapProvider {
    @Composable
    fun LocationPickerMapScreen(
        modifier: Modifier,
        locationInformation: LocationInformation,
        onLocationChanged: (LocationInformation) -> Unit
    )

    @Composable
    fun LocationShowMapScreen(
        modifier: Modifier,
        locationInformation: LocationInformation
    )
}