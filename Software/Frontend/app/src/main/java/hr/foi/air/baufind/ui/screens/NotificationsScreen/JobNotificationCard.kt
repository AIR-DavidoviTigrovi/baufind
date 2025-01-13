@file:Suppress("UNREACHABLE_CODE")

package hr.foi.air.baufind.ui.screens.NotificationsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat.Action
import androidx.navigation.NavController
import hr.foi.air.baufind.ui.components.PrimaryButton
import hr.foi.air.baufind.ws.response.JobNotificationResponse

@Composable
fun JobNotificationCard(
    job: JobNotificationResponse,
    navController: NavController
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            if(job.working_status_id == 3){
                Text("Pozvani ste na ste na posao ${job.title}")
                Spacer(Modifier.height(8.dp))
                Row {
                    Button(
                        onClick = {
                            navController.navigate("jobRoom/${job.id}")
                            //Sad tu rješiti logiku za prihvaćanje posla
                        }
                    ){
                        Text(text = "Prihvati")

                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            //Tu rješiti logiku za odbijanje posla
                        },
                        colors = ButtonColors(
                            contentColor = Color.White,
                            containerColor = Color.Red,
                            disabledContainerColor = Color.Red,
                            disabledContentColor = Color.Red,
                        )
                    ){
                        Text(text = "Odbij")

                    }
                }
            }
            else if (job.working_status_id == 4){
                Text("Prihvaćeni ste na posao ${job.title}")
            }
            else if(job.job_status_id == 3){
                PrimaryButton(
                    text = "Recenziraj posao",
                    onClick = {
                        //logika
                    }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

