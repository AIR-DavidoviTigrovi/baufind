package hr.foi.air.baufind.example_map

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import hr.foi.air.baufind.core.map.MapProvider
import hr.foi.air.baufind.core.map.models.LocationInformation

class ExampleMapProvider : MapProvider {
    @Composable
    override fun LocationPickerMapScreen(
        modifier: Modifier,
        locationInformation: LocationInformation,
        onLocationChanged: (LocationInformation) -> Unit
    ) {
        var latText by remember { mutableStateOf("0.0") }
        var longText by remember { mutableStateOf("0.0") }

        var latError by remember { mutableStateOf("") }
        var longError by remember { mutableStateOf("") }

        TextField(
            value = latText,
            onValueChange = {
                latText = it
                val lat = latText.toDoubleOrNull()
                if (lat != null) {
                    locationInformation.lat = lat
                    latError = ""
                    locationInformation.isValid = longError == ""
                    if (locationInformation.isValid) {
                        onLocationChanged(locationInformation)
                    }
                } else {
                    locationInformation.lat = 0.0
                    locationInformation.isValid = false
                    latError = "Morate unijeti validan lat"
                }
            },
            label = { Text(text = "Latitude") },
            isError = (latError != ""),
            supportingText = {
                if (latError != "") {
                    Text(latError, color = Color.Red)
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
        TextField(
            value = longText,
            onValueChange = {
                longText = it
                val long = longText.toDoubleOrNull()
                if (long != null) {
                    locationInformation.long = long
                    longError = ""
                    locationInformation.isValid = latError == ""
                    if (locationInformation.isValid) {
                        onLocationChanged(locationInformation)
                    }
                } else {
                    locationInformation.long = 0.0
                    locationInformation.isValid = false
                    longError = "Morate unijeti validan lat"
                }
            },
            label = { Text(text = "Longitude") },
            isError = (longError != ""),
            supportingText = {
                if (longError != "") {
                    Text(longError, color = Color.Red)
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
    }

    @Composable
    override fun LocationShowMapScreen(
        modifier: Modifier,
        locationInformation: LocationInformation
    ) {
        Text(
            text = "Lat: ${locationInformation.lat}",
        )
        Text(
            text = "Lng: ${locationInformation.long}",
        )
    }
}