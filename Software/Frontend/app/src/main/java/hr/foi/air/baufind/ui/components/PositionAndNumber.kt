package hr.foi.air.baufind.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun PositionAndNumber(
    text: String,
    count: Int,
    onCountChange: (Int) -> Unit
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = text)

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { onCountChange(count -1) }) {
                Icon(imageVector = Icons.Filled.Clear, contentDescription = "Minus")
            }
            Text(text = count.toString())
            IconButton(onClick = { onCountChange(count + 1) }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Plus")
            }
        }
    }
}