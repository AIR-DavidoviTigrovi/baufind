package hr.foi.air.baufind.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
            Column(modifier = Modifier.padding(12.dp,8.dp,8.dp,8.dp)) {
                Text(text = "Ime: ${worker?.name}", fontWeight = FontWeight.Bold , overflow = TextOverflow.Ellipsis, maxLines = 1)
                Text(text = "Pozicija: ${worker?.skills}" , overflow = TextOverflow.Ellipsis, maxLines = 1, modifier = Modifier.width(200.dp))
                Text(text = "Lokacija: ${worker?.address}" , overflow = TextOverflow.Ellipsis, maxLines = 1)
                Text(text = "Broj poslova: ${worker?.numOfJobs}" , overflow = TextOverflow.Ellipsis, maxLines = 1)
            }
            Column(modifier = Modifier.padding(0.dp,8.dp,16.dp,8.dp))
             {
                Text(text = "${worker?.avgRating}", fontWeight = FontWeight.Bold, fontSize = 20.sp, overflow = TextOverflow.Ellipsis, maxLines = 1)

            }
        }

    }

}