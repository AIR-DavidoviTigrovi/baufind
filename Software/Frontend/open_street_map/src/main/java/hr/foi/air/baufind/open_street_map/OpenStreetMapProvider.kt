package hr.foi.air.baufind.open_street_map

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.utsman.osmandcompose.OpenStreetMap
import com.utsman.osmandcompose.rememberCameraState
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

        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OpenStreetMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(4f/3f)
                    .clip(AbsoluteCutCornerShape(0.dp)),
                cameraState = cameraState
            )
        }
    }
}