package hr.foi.air.baufind.ui.screens.NotificationsScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import hr.foi.air.baufind.ws.response.ReviewNotificationResponse

@Composable
fun ReviewNotificationCard(
    notification: ReviewNotificationResponse,
    navController: NavController
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = "Posao: ${notification.jobTitle}",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (notification.position == "employer") {
                Text(text = "Recenzirate poslodavca: ${notification.personName}")
            } else {
                Text(text = "Recenzirate radnika: ${notification.personName} ")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (notification.position == "employer") {
                        navController.navigate("employerReviewScreen/${notification.jobId}")
                    } else {
                        navController.navigate("workerReviewScreen/${notification.personId}/${notification.workingId}")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Recenziraj")
            }
        }
    }
}