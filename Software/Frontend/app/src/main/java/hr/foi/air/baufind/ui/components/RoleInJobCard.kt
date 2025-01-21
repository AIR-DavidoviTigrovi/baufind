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
import androidx.compose.runtime.DisposableEffect
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
import hr.foi.air.baufind.service.JobRoomService.RoomOwnerState
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
    jobRoom: List<JobRoom>,
    roomOwnerState: RoomOwnerState
) {
    // Grouping job rooms by skill title
    val roomStructureCount = jobRoom.groupBy { it.skillTitle }.mapValues { entry ->
        entry.value.count { it.workerId == null }
    }

    // Extracting people IDs from job rooms
    val peopleIds = jobRoom.mapNotNull { it.workerId }.toMutableList()
    if (peopleIds.isEmpty()) {
        peopleIds.add(0)
    }

    Column {
        for (person in peopleInRoom) {
            val skillTitle = person.key
            val requiredCount = roomStructureCount[skillTitle] ?: 0

            val confirmedWorkers = jobRoom.filter { job ->
                job.skillTitle == skillTitle && job.workingStatus == "Potvrden"
            }.map { it.workerName }

            val noWorkerJobs = jobRoom.filter { job ->
                job.skillTitle == skillTitle && job.workingStatus == "Nema radnika"
            }

            if (confirmedWorkers.isNotEmpty() || noWorkerJobs.isNotEmpty()) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    Text(
                        text = skillTitle,
                        modifier = Modifier.padding(6.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )

                    if (confirmedWorkers.isNotEmpty()) {
                        confirmedWorkers.forEach { worker ->
                            PersonInRoomCard(workerName = worker, onItemClick = { })
                        }
                    }

                    if (noWorkerJobs.isNotEmpty() || confirmedWorkers.size < requiredCount) {
                        if (roomOwnerState == RoomOwnerState.Employer || allowedInvitations) {
                            Button(
                                modifier = Modifier.padding(6.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = LightPrimary,
                                    contentColor = Color.White
                                ),
                                onClick = {
                                    listOfSkills.firstOrNull { it.title == skillTitle }?.let { skill ->
                                        navController.navigate("workersSearchScreen/[${skill.id}]/${peopleIds}/${Jobid}")
                                    }
                                }
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.add_person_icon),
                                    contentDescription = "Add Worker Icon",
                                    modifier = Modifier.size(24.dp)
                                )
                                Text(text = "Dodaj radnika", modifier = Modifier.padding(start = 8.dp))
                            }
                        }
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
            "skill koji samo David ima" to listOf("string1", "string","string1","string"),
            "prvi skill" to listOf( "Viktor", "Nema radnika","Nema radnika","Nema radnika")
        ),
        19,
        listOf(
            JobRoom(
                workingId = 495, jobId = 176, jobTitle = "stringares", jobStatus = "Nema radnika", allowWorkerInvite = true,
                employerId = 19, skillId = 4, skillTitle = "skill koji samo David ima", workerId = 9, workerName = "string1", workingStatus = "Potvrden"
            ),
            JobRoom(
                workingId = 496, jobId = 176, jobTitle = "stringares", jobStatus = "Nema radnika", allowWorkerInvite = true,
                employerId = 19, skillId = 4, skillTitle = "skill koji samo David ima", workerId = null, workerName = "string", workingStatus = "Potvrden"
            ),
            JobRoom(
                workingId = 497, jobId = 176, jobTitle = "stringares", jobStatus = "Nema radnika", allowWorkerInvite = true,
                employerId = 19, skillId = 1, skillTitle = "prvi skill", workerId = null, workerName = "Nema radnika", workingStatus = "Nema radnika"
            ),
            JobRoom(
                workingId = 498, jobId = 176, jobTitle = "stringares", jobStatus = "Nema radnika", allowWorkerInvite = true,
                employerId = 19, skillId = 1, skillTitle = "prvi skill", workerId = null, workerName = "Nema radnika", workingStatus = "Nema radnika"
            ),
            JobRoom(
                workingId = 499, jobId = 176, jobTitle = "stringares", jobStatus = "Nema radnika", allowWorkerInvite = true,
                employerId = 19, skillId = 1, skillTitle = "prvi skill", workerId = null, workerName = "Nema radnika", workingStatus = "Nema radnika"
            ),
            JobRoom(
                workingId = 500, jobId = 176, jobTitle = "stringares", jobStatus = "Nema radnika", allowWorkerInvite = true,
                employerId = 19, skillId = 4, skillTitle = "skill koji samo David ima", workerId = 9, workerName = "string1", workingStatus = "Ima zahtjev koji mora prihvatiti"
            ),
            JobRoom(
                workingId = 501, jobId = 176, jobTitle = "stringares", jobStatus = "Nema radnika", allowWorkerInvite = true,
                employerId = 19, skillId = 4, skillTitle = "skill koji samo David ima", workerId = 18, workerName = "string", workingStatus = "Ima zahtjev koji mora prihvatiti"
            ),
            JobRoom(
                workingId = 502, jobId = 176, jobTitle = "stringares", jobStatus = "Nema radnika", allowWorkerInvite = true,
                employerId = 19, skillId = 1, skillTitle = "prvi skill", workerId = 8, workerName = "Viktor", workingStatus = "Ima zahtjev koji mora prihvatiti"
            )
        ),
        RoomOwnerState.Worker
    )
}

