package hr.foi.air.baufind.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import hr.foi.air.baufind.ws.model.JobSearchModel

@Composable
fun JobListItem(
    job: JobSearchModel,
    onItemClick: () -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.LightGray)
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Naslov:", modifier = Modifier.weight(0.3f))
                Text(text = job.title, modifier = Modifier.weight(0.7f))
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Opis:", modifier = Modifier.weight(0.3f))
                Text(text = job.description, modifier = Modifier.weight(0.7f))
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Lokacija:", modifier = Modifier.weight(0.3f))
                Text(text = job.location, modifier = Modifier.weight(0.7f))
            }
        }
    }
}