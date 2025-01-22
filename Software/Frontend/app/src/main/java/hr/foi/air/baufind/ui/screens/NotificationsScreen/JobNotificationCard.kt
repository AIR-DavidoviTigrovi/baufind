@file:Suppress("UNREACHABLE_CODE")

package hr.foi.air.baufind.ui.screens.NotificationsScreen

import android.widget.Toast
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import hr.foi.air.baufind.service.WorkerService.WorkerSkillService
import hr.foi.air.baufind.ui.components.PrimaryButton
import hr.foi.air.baufind.ws.network.TokenProvider
import hr.foi.air.baufind.ws.request.ConfirmWorkerRequest
import hr.foi.air.baufind.ws.response.JobNotificationResponse
import kotlinx.coroutines.launch

@Composable
fun JobNotificationCard(
    job: JobNotificationResponse,
    navController: NavController,
    tokenProvider: TokenProvider
) {
    var corutine = rememberCoroutineScope()
    val context = LocalContext.current
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
                            corutine.launch {
                                val service = WorkerSkillService()
                                val response = service.workerConfirmsJob(ConfirmWorkerRequest(0,job.id,0,4),tokenProvider)
                                if(response.success){
                                    Toast.makeText(context,"Uspješno ste prihvatili posao",Toast.LENGTH_SHORT).show()
                                    navController.popBackStack()
                                    navController.navigate("jobRoom/${job.id}")

                                }
                            }
                        }
                    ){
                        Text(text = "Prihvati")

                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            corutine.launch {
                                val service = WorkerSkillService()
                                val response = service.workerConfirmsJob(ConfirmWorkerRequest(0,job.id,0,5),tokenProvider)
                                if(response.success){
                                    Toast.makeText(context,"Uspješno ste odbili posao",Toast.LENGTH_SHORT).show()
                                    navController.popBackStack()
                                }
                            }
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

