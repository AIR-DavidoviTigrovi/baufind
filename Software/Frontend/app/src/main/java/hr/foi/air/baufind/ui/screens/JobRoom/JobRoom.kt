package hr.foi.air.baufind.ui.screens.JobRoom

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import hr.foi.air.baufind.service.JobRoomService.RoomOwnerState
import hr.foi.air.baufind.ui.components.RoleInJobCard
import hr.foi.air.baufind.ws.network.TokenProvider
import kotlinx.coroutines.launch


@Composable
fun JobRoomScreen(navController: NavController,tokenProvider: TokenProvider,jobID: Int){
    val viewModel: JobRoomViewModel = viewModel()
    viewModel.tokenProvider.value = tokenProvider
    val context = LocalContext.current
    val scrollableState = rememberScrollState()
    LaunchedEffect(Unit) {
        viewModel.getAllSkills()
        viewModel.getJobRoom(jobID)
        viewModel.loadJobPeople(jobID)
        viewModel.determinateOwner(context)
    }
    var status by remember { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(scrollableState),

    ) {
        if (viewModel.jobRoom.value.isNotEmpty()) {
            Text(text = viewModel.jobRoom.value[0].jobTitle!!)
            status = viewModel.jobRoom.value[0].jobStatus
            RoleInJobCard(
                viewModel.jobRoom.value[0].allowWorkerInvite,
                viewModel.roomSkills.value,
                navController = navController,
                peopleInRoom = viewModel.peopleInRoom,
                jobID,
                viewModel.jobRoom.value
            )
            if(viewModel.roomOwnerState.value == RoomOwnerState.Employer){
                when (status) {
                    "Zavrsen" -> {
                        Text("Posao je završen")
                    }
                    "Zapocet" -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom
                        ) {
                            Button(
                                onClick = {

                                    viewModel.viewModelScope.launch {
                                        viewModel.setRoomStatus(viewModel.jobRoom.value[0].jobId, 3)
                                        status = "Zavrsen"
                                    }
                                },
                                modifier = Modifier.wrapContentWidth()
                            ) {
                                Text(viewModel.buttonStateText.value)
                            }
                        }
                    }
                    else -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom
                        ) {
                            Button(
                                onClick = {

                                    viewModel.viewModelScope.launch {
                                        viewModel.peopleInRoom.forEach { (skill, people) ->
                                            people.forEach { person ->
                                                if (person == "Nema radnika"){
                                                    Toast.makeText(context, "Please add workers to this room", Toast.LENGTH_SHORT).show()
                                                    return@launch
                                                }
                                            }
                                        }
                                        viewModel.setRoomStatus(viewModel.jobRoom.value[0].jobId, 4)
                                        status = "Zapocet"
                                    }
                                },
                                modifier = Modifier.wrapContentWidth()
                            ) {
                                Text(viewModel.buttonStateText.value)
                            }
                        }
                    }
                }


            }

        } else {

            Text(text = "Loading...")
        }
    DisposableEffect(Unit) {
        onDispose {
            viewModel.peopleInRoom.clear()
        }
    }

    }
}
@Preview(showBackground = true)
@Composable
fun JobRoomScreenPreview() {
    JobRoomScreen(navController = NavController(LocalContext.current),tokenProvider = object : TokenProvider { override fun getToken(): String? { return null }},9)
}