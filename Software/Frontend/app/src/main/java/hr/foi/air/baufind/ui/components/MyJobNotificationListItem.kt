package hr.foi.air.baufind.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import hr.foi.air.baufind.ws.model.MyJobModel
import hr.foi.air.baufind.ws.response.MyJobNotificationModel

@Composable
fun MyJobNotificationListItem(
    job: MyJobNotificationModel,
    onItemClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.LightGray)
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            .padding(8.dp)
            .clickable { onItemClick() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Id posla: ", modifier = Modifier.weight(0.3f))
                Text(text = job.jobId.toString(), modifier = Modifier.weight(0.7f))

            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Naziv posla: ", modifier = Modifier.weight(0.3f))
                Text(text = job.jobTitle, modifier = Modifier.weight(0.7f))
            }
        }
    }
}