package hr.foi.air.baufind.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import hr.foi.air.baufind.R
import hr.foi.air.baufind.service.SkillsService.SkillsService
import hr.foi.air.baufind.ui.screens.JobRoom.JobRoomScreen
import hr.foi.air.baufind.ui.theme.LightPrimary
import hr.foi.air.baufind.ws.model.JobRoom
import hr.foi.air.baufind.ws.model.Skill
import hr.foi.air.baufind.ws.network.TokenProvider

@Composable
fun RoleInJobCard(
    allowedInvitations: Boolean,
    listOfSkills: List<Skill>,
    navController: NavController,
    peopleInRoom: Map<String, List<String>>,
    Jobid: Int,
    jobRoom: List<JobRoom>
) {
    Column {
        for (person in peopleInRoom) {
            Column(modifier = Modifier.padding(top = 16.dp)) {
                Text(
                    text = person.key,
                    modifier = Modifier.padding(6.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )

                val confirmedWorkers = jobRoom.filter { job ->
                    job.skillTitle == person.key && job.workingStatus == "Potvrden"
                }.map { it.workerName }

                if (confirmedWorkers.isNotEmpty()) {
                    confirmedWorkers.forEach { worker ->
                        PersonInRoomCard(workerName = worker, onItemClick = { })
                    }
                } else if (allowedInvitations) {
                    Button(
                        modifier = Modifier.padding(6.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LightPrimary,
                            contentColor = Color.White
                        ),
                        onClick = {
                            for (skill in listOfSkills) {
                                if (skill.title == person.key) {
                                    navController.navigate("workersSearchScreen/[${skill.id}]/${Jobid}")
                                }
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.add_person_icon),
                            contentDescription = "Add Worker Icon",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun RoleInJobCardPreview() {
    RoleInJobCard(
        allowedInvitations = true,
        listOfSkills = listOf(Skill(1, "Električar"), Skill(2, "Vodoinstalater")),
        navController = NavController(LocalContext.current),
        peopleInRoom = mapOf(
            "skill koji samo David ima" to listOf("David Matijanić"),
            "prvi skill" to listOf("Viktor","string", "Mateo Vujica")
        ),
        19,
        listOf(JobRoom(workingId=467, jobId=163, jobTitle="test2", jobStatus="Zapocet", allowWorkerInvite=true, employerId=9, skillId=1, skillTitle="skill koji samo David ima", workerId=1, workerName="David Matijanić", workingStatus="Potvrden"),
        JobRoom(workingId=468, jobId=163, jobTitle="test2", jobStatus="Zapocet", allowWorkerInvite=true, employerId=9, skillId=1, skillTitle="prvi skill", workerId=6, workerName="Mateo Vujica", workingStatus="Ceka odobrenje poslodavca"),
    JobRoom(workingId=469, jobId=163, jobTitle="test2", jobStatus="Zapocet", allowWorkerInvite=true, employerId=9, skillId=1, skillTitle="prvi skill", workerId=8, workerName="Viktor", workingStatus="Ceka odobrenje poslodavca"),
     JobRoom(workingId=470, jobId=163, jobTitle="test2", jobStatus="Zapocet", allowWorkerInvite=true, employerId=9, skillId=4, skillTitle="prvi skill", workerId=18, workerName="string", workingStatus="Ceka odobrenje poslodavca"))
    )
}
