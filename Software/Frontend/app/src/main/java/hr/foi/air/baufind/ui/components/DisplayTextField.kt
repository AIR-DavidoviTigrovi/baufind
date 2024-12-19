package hr.foi.air.baufind.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DisplayTextField(
    title : String,
    text : String,
    modifier: Modifier = Modifier
){
    Column(modifier = modifier){
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        TextField(
            value = text,
            onValueChange = {},
            readOnly = true,
            enabled = false,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                disabledTextColor = MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier
                .padding(top = 4.dp)
                .fillMaxWidth()
        )
    }
}