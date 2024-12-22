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
import hr.foi.air.baufind.core.map.models.LocationInformation

class GoogleMapProvider : MapProvider {
    @Composable
    override fun LocationPickerMapScreen(
        modifier: Modifier,
        locationInformation: LocationInformation,
        onLocationChanged: (LocationInformation) -> Unit
    ) {
        var isMapLoaded by remember { mutableStateOf(false) }
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(
                LatLng(locationInformation.lat, locationInformation.long),
                15.0f
            )
        }

        var lat by remember { mutableDoubleStateOf(locationInformation.lat) }
        var long by remember { mutableDoubleStateOf(locationInformation.long) }
        var valid by remember { mutableStateOf(false) }

        val context = LocalContext.current

        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(4f/3f),
            onMapLoaded = { isMapLoaded = true },
            cameraPositionState = cameraPositionState,
            onMapLongClick = { coordinates ->
                locationInformation.lat = coordinates.latitude
                locationInformation.long = coordinates.longitude
                locationInformation.isValid = true

                lat = locationInformation.lat
                long = locationInformation.long
                valid = true

                onLocationChanged(locationInformation)
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
        locationInformation: LocationInformation
    ) {
        var isMapLoaded by remember { mutableStateOf(false) }
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(
                LatLng(locationInformation.lat, locationInformation.long),
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
                state = rememberMarkerState(position = LatLng(locationInformation.lat, locationInformation.long)),
                title = locationInformation.location
            )
        }
    }
}