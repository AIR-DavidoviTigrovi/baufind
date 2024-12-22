package hr.foi.air.baufind.google_map

import android.widget.Toast
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import hr.foi.air.baufind.core.map.MapProvider
import hr.foi.air.baufind.core.map.models.Coordinates

class GoogleMapProvider : MapProvider {
    @Composable
    override fun LocationPickerMapScreen(
        modifier: Modifier,
        coordinates: Coordinates,
        onCoordinatesChanged: (Coordinates) -> Unit
    ) {
        var isMapLoaded by remember { mutableStateOf(false) }
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(
                LatLng(coordinates.lat, coordinates.long),
                15.0f
            )
        }

        var lat by remember { mutableDoubleStateOf(coordinates.lat) }
        var long by remember { mutableDoubleStateOf(coordinates.long) }
        var valid by remember { mutableStateOf(false) }

        val context = LocalContext.current

        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f/9f),
            onMapLoaded = { isMapLoaded = true },
            cameraPositionState = cameraPositionState,
            onMapLongClick = { coord ->
                coordinates.lat = coord.latitude
                coordinates.long = coord.longitude
                coordinates.isValid = true

                lat = coordinates.lat
                long = coordinates.long
                valid = true

                onCoordinatesChanged(coordinates)
            },
            onMapClick = {
                Toast.makeText(context, "Držite dugi klik da biste odabrali lokaciju", Toast.LENGTH_SHORT).show()
            }
        ) {
            if (valid) {
                Marker(
                    state = rememberMarkerState(position = LatLng(lat, long)),
                    title = "Odabrana lokacija"
                )
            }
        }
    }

    @Composable
    override fun LocationShowMapScreen(
        modifier: Modifier,
        coordinates: Coordinates,
        location: String
    ) {
        var isMapLoaded by remember { mutableStateOf(false) }
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(
                LatLng(coordinates.lat, coordinates.long),
                15.0f
            )
        }

        GoogleMap(
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(4f/3f),
            onMapLoaded = { isMapLoaded = true },
            cameraPositionState = cameraPositionState
        ) {
            Marker(
                state = rememberMarkerState(position = LatLng(coordinates.lat, coordinates.long)),
                title = location
            )
        }
    }
}