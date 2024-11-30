package hr.foi.air.baufind.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hr.foi.air.baufind.ws.model.Worker


@Composable
fun WorkerCard(
    worker: Worker?,
    onItemClick: () -> Unit
) {
    // Example UI for displaying worker's name and position
    Card(
        modifier = Modifier.fillMaxWidth().padding(4.dp),
        onClick = onItemClick
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ){
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Ime: ${worker?.name}", fontWeight = FontWeight.Bold)
                Text(text = "Pozicija: ${worker?.skills}")
                Text(text = "Lokacija: ${worker?.address}")
                Text(text = "Broj poslova: ${worker?.numOfJobs}")
            }
            Column(modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "${worker?.avgRating}", fontWeight = FontWeight.Bold, fontSize = 24.sp)

            }
        }

    }

}