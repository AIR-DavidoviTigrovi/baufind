package hr.foi.air.baufind.example_map

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import hr.foi.air.baufind.core.map.MapProvider

class ExampleMapProvider : MapProvider {
    @Composable
    override fun MapScreen(
        modifier: Modifier
    ) {
        Text(
            text = "Ja radim!"
        )
    }
}