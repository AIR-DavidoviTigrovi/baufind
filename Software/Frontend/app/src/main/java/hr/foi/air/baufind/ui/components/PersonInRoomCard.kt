package hr.foi.air.baufind.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PersonInRoomCard(workerName: String,onItemClick: () -> Unit){
    Card(
        modifier = Modifier.fillMaxWidth().padding(4.dp),
        onClick = onItemClick,
    ){
        Text(text = workerName, modifier = Modifier.padding(16.dp))
    }
}