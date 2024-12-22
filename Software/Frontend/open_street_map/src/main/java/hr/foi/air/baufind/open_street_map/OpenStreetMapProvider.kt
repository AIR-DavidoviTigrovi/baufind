package hr.foi.air.baufind.open_street_map

import android.widget.Toast
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.utsman.osmandcompose.Marker
import com.utsman.osmandcompose.OpenStreetMap
import com.utsman.osmandcompose.rememberCameraState
import com.utsman.osmandcompose.rememberMarkerState
import hr.foi.air.baufind.core.map.MapProvider
import hr.foi.air.baufind.core.map.models.Coordinates
import org.osmdroid.util.GeoPoint

class OpenStreetMapProvider : MapProvider {
    @Composable
    override fun LocationPickerMapScreen(
        modifier: Modifier,
        coordinates: Coordinates,
        onCoordinatesChanged: (Coordinates) -> Unit
    ) {
        val cameraState = rememberCameraState {
            geoPoint = GeoPoint(coordinates.lat, coordinates.long)
            zoom = 15.0
        }

        val markerState = rememberMarkerState(
            geoPoint = GeoPoint(coordinates.lat, coordinates.long)
        )

        var lat by remember { mutableDoubleStateOf(coordinates.lat) }
        var long by remember { mutableDoubleStateOf(coordinates.long) }
        var valid by remember { mutableStateOf(coordinates.isValid) }

        val context = LocalContext.current

        OpenStreetMap(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f/9f)
                .clip(AbsoluteCutCornerShape(0.dp)),
            cameraState = cameraState,
            onMapLongClick = { geoPoint ->
                coordinates.lat = geoPoint.latitude
                coordinates.long = geoPoint.longitude
                coordinates.isValid = true

                lat = coordinates.lat
                long = coordinates.long
                valid = true

                markerState.geoPoint = geoPoint
                onCoordinatesChanged(coordinates)
            },
            onMapClick = {
                Toast.makeText(context, "Držite dugi klik da biste odabrali lokaciju", Toast.LENGTH_SHORT).show()
            }
        ) {
            Marker(
                state = markerState,
                visible = valid,
                infoWindowContent = {
                    Text("Odabrana lokacija")
                }
            )
        }
    }

    @Composable
    override fun LocationShowMapScreen(
        modifier: Modifier,
        coordinates: Coordinates,
        location: String
    ) {
        val cameraState = rememberCameraState {
            geoPoint = GeoPoint(coordinates.lat, coordinates.long)
            zoom = 15.0
        }

        OpenStreetMap(
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(4f/3f)
                .clip(AbsoluteCutCornerShape(0.dp)),
            cameraState = cameraState
        ) {
            Marker(
                state = rememberMarkerState(
                    geoPoint = GeoPoint(coordinates.lat, coordinates.long)
                ),
                infoWindowContent = {
                    Text(location)
                }
            )
        }
    }
}