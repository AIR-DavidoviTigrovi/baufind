package hr.foi.air.baufind.open_street_map

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.utsman.osmandcompose.Marker
import com.utsman.osmandcompose.OpenStreetMap
import com.utsman.osmandcompose.rememberCameraState
import com.utsman.osmandcompose.rememberMarkerState
import hr.foi.air.baufind.core.map.MapProvider
import hr.foi.air.baufind.core.map.models.LocationInformation
import hr.foi.air.baufind.ws.network.NetworkService
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint

class OpenStreetMapProvider : MapProvider {
    @Composable
    override fun LocationPickerMapScreen(modifier: Modifier, locationInformation: LocationInformation) {
        val geocodingService = NetworkService.createGeocodingService()

        val cameraState = rememberCameraState {
            geoPoint = GeoPoint(locationInformation.lat, locationInformation.long)
            zoom = 15.0
        }

        val markerState = rememberMarkerState(
            geoPoint = GeoPoint(locationInformation.lat, locationInformation.long)
        )

        var lat by remember { mutableDoubleStateOf(locationInformation.lat) }
        var long by remember { mutableDoubleStateOf(locationInformation.long) }
        var valid by remember { mutableStateOf(locationInformation.isValid) }

        var markerValid by remember { mutableStateOf(false) }
        var locationText by remember { mutableStateOf("") }

        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()

        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Lokacija: $locationText",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
            OpenStreetMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(4f/3f)
                    .clip(AbsoluteCutCornerShape(0.dp)),
                cameraState = cameraState,
                onMapLongClick = { geoPoint ->
                    markerValid = true

                    locationInformation.lat = geoPoint.latitude
                    locationInformation.long = geoPoint.longitude

                    lat = locationInformation.lat
                    long = locationInformation.long

                    markerState.geoPoint = geoPoint
                    coroutineScope.launch {
                        locationInformation.location = ""
                        locationText = "Učitavam lokaciju..."
                        locationInformation.isValid = false
                        valid = false
                        try {
                            val geocodingResponse = geocodingService.reverseGeocode(lat, long)
                            locationInformation.location = geocodingResponse.location ?: "Nepoznata lokacija"
                            locationText = locationInformation.location
                            locationInformation.isValid = true
                            valid = true
                        } catch (e: Exception) {
                            locationInformation.location = ""
                            locationText = "Greška pri dohvaćanju lokacije"
                        }
                    }
                },
                onMapClick = {
                    Toast.makeText(context, "Držite dugi klik da biste odabrali lokaciju", Toast.LENGTH_SHORT).show()
                }
            ) {
                Marker(
                    state = markerState,
                    visible = markerValid,
                    infoWindowContent = {
                        Text(locationText)
                    }
                )
            }
        }
    }

    @Composable
    override fun LocationShowMapScreen(
        modifier: Modifier,
        locationInformation: LocationInformation
    ) {
        Text("TODO")
    }
}