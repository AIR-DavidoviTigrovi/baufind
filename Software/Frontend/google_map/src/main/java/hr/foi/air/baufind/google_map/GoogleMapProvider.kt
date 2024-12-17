package hr.foi.air.baufind.google_map

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(
                LatLng(locationInformation.lat, locationInformation.long),
                15.0f
            )
        }

        var locationText by remember { mutableStateOf("") }
        var lat by remember { mutableDoubleStateOf(locationInformation.lat) }
        var long by remember { mutableDoubleStateOf(locationInformation.long) }
        var valid by remember { mutableStateOf(locationInformation.isValid) }

        var locationError by remember { mutableStateOf("") }
        var markerValid by remember { mutableStateOf(false) }

        TextField(
            value = locationText,
            onValueChange = {
                locationText = it
                locationInformation.location = locationText
                if (locationText != "") {
                    locationError = ""
                    locationInformation.isValid = markerValid
                } else {
                    locationError = "Morate unijeti lokaciju"
                    locationInformation.isValid = true
                }
            },
            label = { Text(text = "Lokacija") },
            isError = (locationError != ""),
            supportingText = {
                if (locationError != "") {
                    Text(locationError, color = Color.Red)
                }
            },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                errorContainerColor = MaterialTheme.colorScheme.errorContainer
            ),
            modifier = modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Držite dugi klik na karti da biste odabrali lokaciju.",
            fontSize = 16.sp,
        )
        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(4f/3f),
            onMapLoaded = { isMapLoaded = true },
            cameraPositionState = cameraPositionState,
            onMapLongClick = { coordinates ->
                markerValid = true

                locationInformation.lat = coordinates.latitude
                locationInformation.long = coordinates.longitude
                locationInformation.isValid = locationError == "" && locationText != ""

                lat = locationInformation.lat
                long = locationInformation.long
                valid = locationInformation.isValid
            }
        ) {
            if (markerValid) {
                Marker(
                    state = rememberMarkerState(position = LatLng(lat, long)),
                    title = "Odabrana lokacija"
                )
            }
        }
    }
}