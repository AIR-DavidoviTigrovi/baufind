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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.utsman.osmandcompose.Marker
import com.utsman.osmandcompose.OpenStreetMap
import com.utsman.osmandcompose.rememberCameraState
import com.utsman.osmandcompose.rememberMarkerState
import hr.foi.air.baufind.core.map.MapProvider
import hr.foi.air.baufind.core.map.models.LocationInformation
import org.osmdroid.util.GeoPoint

class OpenStreetMapProvider : MapProvider {
    @Composable
    override fun MapScreen(modifier: Modifier, locationInformation: LocationInformation) {
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

        val context = LocalContext.current

        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                    locationInformation.isValid = true

                    lat = locationInformation.lat
                    long = locationInformation.long
                    valid = locationInformation.isValid

                    markerState.geoPoint = geoPoint
                },
                onMapClick = {
                    Toast.makeText(context, "Držite dugi klik da biste odabrali lokaciju", Toast.LENGTH_SHORT).show()
                }
            ) {
                Marker(
                    state = markerState,
                    visible = markerValid,
                    infoWindowContent = {
                        Text("Lokacija posla")
                    }
                )
            }
        }
    }
}