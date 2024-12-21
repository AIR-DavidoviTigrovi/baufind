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
        locationInformation: LocationInformation
    ) {
        var locationText by remember { mutableStateOf("") }
        var latText by remember { mutableStateOf("0.0") }
        var longText by remember { mutableStateOf("0.0") }

        var locationError by remember { mutableStateOf("") }
        var latError by remember { mutableStateOf("") }
        var longError by remember { mutableStateOf("") }

        TextField(
            value = locationText,
            onValueChange = {
                locationText = it
                locationInformation.location = locationText
                if (locationText != "") {
                    locationError = ""
                    locationInformation.isValid = (latError == "" && longError == "")
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
        TextField(
            value = latText,
            onValueChange = {
                latText = it
                val lat = latText.toDoubleOrNull()
                if (lat != null) {
                    locationInformation.lat = lat
                    latError = ""
                    locationInformation.isValid = (longError == "" && locationError == "" && locationText != "")
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
                    locationInformation.isValid = (latError == "" && locationError == "" && locationText != "")
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
        Text("TODO")
    }
}