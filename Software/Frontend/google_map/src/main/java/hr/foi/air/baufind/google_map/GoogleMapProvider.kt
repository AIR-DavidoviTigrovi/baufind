package hr.foi.air.baufind.google_map

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import hr.foi.air.baufind.core.map.MapProvider
import hr.foi.air.baufind.core.map.models.LocationInformation

class GoogleMapProvider : MapProvider {
    @Composable
    override fun MapScreen(modifier: Modifier, locationInformation: LocationInformation) {
        var isMapLoaded by remember { mutableStateOf(false) }
        val latLng = LatLng(45.33295293903444, 17.702489909850566)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(
                latLng,
                10.0f
            )
        }

        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            onMapLoaded = { isMapLoaded = true },
            cameraPositionState = cameraPositionState
        ) {
            Marker(
                state = rememberMarkerState(position = latLng),
                title = "Stara kuća"
            )
        }
    }
}