package hr.foi.air.baufind.ui.screens.Settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hr.foi.air.baufind.helpers.MapHelper

@Composable
fun MapProviderSelection() {
    var selectedProvider by remember { mutableStateOf(MapHelper.mapProvider) }

    Column {
        MapHelper.mapProviders.forEach { provider ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                RadioButton(
                    selected = provider == selectedProvider,
                    onClick = {
                        selectedProvider = provider
                        MapHelper.mapProvider = provider
                    }
                )
                Text(
                    text = provider::class.java.simpleName,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }
    }
}
